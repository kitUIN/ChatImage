package github.kituin.chatimage.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;

import static github.kituin.chatimage.ChatImage.MOD_ID;

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
        SimpleChannel net = ChannelBuilder
                .named(DOWNLOAD_FILE_CHANNEL)
                .networkProtocolVersion(1)
                .acceptedVersions((s, v) -> v == 1)
                .clientAcceptedVersions((s, v) -> true)
                .serverAcceptedVersions((s, v) -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(DownloadFileChannelPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(DownloadFileChannelPacket::new)
                .encoder(DownloadFileChannelPacket::toBytes)
                .consumerNetworkThread(DownloadFileChannelPacket::clientHandle)
                .add();
    }


    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(message,PacketDistributor.PLAYER.with(player));
    }
}