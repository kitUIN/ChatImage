package io.github.kituin.chatimage.network;

import net.minecraft.network.FriendlyByteBuf;


// IF <= neoforge-1.20.3
//
//import io.github.kituin.ChatImageCode.ChatImageFrame;
//import net.minecraft.server.level.ServerPlayer;
//import net.neoforged.neoforge.network.NetworkEvent;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//import static io.github.kituin.ChatImageCode.ClientStorage.AddImageError;
//
//import static io.github.kituin.chatimage.network.ChatImagePacket.loadFromServer;
//import static io.github.kituin.chatimage.network.ChatImagePacket.serverFileInfoChannelReceived;
//
//public class FileInfoChannelPacket {
//    public final String message;
//    private static final Logger LOGGER = LogManager.getLogger();
//
//    public FileInfoChannelPacket(FriendlyByteBuf buffer) {
//        message = buffer.readUtf();
//    }
//
//    public FileInfoChannelPacket(String message) {
//        this.message = message;
//    }
//
//    public void toBytes(FriendlyByteBuf buf) {
//        buf.writeUtf(this.message);
//    }
//
//    public static void handler(FileInfoChannelPacket packet, NetworkEvent.Context ctx) {
//        ctx.enqueueWork(() -> {
//            ServerPlayer player = ctx.getSender();
//            serverFileInfoChannelReceived(player, packet.message);
//        });
//        ctx.setPacketHandled(true);
//    }
//
//    public static void clientHandle(FileInfoChannelPacket packet, NetworkEvent.Context ctx) {
//        ctx.enqueueWork(() -> {
//            String data = packet.message;
//            String url = data.substring(6);
//            LOGGER.info(url);
//            if (data.startsWith("null")) {
//                LOGGER.info("[GetFileChannel-NULL]" + url);
//                AddImageError(url, ChatImageFrame.FrameError.FILE_NOT_FOUND);
//            } else if (data.startsWith("true")) {
//                LOGGER.info("[GetFileChannel-Retry]" + url);
//                loadFromServer(url);
//            }
//        });
//        ctx.setPacketHandled(true);
//    }
//}
// ELSE
//
//import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
//import net.minecraft.resources.ResourceLocation;
//import org.jetbrains.annotations.NotNull;
// IF >= neoforge-1.20.5
//import io.netty.buffer.ByteBuf;
//import net.minecraft.network.codec.ByteBufCodecs;
// END IF
//
//import static io.github.kituin.chatimage.ChatImage.MOD_ID;
//
//
//public record FileInfoChannelPacket(String message) implements CustomPacketPayload {
//    public FileInfoChannelPacket(FriendlyByteBuf buffer) {
//        this(buffer.readUtf());
//    }
//    public static ResourceLocation FILE_INFO =  #ResourceLocationfromNamespaceAndPath#(MOD_ID, "file_info");
// IF >= neoforge-1.20.5
//    public static final CustomPacketPayload.Type<FileInfoChannelPacket> TYPE =
//            new CustomPacketPayload.Type<>(FILE_INFO);
//    public Type<? extends CustomPacketPayload> type() {
//        return TYPE;
//    }
//    public static final net.minecraft.network.codec.StreamCodec<ByteBuf, FileInfoChannelPacket> CODEC =
//            net.minecraft.network.codec.StreamCodec.composite(
//                    ByteBufCodecs.STRING_UTF8,
//                    FileInfoChannelPacket::message,
//                    FileInfoChannelPacket::new
//            );
// ELSE
//
//     public void write(final FriendlyByteBuf buffer) {
//         buffer.writeUtf(message());
//     }
//
//     @Override
//     public @NotNull ResourceLocation id() {
//         return FILE_INFO;
//     }
// END IF
//}
//
// END IF