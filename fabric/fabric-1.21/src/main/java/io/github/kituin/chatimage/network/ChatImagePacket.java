package io.github.kituin.chatimage.network;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import io.github.kituin.ChatImageCode.ChatImageFrame;
import io.github.kituin.ChatImageCode.ChatImageIndex;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static io.github.kituin.ChatImageCode.ClientStorage.AddImageError;
import static io.github.kituin.ChatImageCode.ClientStorage.CLIENT_CACHE_MAP;
import static io.github.kituin.ChatImageCode.NetworkHelper.mergeFileBlocks;
import static io.github.kituin.ChatImageCode.ServerStorage.*;
import static io.github.kituin.chatimage.ChatImage.LOGGER;

public class ChatImagePacket {

    public static Gson gson = new Gson();

    /**
     * 发送给客户端一个网络包(异步)
     *
     * @param player 发送者(ClientPlayerEntity状态为发送给服务器,反之则发送给玩家)
     * @param packet 网络包数据
     */
    public static void sendPacketAsync(ServerPlayerEntity player, CustomPayload packet) {
        CompletableFuture.supplyAsync(() -> {
            ServerPlayNetworking.send(player, packet);
            return null;
        });
    }

    /**
     * 发送给服务器一个网络包(异步)
     *
     * @param packet 网络包数据
     */
    @Environment(EnvType.CLIENT)
    public static void sendPacketAsync(CustomPayload packet) {
        CompletableFuture.supplyAsync(() -> {
            ClientPlayNetworking.send(packet);
            return null;
        });
    }

    /**
     * 发送给服务器一连串网络包(异步)
     *
     * @param handler 处理
     * @param bufs    网络包数据列表
     */
    public static void sendPacketAsync(Function<String, CustomPayload> handler, List<String> bufs) {
        for (String buf : bufs) {
            sendPacketAsync(handler.apply(buf));
        }
    }


    /**
     * 尝试从服务器获取图片
     *
     * @param url 图片url
     */
    public static void loadFromServer(String url) {
        if (MinecraftClient.getInstance().player != null) {
            sendPacketAsync(new FileInfoChannelPacket(url));
            LOGGER.info("[GetFileChannel-Try]" + url);
        } else {
            AddImageError(url, ChatImageFrame.FrameError.FILE_NOT_FOUND);
        }
    }

    /**
     * 服务端接收 图片文件分块 的处理
     *
     * @param packet  FileInfoChannelPacket
     * @param content ClientPlayNetworking.Context
     */
    public static void serverFileChannelReceived(FileChannelPacket packet, ServerPlayNetworking.Context content) {
        MinecraftServer server = content.player().server;
        String res = packet.message();
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
                    ServerPlayerEntity serverPlayer = server.getPlayerManager().getPlayer(UUID.fromString(uuid));
                    sendPacketAsync(serverPlayer, new FileInfoChannelPacket("true->" + title.url));
                    LOGGER.info("[FileChannel->Client(" + uuid + ")]" + title.url);
                }
                USER_CACHE_MAP.put(title.url, Lists.newArrayList());
            }
            LOGGER.info("[FileChannel->Server]" + title.url);
        }
    }

    /**
     * 服务端接收 客户端试图获取图片文件 的处理
     *
     * @param packet  FileInfoChannelPacket
     * @param content ServerPlayNetworking.Context
     */
    public static void serverGetFileChannelReceived(FileInfoChannelPacket packet, ServerPlayNetworking.Context content) {
        ServerPlayerEntity player = content.player();
        String url = packet.message();
        if (SERVER_BLOCK_CACHE.containsKey(url) && FILE_COUNT_MAP.containsKey(url)) {
            HashMap<Integer, String> list = SERVER_BLOCK_CACHE.get(url);
            Integer total = FILE_COUNT_MAP.get(url);
            if (total == list.size()) {
                // 服务器存在缓存图片,直接发送给客户端
                for (Map.Entry<Integer, String> entry : list.entrySet()) {
                    sendPacketAsync(player, new DownloadFileChannelPacket(entry.getValue()));
                    LOGGER.debug("[GetFileChannel->Client:" + entry.getKey() + "/" + (list.size() - 1) + "]" + url);
                }
                LOGGER.info("[GetFileChannel->Client]" + url);
                return;
            }
        }

        //通知客户端无文件
        sendPacketAsync(player, new FileInfoChannelPacket("null->" + url));
        LOGGER.error("[GetFileChannel]not found in server:" + url);
        // 记录uuid,后续有文件了推送
        List<String> names = USER_CACHE_MAP.containsKey(url) ? USER_CACHE_MAP.get(url) : Lists.newArrayList();
        names.add(player.getUuidAsString());
        USER_CACHE_MAP.put(url, names);
        LOGGER.info("[GetFileChannel]记录uuid:" + player.getUuidAsString());
    }

    /**
     * 客户端接收 无文件 处理
     *
     * @param packet FileInfoChannelPacket
     */
    public static void clientGetFileChannelReceived(FileInfoChannelPacket packet) {
        String data = packet.message();
        String url = data.substring(6);
        LOGGER.info(url);
        if (data.startsWith("null")) {
            LOGGER.info("[GetFileChannel-NULL]" + url);
            AddImageError(url, ChatImageFrame.FrameError.FILE_NOT_FOUND);
        } else if (data.startsWith("true")) {
            loadFromServer(url);
        }
    }


    /**
     * 客户端接收 下载文件分块 处理
     *
     * @param packet DownloadFileChannelPacket
     */
    public static void clientDownloadFileChannelReceived(DownloadFileChannelPacket packet) {
        String res = packet.message();
        ChatImageIndex title = gson.fromJson(res, ChatImageIndex.class);
        HashMap<Integer, ChatImageIndex> blocks = CLIENT_CACHE_MAP.containsKey(title.url) ? CLIENT_CACHE_MAP.get(title.url) : new HashMap<>();
        blocks.put(title.index, title);
        CLIENT_CACHE_MAP.put(title.url, blocks);
        LOGGER.info("[DownloadFile(" + title.index + "/" + title.total + ")]" + title.url);
        if (blocks.size() == title.total) {
            LOGGER.info(String.valueOf(blocks));
            mergeFileBlocks(title.url, blocks);
            LOGGER.info("[DownloadFileChannel-Merge]" + title.url);
        }
    }
}
