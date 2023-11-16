package github.kituin.chatimage.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import static github.kituin.chatimage.ChatImage.MOD_ID;

public class FileInfoChannel {
    public static SimpleChannel INSTANCE;
    public static final String VERSION = "1.0";
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }

    public static void registerMessage() {
        INSTANCE = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(MOD_ID, "first_networking"),
                () -> VERSION,
                (version) -> version.equals(VERSION),
                (version) -> version.equals(VERSION)
        );
        INSTANCE.messageBuilder(FileInfoChannelPacket.class, nextID(), NetworkDirection.PLAY_TO_SERVER)
                .encoder(FileInfoChannelPacket::toBytes)
                .decoder(FileInfoChannelPacket::new)
                .consumerNetworkThread(FileInfoChannelPacket::handler)
                .add();

    }
    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

}

