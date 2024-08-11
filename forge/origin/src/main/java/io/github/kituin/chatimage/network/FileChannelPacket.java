package io.github.kituin.chatimage.network;
// IF <= forge-1.20
//import java.util.function.Supplier;
// ELSE
//import net.minecraftforge.event.network.CustomPayloadEvent;
// END IF

import static io.github.kituin.chatimage.network.ChatImagePacket.serverFileChannelReceived;


public class FileChannelPacket extends BChannelPacket{
    public FileChannelPacket(String message) {
        super(message);
    }
    public FileChannelPacket(#FriendlyByteBuf# buffer) {
        super(buffer);
    }
// IF <= forge-1.20
//    public boolean serverHandle(Supplier<#NetworkEvent#.Context> supplier) {
//        #NetworkEvent#.Context ctx = supplier.get();
//        ctx.enqueueWork(() -> serverFileChannelReceived(ctx.getSender(), this.message));
// ELSE
//    public static boolean serverHandle(FileChannelPacket packet, CustomPayloadEvent.Context ctx) {
//      ctx.enqueueWork(() -> serverFileChannelReceived(ctx.getSender(), packet.message));
// END IF
        return true;
    }
}