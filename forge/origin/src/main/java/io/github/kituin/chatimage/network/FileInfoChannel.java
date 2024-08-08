package io.github.kituin.chatimage.network;

import io.github.kituin.chatimage.ChatImage;
// IF forge-1.16.5
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.fml.network.NetworkDirection;
//import net.minecraftforge.fml.network.simple.SimpleChannel;
// ELSE
//import net.minecraft.resources.ResourceLocation;
//import net.minecraftforge.network.NetworkDirection;
//import net.minecraftforge.network.simple.SimpleChannel;
// END IF
// IF forge-1.16.5
//import net.minecraftforge.fml.network.NetworkRegistry.ChannelBuilder;
// ELSE IF <= forge-1.20
//import net.minecraftforge.network.NetworkRegistry.ChannelBuilder;
// ELSE
//import net.minecraftforge.network.ChannelBuilder;
//import net.minecraftforge.network.PacketDistributor;
// END IF

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
// IF <= forge-1.20
//                .networkProtocolVersion(() -> VERSION)
//                .clientAcceptedVersions(s -> true)
//                .serverAcceptedVersions(s -> true)
// ELSE
//                .networkProtocolVersion(1)
//                .acceptedVersions((s, v) -> v == 1)
//                .clientAcceptedVersions((s, v) -> true)
//                .serverAcceptedVersions((s, v) -> true)
// END IF
                .simpleChannel();

        INSTANCE.messageBuilder(FileInfoChannelPacket.class, nextID(), NetworkDirection.PLAY_TO_SERVER)
                .encoder(FileInfoChannelPacket::toBytes)
                .decoder(FileInfoChannelPacket::new)
// IF <= forge-1.20
//                .consumer(FileInfoChannelPacket::serverHandle)
// ELSE
//                .consumerNetworkThread(FileInfoChannelPacket::serverHandle)
// END IF
                .add();
        
    }
    public static <MSG> void sendToServer(MSG message) {
// IF <= forge-1.20
//            INSTANCE.sendToServer(message);
// ELSE
//        INSTANCE.send(message,PacketDistributor.SERVER.noArg());
// END IF
    }

}