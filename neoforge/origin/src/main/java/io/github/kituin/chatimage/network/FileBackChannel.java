// ONLY <= neoforge-1.20.3
package io.github.kituin.chatimage.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.NetworkRegistry;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.PlayNetworkDirection;
import net.neoforged.neoforge.network.simple.SimpleChannel;

import static io.github.kituin.chatimage.ChatImage.MOD_ID;

public class FileBackChannel {
    public static SimpleChannel INSTANCE;
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }

    public static void register() {
        INSTANCE = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(MOD_ID, "file_back"))
                .networkProtocolVersion(() -> "1")
                .clientAcceptedVersions((v) -> true)
                .serverAcceptedVersions((v) -> true)
                .simpleChannel();

        INSTANCE.messageBuilder(FileInfoChannelPacket.class, nextID(), PlayNetworkDirection.PLAY_TO_CLIENT)
                .encoder(FileInfoChannelPacket::toBytes)
                .decoder(FileInfoChannelPacket::new)
                .consumerNetworkThread(FileInfoChannelPacket::clientHandle)
                .add();
    }
    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(()->player),message);
    }
}