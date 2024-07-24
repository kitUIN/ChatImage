package io.github.kituin.chatimage.network;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.NetworkRegistry;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.PlayNetworkDirection;
import net.neoforged.neoforge.network.simple.SimpleChannel;

import static io.github.kituin.chatimage.ChatImage.MOD_ID;

/**
 * 客户端发送文件分块到服务器通道
 */
public class FileChannel {
    /**
     * 客户端发送文件分块到服务器通道(Map)
     */
    public static ResourceLocation FILE_CHANNEL = new ResourceLocation(MOD_ID, "file_channel");
    private static SimpleChannel INSTANCE;

    private static int packetId = 1;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(FILE_CHANNEL)
                .networkProtocolVersion(() -> "1")
                .clientAcceptedVersions((v) -> true)
                .serverAcceptedVersions((v) -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(FileChannelPacket.class, id(), PlayNetworkDirection.PLAY_TO_SERVER)
                .decoder(FileChannelPacket::new)
                .encoder(FileChannelPacket::toBytes)
                .consumerNetworkThread(FileChannelPacket::serverHandle)
                .add();


    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.send(PacketDistributor.SERVER.noArg(),message);
    }
}
