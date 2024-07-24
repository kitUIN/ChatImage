package io.github.kituin.chatimage.network;

import io.github.kituin.chatimage.ChatImage;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

/**
 * 发送文件分块到客户端通道
 */
public class DownloadFileChannel {
    /**
     * 发送文件分块到客户端通道(Map)
     */
    public static ResourceLocation DOWNLOAD_FILE_CHANNEL = new ResourceLocation(ChatImage.MOD_ID, "download_file_channel");

    public static SimpleChannel INSTANCE;
    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(DOWNLOAD_FILE_CHANNEL)
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(DownloadFileChannelPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(DownloadFileChannelPacket::new)
                .encoder(DownloadFileChannelPacket::toBytes)
                .consumer(DownloadFileChannelPacket::clientHandle)
                .add();
    }


    public static <MSG> void sendToPlayer(MSG message, ServerPlayerEntity player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
