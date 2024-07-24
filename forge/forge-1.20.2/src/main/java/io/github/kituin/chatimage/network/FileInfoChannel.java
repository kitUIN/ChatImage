package io.github.kituin.chatimage.network;

import io.github.kituin.chatimage.ChatImage;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;

public class FileInfoChannel {
    public static SimpleChannel INSTANCE;
    public static final String VERSION = "1.0";
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }

    public static void registerMessage() {
        INSTANCE = ChannelBuilder
                .named(new ResourceLocation(ChatImage.MOD_ID, "first_networking"))
                .networkProtocolVersion(1)
                .acceptedVersions((s, v) -> v == 1)
                .clientAcceptedVersions((s, v) -> true)
                .serverAcceptedVersions((s, v) -> true)
                .simpleChannel();

        INSTANCE.messageBuilder(FileInfoChannelPacket.class, nextID(), NetworkDirection.PLAY_TO_SERVER)
                .encoder(FileInfoChannelPacket::toBytes)
                .decoder(FileInfoChannelPacket::new)
                .consumerNetworkThread(FileInfoChannelPacket::handler)
                .add();

    }
    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.send(message,PacketDistributor.SERVER.noArg());
    }

}

