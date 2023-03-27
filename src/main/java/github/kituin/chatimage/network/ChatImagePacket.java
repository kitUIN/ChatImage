package github.kituin.chatimage.network;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.mojang.logging.LogUtils;
import github.kituin.chatimage.tool.ChatImageFrame;
import github.kituin.chatimage.tool.ChatImageIndex;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static github.kituin.chatimage.ChatImage.LOGGER;
import static github.kituin.chatimage.tool.ChatImageHandler.AddChatImageError;
import static github.kituin.chatimage.tool.ChatImageHandler.loadFile;

public class ChatImagePacket {

    /**
     * 服务器文件分块缓存 URL->MAP(序号,数据)
     */
    public static HashMap<String, HashMap<Integer, byte[]>> SERVER_BLOCK_CACHE = new HashMap<>();

    /**
     * 文件分块总数记录 URL->Total
     */
    public static HashMap<String, Integer> FILE_COUNT_MAP = new HashMap<>();
    /**
     * 广播列表 URL->List(UUID)
     */
    public static HashMap<String, List<String>> USER_CACHE_MAP = new HashMap<>();
    /**
     * 用户本地分块缓存
     */
    public static HashMap<String, HashMap<Integer, byte[]>> CLIENT_CACHE_MAP = new HashMap<>();
    public static Gson gson = new Gson();

    public static DownloadFileChannelPacket createDownloadFilePacket(int index, int total, String url, byte[] bytes) {
        HashMap<String, byte[]> fileMap = new HashMap<>();
        fileMap.put(gson.toJson(new ChatImageIndex(index, total, url)), bytes);
        return new DownloadFileChannelPacket(fileMap);
    }

    public static FileChannelPacket createFilePacket(int index, int total, String url, byte[] bytes) {
        HashMap<String, byte[]> fileMap = new HashMap<>();
        fileMap.put(gson.toJson(new ChatImageIndex(index, total, url)), bytes);
        return new FileChannelPacket(fileMap);
    }

