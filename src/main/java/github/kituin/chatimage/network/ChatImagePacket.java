package github.kituin.chatimage.network;

import io.github.kituin.ChatImageCode.ChatImageFrame;
import io.github.kituin.ChatImageCode.ChatImageIndex;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static io.github.kituin.ChatImageCode.ChatImageHandler.AddChatImageError;
import static io.github.kituin.ChatImageCode.ChatImagePacketHelper.*;
import static io.github.kituin.ChatImageCode.ChatImagePacketHelper.CLIENT_CACHE_MAP;

public class ChatImagePacket {

    public static Gson gson = new Gson();


    private static final Logger LOGGER = LogManager.getLogger();

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
            AddChatImageError(url, ChatImageFrame.FrameError.FILE_NOT_FOUND);
        }
    }


    /**
     * 服务端接收 图片文件分块 的处理
     *
     * @param player ServerPlayer
     * @param res    String
     */
    public static void serverFileChannelReceived(ServerPlayerEntity player, String res) {
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
                    ServerPlayerEntity serverPlayer = player.server.getPlayerList().getPlayerByUUID(UUID.fromString(uuid));
                    FileBackChannel.sendToPlayer(new FileInfoChannelPacket("true->" + title.url), serverPlayer);
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
            try {
                mergeFileBlocks(title.url,blocks);
                LOGGER.info("[DownloadFileChannel-Merge]" + title.url);
            } catch (IOException e) {
                LOGGER.error("[DownloadFileChannel-Error]" + title.url);
            }
        }
    }
}
