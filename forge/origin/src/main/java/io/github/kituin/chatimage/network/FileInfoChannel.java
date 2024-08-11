package io.github.kituin.chatimage.network;

import static io.github.kituin.chatimage.ChatImage.MOD_ID;

public class FileInfoChannel {
    public static #SimpleChannel# INSTANCE;
    public static final String VERSION = "1.0";
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }

    public static void registerMessage() {
        INSTANCE = #ChannelBuilder#
                .named(new #ResourceLocation#(MOD_ID, "first_networking"))
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

        INSTANCE.messageBuilder(FileInfoChannelPacket.class, nextID(), #NetworkDirection#.PLAY_TO_SERVER)
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