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
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

// IF fabric-1.21 || fabric-1.20.5
//import net.minecraft.network.packet.CustomPayload;
//import net.minecraft.util.Identifier;
//
//import java.util.function.Function;
// ELSE
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
// END IF

import static io.github.kituin.ChatImageCode.NetworkHelper.mergeFileBlocks;
import static io.github.kituin.ChatImageCode.ServerStorage.*;
import static io.github.kituin.ChatImageCode.ClientStorage.*;
import static io.github.kituin.chatimage.ChatImage.LOGGER;

public class ChatImagePacket {

// IF fabric-1.21 || fabric-1.20.5
// ELSE
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
// END IF

    public static Gson gson = new Gson();

// IF fabric-1.21 || fabric-1.20.5
// ELSE
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
// END IF

// IF fabric-1.21 || fabric-1.20.5
//    /**
//     * 发送给客户端一个网络包(异步)
//     *
//     * @param player 发送者(ClientPlayerEntity状态为发送给服务器,反之则发送给玩家)
//     * @param packet 网络包数据
//     */
//    public static void sendPacketAsync(ServerPlayerEntity player, CustomPayload packet) {
//        CompletableFuture.supplyAsync(() -> {
//            ServerPlayNetworking.send(player, packet);
//            return null;
//        });
//    }
//    /**
//     * 发送给服务器一个网络包(异步)
//     *
//     * @param packet 网络包数据
//     */
//    @Environment(EnvType.CLIENT)
//    public static void sendPacketAsync(CustomPayload packet) {
//        CompletableFuture.supplyAsync(() -> {
//            ClientPlayNetworking.send(packet);
//            return null;
//        });
//    }
//
//    /**
//     * 发送给服务器一连串网络包(异步)
//     *
//     * @param handler 处理
//     * @param bufs    网络包数据列表
//     */
//    public static void sendPacketAsync(Function<String, CustomPayload> handler, List<String> bufs) {
//        for (String buf : bufs) {
//            sendPacketAsync(handler.apply(buf));
//        }
//    }
//
//
//    /**
//     * 尝试从服务器获取图片
//     *
//     * @param url 图片url
//     */
//    public static void loadFromServer(String url) {
//        if (MinecraftClient.getInstance().player != null) {
//            sendPacketAsync(new FileInfoChannelPacket(url));
//            LOGGER.info("[GetFileChannel-Try]{}", url);
//        } else {
//            AddImageError(url, ChatImageFrame.FrameError.FILE_NOT_FOUND);
//        }
//    }
// ELSE
    /**
     * 发送给客户端一个网络包(异步)
     *
     * @param player  发送者(ClientPlayerEntity状态为发送给服务器,反之则发送给玩家)
     * @param channel 发送频道
     * @param buf     网络包数据
     */
    public static void sendPacketAsync(ServerPlayerEntity player, Identifier channel, PacketByteBuf buf) {
        CompletableFuture.supplyAsync(() -> {
            ServerPlayNetworking.send(player, channel, buf);
            return null;
        });
    }

    /**
     * 发送给服务器一个网络包(异步)
     *
     * @param channel 发送频道
     * @param buf     网络包数据
     */
    @Environment(EnvType.CLIENT)
    public static void sendPacketAsync(Identifier channel, PacketByteBuf buf) {
        CompletableFuture.supplyAsync(() -> {
            ClientPlayNetworking.send(channel, buf);
            return null;
        });
    }

    /**
     * 发送给服务器一连串网络包(异步)
     *
     * @param channel 发送频道
     * @param bufs    网络包数据列表
     */
    public static void sendPacketAsync(Identifier channel, List<String> bufs) {
        for (String buf : bufs) {
            sendPacketAsync(channel, createStringPacket(buf));
        }
    }


    /**
     * 尝试从服务器获取图片
     *
     * @param url 图片url
     */
    public static void loadFromServer(String url) {
        if (MinecraftClient.getInstance().player != null) {
            sendPacketAsync(GET_FILE_CHANNEL, createStringPacket(url));
            LOGGER.info("[GetFileChannel-Try]" + url);
        } else {
            AddImageError(url, ChatImageFrame.FrameError.FILE_NOT_FOUND);
        }
    }
// END IF


