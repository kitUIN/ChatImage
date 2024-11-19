package io.github.kituin.chatimage.network;

import net.minecraft.network.FriendlyByteBuf;


// IF <= neoforge-1.20.3
// import net.neoforged.neoforge.network.NetworkEvent;
//
// import static io.github.kituin.chatimage.network.ChatImagePacket.clientDownloadFileChannelReceived;
// public class DownloadFileChannelPacket {
//
//     public String message;
//
//     public DownloadFileChannelPacket(FriendlyByteBuf buffer) {
//         message = buffer.readUtf();
//     }
//
//     public DownloadFileChannelPacket(String message) {
//         this.message = message;
//     }
//
//     public void toBytes(FriendlyByteBuf buf) {
//         buf.writeUtf(this.message);
//     }
//     public static boolean clientHandle(DownloadFileChannelPacket packet, NetworkEvent.Context ctx) {
//         ctx.enqueueWork(() -> clientDownloadFileChannelReceived(packet.message));
//         return true;
//     }
// }
// ELSE
//import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
//import net.minecraft.resources.ResourceLocation;
//import org.jetbrains.annotations.NotNull;
//
//import static io.github.kituin.chatimage.ChatImage.MOD_ID;
//
//public record DownloadFileChannelPacket(String message) implements CustomPacketPayload {
//    /**
//     * 发送文件分块到客户端通道(Map)
//     */
//    public static ResourceLocation DOWNLOAD_FILE_CHANNEL = new ResourceLocation(MOD_ID, "download_file_channel");
//
//    public DownloadFileChannelPacket(FriendlyByteBuf buffer) {
//        this(buffer.readUtf());
//    }
//
//    @Override
//    public void write(final FriendlyByteBuf buffer) {
//        buffer.writeUtf(message());
//    }
//
//    @Override
//    public @NotNull ResourceLocation id() {
//        return DOWNLOAD_FILE_CHANNEL;
//    }
//
//}
// END IF