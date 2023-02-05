package github.kituin.chatimage.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import static github.kituin.chatimage.Chatimage.MOD_ID;

/**
 * 客户端尝试获取图片请求通道
 */
public class GetFileCannel {
    public static ResourceLocation GET_FILE_CANNEL = new ResourceLocation(MOD_ID, "getfilecannel");
    private static SimpleChannel INSTANCE;
    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(GET_FILE_CANNEL)
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(GetFileCannelPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(GetFileCannelPacket::new)
                .encoder(GetFileCannelPacket::toBytes)
                .consumerNetworkThread(GetFileCannelPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

}
