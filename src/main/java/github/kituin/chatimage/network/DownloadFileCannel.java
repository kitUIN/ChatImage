package github.kituin.chatimage.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import static github.kituin.chatimage.Chatimage.MOD_ID;

/**
 * 发送文件分块到客户端通道
 */
public class DownloadFileCannel {

    public static ResourceLocation DOWNLOAD_FILE_CANNEL = new ResourceLocation(MOD_ID, "downloadfilecannel");

    public static SimpleChannel INSTANCE;
    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(DOWNLOAD_FILE_CANNEL)
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(DownloadFileCannelPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(DownloadFileCannelPacket::new)
                .encoder(DownloadFileCannelPacket::toBytes)
                .consumerNetworkThread(DownloadFileCannelPacket::handle)
                .add();
    }


    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
