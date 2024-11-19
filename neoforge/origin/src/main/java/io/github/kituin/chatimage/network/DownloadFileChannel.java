// ONLY <= neoforge-1.20.3
package io.github.kituin.chatimage.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.NetworkRegistry;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.PlayNetworkDirection;
import net.neoforged.neoforge.network.simple.SimpleChannel;

import static io.github.kituin.chatimage.ChatImage.MOD_ID;

/**
 * 发送文件分块到客户端通道
 */
public class DownloadFileChannel {
    /**
     * 发送文件分块到客户端通道(Map)
     */
    public static ResourceLocation DOWNLOAD_FILE_CHANNEL = new ResourceLocation(MOD_ID, "download_file_channel");

    public static SimpleChannel INSTANCE;
    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(DOWNLOAD_FILE_CHANNEL)
                .networkProtocolVersion(() -> "1")
                .clientAcceptedVersions((v) -> true)
                .serverAcceptedVersions((v) -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(DownloadFileChannelPacket.class, id(), PlayNetworkDirection.PLAY_TO_CLIENT)
                .decoder(DownloadFileChannelPacket::new)
                .encoder(DownloadFileChannelPacket::toBytes)
                .consumerNetworkThread(DownloadFileChannelPacket::clientHandle)
                .add();
    }


    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}