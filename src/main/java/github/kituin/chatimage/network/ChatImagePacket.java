package github.kituin.chatimage.network;

import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import github.kituin.chatimage.tool.ChatImageFrame;
import github.kituin.chatimage.tool.ChatImageUrl;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static github.kituin.chatimage.tool.ChatImageCode.CACHE_MAP;

public class ChatImagePacket {
    private static final Logger LOGGER = LogUtils.getLogger();
    /**
     * 客户端发送文件分块到服务器通道(Map)
     */
    public static Identifier FILE_CHANNEL = new Identifier("chatimage", "file_channel");
    /**
     * 尝试获取图片请求通道(String)
     */
    public static Identifier GET_FILE_CHANNEL = new Identifier("chatimage", "get_file_channel");
    /**
     * 发送文件分块到客户端通道(Map)
     */
    public static Identifier DOWNLOAD_FILE_CHANNEL = new Identifier("chatimage", "download_file_channel");
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
     * 本地块缓存
     */
    public static HashMap<String, HashMap<Integer, byte[]>> CLIENT_CACHE_MAP = new HashMap<>();

    /**
     * 创建一个Map网络包
     *
     * @param key 键
     * @param byt bytes
     * @return {@link PacketByteBuf}
     */
    public static PacketByteBuf createMapPacket(String key, byte[] byt) {
        PacketByteBuf buf = PacketByteBufs.create();
        HashMap<String, byte[]> fileMap = new HashMap<>();
        fileMap.put(key, byt);
        buf.writeMap(fileMap, PacketByteBuf::writeString, PacketByteBuf::writeByteArray);
        return buf;
    }

