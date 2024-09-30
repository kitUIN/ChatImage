package io.github.kituin.chatimage.network;

import static io.github.kituin.chatimage.ChatImage.MOD_ID;
/**
 * 发送文件分块到客户端通道
 */
public class DownloadFileChannel {

    private static #SimpleChannel# INSTANCE;
    public static final String VERSION = "1.0";
    private static int ID = 0;
    
    public static int nextID() {
        return ID++;
    }
    
    public static void register() {
        INSTANCE = #ChannelBuilder#
                .named(new #ResourceLocation#(MOD_ID, "download_file_channel"))
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

        INSTANCE.messageBuilder(DownloadFileChannelPacket.class, nextID(), #NetworkDirection#.PLAY_TO_CLIENT)
                .decoder(DownloadFileChannelPacket::new)
                .encoder(DownloadFileChannelPacket::toBytes)
// IF < forge-1.19
//                .consumer(DownloadFileChannelPacket::clientHandle)
// ELSE
//                .consumerNetworkThread(DownloadFileChannelPacket::clientHandle)
// END IF
                .add();
    }
    
    public static <MSG> void sendToPlayer(MSG message, #ServerPlayer# player) {
// IF <= forge-1.20
//        INSTANCE.send(#PacketDistributor#.PLAYER.with(() -> player), message);
// ELSE
//                INSTANCE.send(message,#PacketDistributor#.PLAYER.with(player));
// END IF
    }
}