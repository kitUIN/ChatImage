package io.github.kituin.chatimage.network;

// IF <= forge-1.20
//import java.util.function.Supplier;
// ELSE
//import net.minecraftforge.event.network.CustomPayloadEvent;
// END IF

import static io.github.kituin.chatimage.network.ChatImagePacket.clientDownloadFileChannelReceived;


public class DownloadFileChannelPacket extends BChannelPacket {
    public DownloadFileChannelPacket(String message) {
        super(message);
    }
    public DownloadFileChannelPacket(#FriendlyByteBuf# buffer) {
        super(buffer);
    }
// IF <= forge-1.20
//    public boolean clientHandle(Supplier<#NetworkEvent#.Context> supplier) {
//        #NetworkEvent#.Context ctx = supplier.get();
//        ctx.enqueueWork(() -> clientDownloadFileChannelReceived(this.message));
// ELSE
//    public static boolean clientHandle(DownloadFileChannelPacket packet, CustomPayloadEvent.Context ctx) {
//      ctx.enqueueWork(() -> clientDownloadFileChannelReceived(packet.message));
// END IF
        return true;
    }
}