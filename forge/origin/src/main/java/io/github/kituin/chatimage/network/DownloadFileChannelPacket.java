package io.github.kituin.chatimage.network;
// IF forge-1.16.5
//import net.minecraft.network.PacketBuffer;
// ELSE
//import net.minecraft.network.FriendlyByteBuf;
// END IF
// IF forge-1.16.5
//import net.minecraftforge.fml.network.NetworkEvent;
//import java.util.function.Supplier;
// ELSE IF <= forge-1.20
//import java.util.function.Supplier;
//import net.minecraftforge.network.NetworkEvent;
// ELSE
//import net.minecraftforge.event.network.CustomPayloadEvent;
// END IF

import static io.github.kituin.chatimage.network.ChatImagePacket.clientDownloadFileChannelReceived;


public class DownloadFileChannelPacket extends BChannelPacket {
    public DownloadFileChannelPacket(String message) {
        super(message);
    }
// IF forge-1.16.5
//    public DownloadFileChannelPacket(PacketBuffer buffer) {
//        super(buffer);
//    }
// ELSE
//    public DownloadFileChannelPacket(FriendlyByteBuf buffer) {
//        super(buffer);
//    }
// END IF
// IF <= forge-1.20
//    public boolean clientHandle(Supplier<NetworkEvent.Context> supplier) {
//        NetworkEvent.Context ctx = supplier.get();
//        ctx.enqueueWork(() -> clientDownloadFileChannelReceived(this.message));
// ELSE
//    public static boolean clientHandle(DownloadFileChannelPacket packet, CustomPayloadEvent.Context ctx) {
//      ctx.enqueueWork(() -> clientDownloadFileChannelReceived(packet.message));
// END IF
        return true;
    }
}