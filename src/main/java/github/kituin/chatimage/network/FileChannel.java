package github.kituin.chatimage.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;

import static github.kituin.chatimage.ChatImage.MOD_ID;
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
        SimpleChannel net = ChannelBuilder
                .named(FILE_CHANNEL)
                .networkProtocolVersion(1)
                .acceptedVersions((s, v) -> v == 1)
                .clientAcceptedVersions((s, v) -> true)
                .serverAcceptedVersions((s, v) -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(FileChannelPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(FileChannelPacket::new)
                .encoder(FileChannelPacket::toBytes)
                .consumerNetworkThread(FileChannelPacket::serverHandle)
                .add();


    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.send(message, PacketDistributor.SERVER.noArg());
    }
}
