package io.github.kituin.chatimage.network;


// IF <= forge-1.20
//import java.util.function.Supplier;
// ELSE
//import net.minecraftforge.event.network.CustomPayloadEvent;
// END IF

import static io.github.kituin.chatimage.network.ChatImagePacket.*;

public class FileInfoChannelPacket extends BChannelPacket {
    public FileInfoChannelPacket(String message) {
        super(message);
    }
    public FileInfoChannelPacket(#FriendlyByteBuf# buffer) {
        super(buffer);
    }

    // IF <= forge-1.20
//    public boolean serverHandle(Supplier<#NetworkEvent#.Context> supplier) {
//        #NetworkEvent#.Context ctx = supplier.get();
//        ctx.enqueueWork(() -> serverFileInfoChannelReceived(ctx.getSender(), this.message));
// ELSE
//    public static boolean serverHandle(FileInfoChannelPacket packet, CustomPayloadEvent.Context ctx) {
//        ctx.enqueueWork(() -> serverFileInfoChannelReceived(ctx.getSender(), packet.message));
// END IF
        return true;
    }
// IF <= forge-1.20
//    public boolean clientHandle(Supplier<#NetworkEvent#.Context> supplier) {
//        #NetworkEvent#.Context ctx = supplier.get();
//        ctx.enqueueWork(() -> clientFileInfoChannelReceived(this.message));
// ELSE
//    public static boolean clientHandle(FileInfoChannelPacket packet, CustomPayloadEvent.Context ctx) {
//        ctx.enqueueWork(() -> clientFileInfoChannelReceived(packet.message));
// END IF
        return true;
    }
}