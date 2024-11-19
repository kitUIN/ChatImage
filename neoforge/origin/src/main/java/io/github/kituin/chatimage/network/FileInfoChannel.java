// ONLY <= neoforge-1.20.3
package io.github.kituin.chatimage.network;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.NetworkRegistry;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.PlayNetworkDirection;
import net.neoforged.neoforge.network.simple.SimpleChannel;

import static io.github.kituin.chatimage.ChatImage.MOD_ID;

public class FileInfoChannel {
    public static SimpleChannel INSTANCE;
    public static final String VERSION = "1.0";
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }

    public static void registerMessage() {
        INSTANCE = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(MOD_ID, "first_networking"))
                .networkProtocolVersion(() -> "1")
                .clientAcceptedVersions((v) -> true)
                .serverAcceptedVersions((v) -> true)
                .simpleChannel();

        INSTANCE.messageBuilder(FileInfoChannelPacket.class, nextID(), PlayNetworkDirection.PLAY_TO_SERVER)
                .encoder(FileInfoChannelPacket::toBytes)
                .decoder(FileInfoChannelPacket::new)
                .consumerNetworkThread(FileInfoChannelPacket::handler)
                .add();

    }
    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.send(PacketDistributor.SERVER.noArg(),message);
    }

}