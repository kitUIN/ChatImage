package github.kituin.chatimage.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import static github.kituin.chatimage.Chatimage.MOD_ID;
/**
 * 客户端发送文件分块到服务器通道
 */
public class FileCannel {

    public static ResourceLocation FILE_CANNEL = new ResourceLocation(MOD_ID, "filecannel");
    private static SimpleChannel INSTANCE;

    // Every packet needs a unique ID (unique for this channel)
    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(FILE_CANNEL)
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(FileCannelPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(FileCannelPacket::new)
                .encoder(FileCannelPacket::toBytes)
                .consumerNetworkThread(FileCannelPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

}
