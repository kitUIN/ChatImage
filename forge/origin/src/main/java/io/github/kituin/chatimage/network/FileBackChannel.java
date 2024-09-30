package io.github.kituin.chatimage.network;

import static io.github.kituin.chatimage.ChatImage.MOD_ID;

public class FileBackChannel {
    private static #SimpleChannel# INSTANCE;
    public static final String VERSION = "1.0";
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }


    public static void register() {
        INSTANCE = #ChannelBuilder#
                .named(new #ResourceLocation#(MOD_ID, "file_back"))
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

        INSTANCE.messageBuilder(FileInfoChannelPacket.class, nextID(), #NetworkDirection#.PLAY_TO_CLIENT)
                .encoder(FileInfoChannelPacket::toBytes)
                .decoder(FileInfoChannelPacket::new)
// IF < forge-1.19
//                .consumer(FileInfoChannelPacket::clientHandle)
// ELSE
//                .consumerNetworkThread(FileInfoChannelPacket::clientHandle)
// END IF
                .add();
    }
    public static <MSG> void sendToPlayer(MSG message, #ServerPlayer# player) {

// IF <= forge-1.20
//        INSTANCE.send(#PacketDistributor#.PLAYER.with(() -> player), message);
// ELSE
//                INSTANCE.send(message, #PacketDistributor#.PLAYER.with(player));
// END IF
    }
}