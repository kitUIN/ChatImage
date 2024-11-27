package io.github.kituin.chatimage.network;

import net.minecraft.network.FriendlyByteBuf;

// IF <= neoforge-1.20.3
//
// import net.neoforged.neoforge.network.NetworkEvent;
// import org.apache.logging.log4j.LogManager;
// import org.apache.logging.log4j.Logger;
//
//
// import static io.github.kituin.chatimage.network.ChatImagePacket.serverFileChannelReceived;
// public class FileChannelPacket {
//
//     private final String message;
//
//     private static final Logger LOGGER = LogManager.getLogger();
//     public FileChannelPacket(FriendlyByteBuf buffer) {
//         message = buffer.readUtf();
//     }
//
//     public FileChannelPacket(String message) {
//         this.message = message;
//     }
//
//     public void toBytes(FriendlyByteBuf buf) {
//         buf.writeUtf(this.message);
//     }
//
//     /**
//      * 服务端接收 图片文件分块 的处理
//      */
//     public static boolean serverHandle(FileChannelPacket packet, NetworkEvent.Context ctx) {
//         ctx.enqueueWork(() -> serverFileChannelReceived(ctx.getSender(), packet.message));
//         return true;
//     }
//
// }
// ELSE
//import io.netty.buffer.ByteBuf;
//import net.minecraft.network.codec.ByteBufCodecs;
//import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
//import net.minecraft.resources.ResourceLocation;
//import org.jetbrains.annotations.NotNull;
//
//import static io.github.kituin.chatimage.ChatImage.MOD_ID;
///**
// * 客户端发送文件分块到服务器通道(Map)
// */
//public record FileChannelPacket(String message) implements CustomPacketPayload {
//
//    public FileChannelPacket(FriendlyByteBuf buffer) {
//        this(buffer.readUtf());
//    }
// IF >= neoforge-1.21.0
//   public static ResourceLocation FILE_CHANNEL =  ResourceLocation.fromNamespaceAndPath(MOD_ID, "file_channel");
//   public static final CustomPacketPayload.Type<FileChannelPacket> TYPE =
//           new CustomPacketPayload.Type<>(FILE_CHANNEL);
//   public Type<? extends CustomPacketPayload> type() {
//       return TYPE;
//   }
//    public static final net.minecraft.network.codec.StreamCodec<ByteBuf, FileChannelPacket> CODEC =
//            net.minecraft.network.codec.StreamCodec.composite(
//                    ByteBufCodecs.STRING_UTF8,
//                    FileChannelPacket::message,
//                    FileChannelPacket::new
//            );
// ELSE
//     public static ResourceLocation FILE_CHANNEL = new ResourceLocation(MOD_ID, "file_channel");
//     public void write(final FriendlyByteBuf buffer) {
//         buffer.writeUtf(message());
//     }
//
//     @Override
//     public @NotNull ResourceLocation id() {
//         return FILE_CHANNEL;
//     }
// END IF
//
//}
// END IF