    /**
     * 服务端接收 图片文件分块 的处理
     *
     */
// IF fabric-1.21 || fabric-1.20.5
//    public static void serverFileChannelReceived(FileChannelPacket packet, ServerPlayNetworking.Context content) {
//        MinecraftServer server = content.player().server;
//        String res = packet.message();
// ELSE
    public static void serverFileChannelReceived(MinecraftServer server, PacketByteBuf buf) {
        String res = buf.readString();
// END IF
        ChatImageIndex title = gson.fromJson(res, ChatImageIndex.class);
        HashMap<Integer, String> blocks = SERVER_BLOCK_CACHE.containsKey(title.url) ? SERVER_BLOCK_CACHE.get(title.url) : new HashMap<>();
        blocks.put(title.index, res);
        SERVER_BLOCK_CACHE.put(title.url, blocks);
        FILE_COUNT_MAP.put(title.url, title.total);
        LOGGER.info("[FileChannel->Server:{}/{}]{}", title.index, title.total, title.url);
        if (title.total == blocks.size()) {
            if (USER_CACHE_MAP.containsKey(title.url)) {
                // 通知之前请求但是没图片的客户端
                List<String> names = USER_CACHE_MAP.get(title.url);
                for (String uuid : names) {
                    ServerPlayerEntity serverPlayer = server.getPlayerManager().getPlayer(UUID.fromString(uuid));
// IF fabric-1.21 || fabric-1.20.5
//                    sendPacketAsync(serverPlayer, new FileInfoChannelPacket("true->" + title.url));
// ELSE
                    sendPacketAsync(serverPlayer, GET_FILE_CHANNEL, createStringPacket("true->" + title.url));
// END IF
                    LOGGER.info("[FileChannel->Client({})]{}", uuid, title.url);
                }
                USER_CACHE_MAP.put(title.url, Lists.newArrayList());
            }
            LOGGER.info("[FileChannel->Server]{}", title.url);
        }
    }

    /**
     * 服务端接收 客户端试图获取图片文件 的处理
     *
     */
// IF fabric-1.21 || fabric-1.20.5
//    public static void serverGetFileChannelReceived(FileInfoChannelPacket packet, ServerPlayNetworking.Context content) {
//        ServerPlayerEntity player = content.player();
//        String url = packet.message();
// ELSE
    public static void serverGetFileChannelReceived(ServerPlayerEntity player, PacketByteBuf buf) {
        String url = buf.readString();
// END IF
        if (SERVER_BLOCK_CACHE.containsKey(url) && FILE_COUNT_MAP.containsKey(url)) {
            HashMap<Integer, String> list = SERVER_BLOCK_CACHE.get(url);
            Integer total = FILE_COUNT_MAP.get(url);
            if (total == list.size()) {
                // 服务器存在缓存图片,直接发送给客户端
                for (Map.Entry<Integer, String> entry : list.entrySet()) {
// IF fabric-1.21 || fabric-1.20.5
//                    sendPacketAsync(player, new DownloadFileChannelPacket(entry.getValue()));
// ELSE
                    sendPacketAsync(player, DOWNLOAD_FILE_CHANNEL, createStringPacket(entry.getValue()));
// END IF
                    LOGGER.debug("[GetFileChannel->Client:{}/{}]{}", entry.getKey(), list.size() - 1, url);
                }
                LOGGER.info("[GetFileChannel->Client]{}", url);
                return;
            }
        }
        // 通知客户端无文件
// IF fabric-1.21 || fabric-1.20.5
//                    sendPacketAsync(player, new FileInfoChannelPacket("null->" + url));
// ELSE
        sendPacketAsync(player, GET_FILE_CHANNEL, createStringPacket("null->" + url));
// END IF
        LOGGER.error("[GetFileChannel]not found in server:{}", url);
        // 记录uuid,后续有文件了推送
        List<String> names = USER_CACHE_MAP.containsKey(url) ? USER_CACHE_MAP.get(url) : Lists.newArrayList();
        names.add(player.getUuidAsString());
        USER_CACHE_MAP.put(url, names);
        LOGGER.info("[GetFileChannel]记录uuid:{}", player.getUuidAsString());
    }

    /**
     * 客户端接收 无文件 处理
     *
     */
// IF fabric-1.21 || fabric-1.20.5
//    public static void clientGetFileChannelReceived(FileInfoChannelPacket packet) {
//        String data = packet.message();
// ELSE
    public static void clientGetFileChannelReceived(PacketByteBuf buf) {
        String data = buf.readString();
// END IF

        String url = data.substring(6);
        LOGGER.info(url);
        if (data.startsWith("null")) {
            LOGGER.info("[GetFileChannel-NULL]{}", url);
            AddImageError(url, ChatImageFrame.FrameError.FILE_NOT_FOUND);
        } else if (data.startsWith("true")) {
            loadFromServer(url);
        }
    }


    /**
     * 客户端接收 下载文件分块 处理
     *
     */
// IF fabric-1.21 || fabric-1.20.5
//    public static void clientDownloadFileChannelReceived(DownloadFileChannelPacket packet) {
//        String res = packet.message();
// ELSE
    public static void clientDownloadFileChannelReceived(PacketByteBuf buf) {
        String res = buf.readString();
// END IF
        ChatImageIndex title = gson.fromJson(res, ChatImageIndex.class);
        HashMap<Integer, ChatImageIndex> blocks = CLIENT_CACHE_MAP.containsKey(title.url) ? CLIENT_CACHE_MAP.get(title.url) : new HashMap<>();
        blocks.put(title.index, title);
        CLIENT_CACHE_MAP.put(title.url, blocks);
        LOGGER.info("[DownloadFile({}/{})]{}", title.index, title.total, title.url);
        if (blocks.size() == title.total) {
            LOGGER.info(String.valueOf(blocks));
            mergeFileBlocks(title.url, blocks);
            LOGGER.info("[DownloadFileChannel-Merge]{}", title.url);
        }
    }
}