package io.github.kituin.chatimage.network;

import static io.github.kituin.chatimage.ChatImage.MOD_ID;

/**
 * 客户端发送文件分块到服务器通道
 */
public class FileChannel {
    /**
     * 客户端发送文件分块到服务器通道(Map)
     */
    private static #SimpleChannel# INSTANCE;
    public static final String VERSION = "1.0";
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }

    public static void register() {
        INSTANCE = #ChannelBuilder#
                .named(new #ResourceLocation#(MOD_ID, "file_channel"))
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

        INSTANCE.messageBuilder(FileChannelPacket.class, nextID(), #NetworkDirection#.PLAY_TO_SERVER)
                .encoder(FileChannelPacket::toBytes)
                .decoder(FileChannelPacket::new)
// IF <= forge-1.20
//                .consumer(FileChannelPacket::serverHandle)
// ELSE
//                .consumerNetworkThread(FileChannelPacket::serverHandle)
// END IF
                .add();

    }

    public static <MSG> void sendToServer(MSG message) {
// IF <= forge-1.20
//        INSTANCE.sendToServer(message);
// ELSE
//        INSTANCE.send(message, PacketDistributor.SERVER.noArg());
// END IF
    }
}