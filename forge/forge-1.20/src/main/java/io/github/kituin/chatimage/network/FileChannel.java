package io.github.kituin.chatimage.network;

import io.github.kituin.chatimage.ChatImage;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

/**
 * 客户端发送文件分块到服务器通道
 */
public class FileChannel {
    /**
     * 客户端发送文件分块到服务器通道(Map)
     */
    public static ResourceLocation FILE_CHANNEL = new ResourceLocation(ChatImage.MOD_ID, "file_channel");
    private static SimpleChannel INSTANCE;

    private static int packetId = 1;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(FILE_CHANNEL)
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(FileChannelPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(FileChannelPacket::new)
                .encoder(FileChannelPacket::toBytes)
                .consumerNetworkThread(FileChannelPacket::serverHandle)
                .add();


    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }
}
