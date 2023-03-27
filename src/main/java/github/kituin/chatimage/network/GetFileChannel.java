package github.kituin.chatimage.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import static github.kituin.chatimage.ChatImage.MOD_ID;

/**
 * 客户端尝试获取图片请求通道
 */
public class GetFileChannel {
    /**
     * 尝试获取图片请求通道(String)
     */
    public static ResourceLocation GET_FILE_CHANNEL = new ResourceLocation(MOD_ID, "get_file_channel");
    private static SimpleChannel INSTANCE;
    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(GET_FILE_CHANNEL)
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(GetFileChannelPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(GetFileChannelPacket::new)
                .encoder(GetFileChannelPacket::toBytes)
                .consumerNetworkThread(GetFileChannelPacket::serverHandle)
                .add();
        net.messageBuilder(GetFileChannelPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(GetFileChannelPacket::new)
                .encoder(GetFileChannelPacket::toBytes)
                .consumerNetworkThread(GetFileChannelPacket::clientHandle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }
    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