    /**
     * 创建文件分块Map网络包列表
     *
     * @param url  url
     * @param file 文件
     * @return {@link List<FileChannelPacket>}
     */
    public static List<FileChannelPacket> createFilePacket(String url, File file) {
        try (InputStream input = new FileInputStream(file)) {
            List<FileChannelPacket> bufs = Lists.newArrayList();
            byte[] byt = new byte[input.available()];
            int limit = 29000 - url.getBytes().length;
            int status = input.read(byt);
            ByteBuffer bb = ByteBuffer.wrap(byt);
            int count = byt.length / limit;
            int total;
            if (byt.length % limit == 0) {
                total = count;
            } else {
                total = count + 1;
            }
            for (int i = 0; i <= count; i++) {
                int bLength = limit;
                if (i == count) {
                    bLength = byt.length - i * limit;
                    if (bLength == 0) {
                        break;
                    }
                }
                byte[] cipher = new byte[bLength];
                bb.get(cipher, 0, cipher.length).array();
                bufs.add(createFilePacket(i, total, url, cipher));
            }
            return bufs;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 发送给服务器一连串网络包(异步)
     *
     * @param bufs 网络包数据列表
     */
    public static void sendFilePackets(List<FileChannelPacket> bufs) {
        for (FileChannelPacket buf : bufs) {
            FileChannel.sendToServer(buf);
        }
    }

    /**
     * 尝试从服务器获取图片
     *
     * @param url 图片url
     */
    public static void loadFromServer(String url) {
        if (Minecraft.getInstance().player != null) {
            GetFileChannel.sendToServer(new GetFileChannelPacket(url));
            LogUtils.getLogger().info("[try get from server]" + url);
        } else {
            AddChatImageError(url, ChatImageFrame.FrameError.FILE_NOT_FOUND);
        }
    }


    /**
     * 服务端接收 图片文件分块 的处理
     *
     * @param player ServerPlayer
     * @param buf    Map<String, byte[]>
     */
    public static void serverFileChannelReceived(ServerPlayer player, Map<String, byte[]> buf) {
        for (Map.Entry<String, byte[]> entry : buf.entrySet()) {
            ChatImageIndex title = gson.fromJson(entry.getKey(), ChatImageIndex.class);
            HashMap<Integer, byte[]> blocks = SERVER_BLOCK_CACHE.containsKey(title.url) ? SERVER_BLOCK_CACHE.get(title.url) : new HashMap<>();
            blocks.put(title.index, entry.getValue());
            SERVER_BLOCK_CACHE.put(title.url, blocks);
            FILE_COUNT_MAP.put(title.url, title.total);
            LOGGER.debug("[FileChannel->Server:" + title.index + "/" + (title.total - 1) + "]" + title.url);
            if (title.total == blocks.size()) {
                if (USER_CACHE_MAP.containsKey(title.url)) {
                    // 通知之前请求但是没图片的客户端
                    List<String> names = USER_CACHE_MAP.get(title.url);
                    for (String uuid : names) {
                        ServerPlayer serverPlayer = player.server.getPlayerList().getPlayer(UUID.fromString(uuid));
                        GetFileChannel.sendToPlayer(new GetFileChannelPacket("true->" + title.url), serverPlayer);
                        LOGGER.info("[echo to client(" + uuid + ")]" + title.url);
                    }
                    USER_CACHE_MAP.put(title.url, Lists.newArrayList());
                }
                LOGGER.info("[FileChannel->Server]" + title.url);
            }
        }
    }

    /**
     * 服务端接收 客户端试图获取图片文件 的处理
     *
     * @param player 玩家
     * @param url    String
     */
    public static void serverGetFileChannelReceived(ServerPlayer player, String url) {
        if (SERVER_BLOCK_CACHE.containsKey(url) && FILE_COUNT_MAP.containsKey(url)) {
            HashMap<Integer, byte[]> list = SERVER_BLOCK_CACHE.get(url);
            Integer total = FILE_COUNT_MAP.get(url);
            if (total == list.size()) {
                // 服务器存在缓存图片,直接发送给客户端
                for (Map.Entry<Integer, byte[]> entry : list.entrySet()) {
                    DownloadFileChannel.sendToPlayer(createDownloadFilePacket(entry.getKey(), total, url, entry.getValue()), player);
                    LOGGER.info("[send to client:" + entry.getKey() + "/" + (list.size() - 1) + "]" + url);
                }
                return;
            }
        }
        //通知客户端无文件
        GetFileChannel.sendToPlayer(new GetFileChannelPacket("null->" + url), player);
        // 记录uuid,后续有文件了推送
        List<String> names = USER_CACHE_MAP.containsKey(url) ? USER_CACHE_MAP.get(url) : Lists.newArrayList();
        names.add(player.getStringUUID());
        USER_CACHE_MAP.put(url, names);
        LOGGER.info("[not found in server]" + url);
    }

    /**
     * 客户端接收 无文件 处理
     *
     * @param data String
     */
    public static void clientGetFileChannelReceived(String data) {
        String url = data.substring(6);
        LOGGER.info(url);
        if (data.startsWith("null")) {
            LOGGER.info("[GetFileChannel-NULL]" + url);
            AddChatImageError(url, ChatImageFrame.FrameError.FILE_NOT_FOUND);
        } else if (data.startsWith("true")) {
            loadFromServer(url);
        }
    }


    /**
     * 客户端接收 下载文件分块 处理
     *
     * @param buf PacketByteBuf
     */
    public static void clientDownloadFileChannelReceived(Map<String, byte[]> buf) {
        for (Map.Entry<String, byte[]> entry : buf.entrySet()) {
            ChatImageIndex title = gson.fromJson(entry.getKey(), ChatImageIndex.class);
            HashMap<Integer, byte[]> blocks = CLIENT_CACHE_MAP.containsKey(title.url) ? CLIENT_CACHE_MAP.get(title.url) : new HashMap<>();
            blocks.put(title.index, entry.getValue());
            CLIENT_CACHE_MAP.put(title.url, blocks);
            if (blocks.size() == title.total) {
                // 合并文件分块
                int length = 0;
                for (Map.Entry<Integer, byte[]> en : blocks.entrySet()) {
                    length += en.getValue().length;
                }
                ByteBuffer bb = ByteBuffer.allocate(length);
                for (int i = 0; i < blocks.size(); i++) {
                    bb.put(blocks.get(i));
                }
                LOGGER.info("[DownloadFileChannel-Merge]" + title.url);
                loadFile(bb.array(), title.url);
            }
        }
    }
}