    /**
     * 创建一个String网络包
     *
     * @param str 字符串
     * @return {@link PacketByteBuf}
     */
    public static PacketByteBuf createStringPacket(String str) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(str);
        return buf;
    }

    /**
     * 创建一个分块Map网络包
     *
     * @param index 分块序号
     * @param total 分块总数
     * @param url   url值
     * @param byt   网络包数据
     * @return {@link PacketByteBuf}
     */
    public static PacketByteBuf createMapPacket(int index, int total, String url, byte[] byt) {
        return createMapPacket(index + "->" + total + "->" + url, byt);
    }

    /**
     * 创建文件分块Map网络包列表
     *
     * @param url  url
     * @param file 文件
     * @return {@link List<PacketByteBuf>}
     */
    public static List<PacketByteBuf> createFilePacket(String url, File file) {
        try (InputStream input = new FileInputStream(file)) {
            List<PacketByteBuf> bufs = Lists.newArrayList();
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
                bufs.add(createMapPacket(i, total, url, cipher));
            }
            return bufs;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 发送一个网络包(异步)
     *
     * @param player  发送者(ClientPlayerEntity状态为发送给服务器,反之则发送给玩家)
     * @param channel 发送频道
     * @param buf     网络包数据
     */
    public static void sendPacketAsync(PlayerEntity player, Identifier channel, PacketByteBuf buf) {
        CompletableFuture.supplyAsync(() -> {
            if (player instanceof ClientPlayerEntity) {
                ClientPlayNetworking.send(channel, buf);
            } else if (player instanceof ServerPlayerEntity) {
                ServerPlayNetworking.send((ServerPlayerEntity) player, channel, buf);
            }
            return null;
        });
    }

    /**
     * 发送一连串网络包(异步)
     *
     * @param player  发送者(ClientPlayerEntity状态为发送给服务器,反之则发送给玩家)
     * @param channel 发送频道
     * @param bufs    网络包数据列表
     */
    public static void sendPacketAsync(PlayerEntity player, Identifier channel, List<PacketByteBuf> bufs) {
        for (PacketByteBuf buf : bufs) {
            sendPacketAsync(player, channel, buf);
        }
    }


    /**
     * 尝试从服务器获取图片
     *
     * @param url 图片url
     */
    public static void loadFromServer(String url) {
        if (MinecraftClient.getInstance().player != null) {
            sendPacketAsync(MinecraftClient.getInstance().player, GET_FILE_CHANNEL, createStringPacket(url));
            LOGGER.info("[try get from server]" + url);
        } else {
            CACHE_MAP.put(url, new ChatImageFrame<>(ChatImageFrame.FrameError.FILE_NOT_FOUND));
        }
    }

    /**
     * 使用->切分字符串
     *
     * @param s 原始字符串
     * @return 切分后的字符串
     */
    public static String[] splits(String s) {
        return StringUtils.split(s, "->");
    }

    public static Map<String, byte[]> readMap(PacketByteBuf buf) {
        return buf.readMap(PacketByteBuf::readString, PacketByteBuf::readByteArray);
    }

    /**
     * 服务端接收 图片文件分块 的处理
     *
     * @param server server
     * @param buf    PacketByteBuf
     */
    public static void serverFileChannelReceived(MinecraftServer server, PacketByteBuf buf) {
        for (Map.Entry<String, byte[]> entry : ChatImagePacket.readMap(buf).entrySet()) {
            String[] order = ChatImagePacket.splits(entry.getKey());
            String url = order[2];
            int index = Integer.parseInt(order[0]);
            int total = Integer.parseInt(order[1]);
            HashMap<Integer, byte[]> blocks = SERVER_BLOCK_CACHE.containsKey(url) ? SERVER_BLOCK_CACHE.get(url) : new HashMap<>();
            if (total == blocks.size()) {
                if (USER_CACHE_MAP.containsKey(url)) {
                    // 通知之前请求但是没图片的客户端
                    List<String> names = USER_CACHE_MAP.get(url);
                    for (String uuid : names) {
                        ServerPlayerEntity serverPlayer = server.getPlayerManager().getPlayer(UUID.fromString(uuid));
                        sendPacketAsync(serverPlayer, GET_FILE_CHANNEL, createStringPacket("true->" + url));
                        LOGGER.info("[echo to client(" + uuid + ")]" + url);
                    }
                    USER_CACHE_MAP.put(url, Lists.newArrayList());
                }
            } else {
                blocks.put(index, entry.getValue());
                SERVER_BLOCK_CACHE.put(url, blocks);
                FILE_COUNT_MAP.put(url, total);
                LOGGER.info("[put to server:" + index + "/" + (total - 1) + "]" + url);
            }
        }
    }

    /**
     * 服务端接收 客户端试图获取图片文件 的处理
     *
     * @param player 玩家
     * @param buf    PacketByteBuf
     */
    public static void serverGetFileChannelReceived(ServerPlayerEntity player, PacketByteBuf buf) {
        String url = buf.readString();
        if (SERVER_BLOCK_CACHE.containsKey(url) && FILE_COUNT_MAP.containsKey(url)) {
            HashMap<Integer, byte[]> list = SERVER_BLOCK_CACHE.get(url);
            Integer total = FILE_COUNT_MAP.get(url);
            if (total == list.size()) {
                // 服务器存在缓存图片,直接发送给客户端
                for (Map.Entry<Integer, byte[]> entry : list.entrySet()) {
                    sendPacketAsync(player, DOWNLOAD_FILE_CHANNEL, createMapPacket(entry.getKey(), total, url, entry.getValue()));
                    LOGGER.info("[send to client:" + entry.getKey() + "/" + (list.size() - 1) + "]" + url);
                }
                return;
            }
        }
        //通知客户端无文件
        sendPacketAsync(player, GET_FILE_CHANNEL, createStringPacket("null->" + url));
        // 记录uuid,后续有文件了推送
        List<String> names = USER_CACHE_MAP.containsKey(url) ? USER_CACHE_MAP.get(url) : Lists.newArrayList();
        names.add(player.getUuidAsString());
        USER_CACHE_MAP.put(url, names);
        LOGGER.info("[not found in server]" + url);

    }

    /**
     * 客户端接收 无文件 处理
     *
     * @param buf PacketByteBuf
     */
    public static void clientGetFileChannelReceived(PacketByteBuf buf) {
        String[] order = ChatImagePacket.splits(buf.readString());
        if ("null".equals(order[0])) {
            CACHE_MAP.put(order[1], new ChatImageFrame<>(ChatImageFrame.FrameError.FILE_NOT_FOUND));
        } else if ("true".equals(order[0])) {
            loadFromServer(order[1]);
        }
    }


    /**
     * 客户端接收 下载文件分块 处理
     *
     * @param buf PacketByteBuf
     */
    public static void clientDownloadFileChannelReceived(PacketByteBuf buf) {
        for (Map.Entry<String, byte[]> entry : readMap(buf).entrySet()) {
            String[] order = ChatImagePacket.splits(entry.getKey());
            String url = order[2];
            int index = Integer.parseInt(order[0]);
            int total = Integer.parseInt(order[1]);
            HashMap<Integer, byte[]> blocks = CLIENT_CACHE_MAP.containsKey(url) ? CLIENT_CACHE_MAP.get(url) : new HashMap<>();
            blocks.put(index, entry.getValue());
            CLIENT_CACHE_MAP.put(url, blocks);
            if (blocks.size() == total) {
                // 合并文件分块
                LOGGER.info("[merge]" + url);
                int length = 0;
                for (Map.Entry<Integer, byte[]> en : blocks.entrySet()) {
                    length += en.getValue().length;
                }
                ByteBuffer bb = ByteBuffer.allocate(length);
                for (int i = 0; i < blocks.size(); i++) {
                    bb.put(blocks.get(i));
                }
                try {
                    ChatImageUrl.loadLocalFile(new ByteArrayInputStream(bb.array()), url);
                } catch (IOException e) {
                    LOGGER.error("[merge load error]" + e.toString());
                    CACHE_MAP.put(url, new ChatImageFrame<>(ChatImageFrame.FrameError.SERVER_FILE_LOAD_ERROR));
                }
            }
        }
    }
}
