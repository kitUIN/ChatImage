package io.github.kituin.chatimage.network;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import io.github.kituin.ChatImageCode.ChatImageFrame;
import io.github.kituin.ChatImageCode.ChatImageIndex;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static io.github.kituin.ChatImageCode.ChatImageCodeInstance.LOGGER;
import static io.github.kituin.ChatImageCode.ClientStorage.AddImageError;
import static io.github.kituin.ChatImageCode.ClientStorage.CLIENT_CACHE_MAP;
import static io.github.kituin.ChatImageCode.NetworkHelper.mergeFileBlocks;
import static io.github.kituin.ChatImageCode.ServerStorage.*;

public class ChatImagePacket {

    public static Gson gson = new Gson();


    /**
     * 发送给服务器一连串网络包(异步)
     * 文件频道
     * @param bufs 网络包数据列表
     */
    public static void sendFilePackets(List<String> bufs) {
        for (String buf : bufs) {
            FileChannel.sendToServer(new FileChannelPacket(buf));
        }
    }

    /**
     * 尝试从服务器获取图片
     *
     * @param url 图片url
     */
    public static void loadFromServer(String url) {
        if (Minecraft.getInstance().player != null) {
            FileInfoChannel.sendToServer(new FileInfoChannelPacket(url));
            LOGGER.info("[try get from server]" + url);

        } else {
            AddImageError(url, ChatImageFrame.FrameError.FILE_NOT_FOUND);
        }
    }


    /**
     * 服务端接收 图片文件分块 的处理
     *
     * @param player ServerPlayer
     * @param res    String
     */
    public static void serverFileChannelReceived(ServerPlayer player, String res) {
        ChatImageIndex title = gson.fromJson(res, ChatImageIndex.class);
        HashMap<Integer, String> blocks = SERVER_BLOCK_CACHE.containsKey(title.url) ? SERVER_BLOCK_CACHE.get(title.url) : new HashMap<>();
        blocks.put(title.index, res);
        SERVER_BLOCK_CACHE.put(title.url, blocks);
        FILE_COUNT_MAP.put(title.url, title.total);
        LOGGER.info("[FileChannel->Server:" + title.index + "/" + title.total + "]" + title.url);
        if (title.total == blocks.size()) {
            if (USER_CACHE_MAP.containsKey(title.url)) {
                // 通知之前请求但是没图片的客户端
                List<String> names = USER_CACHE_MAP.get(title.url);
                for (String uuid : names) {
                    FileBackChannel.sendToPlayer(new FileInfoChannelPacket("true->" + title.url), player.server.getPlayerList().getPlayer(UUID.fromString(uuid)));
                    LOGGER.info("[echo to client(" + uuid + ")]" + title.url);
                }
                USER_CACHE_MAP.put(title.url, Lists.newArrayList());
            }
            LOGGER.info("[FileChannel->Server]" + title.url);
        }
    }
    /**
     * 客户端接收 下载文件分块 处理
     *
     * @param res String
     */
    public static void clientDownloadFileChannelReceived(String res) {
        ChatImageIndex title = gson.fromJson(res, ChatImageIndex.class);
        HashMap<Integer, ChatImageIndex> blocks = CLIENT_CACHE_MAP.containsKey(title.url) ? CLIENT_CACHE_MAP.get(title.url) : new HashMap<>();
        blocks.put(title.index, title);
        CLIENT_CACHE_MAP.put(title.url, blocks);
        LOGGER.info("[DownloadFile(" +title.index+ "/"+ title.total +")]" + title.url);
        if (blocks.size() == title.total) {
            mergeFileBlocks(title.url,blocks);
            LOGGER.info("[DownloadFileChannel-Merge]" + title.url);
        }
    }



    public static void clientFileInfoChannelReceived(String data){
        String url = data.substring(6);
        LOGGER.info(url);
        if (data.startsWith("null")) {
            LOGGER.info("[GetFileChannel-NULL]{}", url);
            AddImageError(url, ChatImageFrame.FrameError.FILE_NOT_FOUND);
        } else if (data.startsWith("true")) {
            LOGGER.info("[GetFileChannel-Retry]{}", url);
            loadFromServer(url);
        }
    }

    public static void serverFileInfoChannelReceived(ServerPlayer player, String url) {
        if (SERVER_BLOCK_CACHE.containsKey(url) && FILE_COUNT_MAP.containsKey(url)) {
            HashMap<Integer, String> list = SERVER_BLOCK_CACHE.get(url);
            Integer total = FILE_COUNT_MAP.get(url);
            if (total == list.size()) {
                // 服务器存在缓存图片,直接发送给客户端
                for (Map.Entry<Integer, String> entry : list.entrySet()) {
                    LOGGER.debug("[GetFileChannel->Client:{}/{}]{}", entry.getKey(), list.size() - 1, url);
                    DownloadFileChannel.sendToPlayer(new DownloadFileChannelPacket(entry.getValue()), player);
                }
                LOGGER.info("[GetFileChannel->Client]{}", url);
                return;
            }
        }
        //通知客户端无文件
        FileBackChannel.sendToPlayer(new FileInfoChannelPacket("null->" + url), player);
        LOGGER.error("[GetFileChannel]not found in server:{}", url);
        // 记录uuid,后续有文件了推送
        List<String> names = USER_CACHE_MAP.containsKey(url) ? USER_CACHE_MAP.get(url) : Lists.newArrayList();
        if (player != null) {
            names.add(player.getStringUUID());
        }
        USER_CACHE_MAP.put(url, names);
        LOGGER.info("[GetFileChannel]记录uuid:{}", player.getStringUUID());
        LOGGER.info("[not found in server]{}", url);
    }
}