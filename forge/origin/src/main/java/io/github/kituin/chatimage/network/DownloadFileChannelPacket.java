package io.github.kituin.chatimage.network;

import net.minecraft.network.FriendlyByteBuf;
// IF forge-1.16.5
//import net.minecraftforge.fml.network.NetworkEvent;
//import java.util.function.Supplier;
// ELSE IF forge-1.18.2
//import java.util.function.Supplier;
//import net.minecraftforge.network.NetworkEvent;
// ELSE
//import net.minecraftforge.event.network.CustomPayloadEvent;
// END IF

import static io.github.kituin.chatimage.network.ChatImagePacket.clientDownloadFileChannelReceived;


public class DownloadFileChannelPacket {

    public String message;

    public DownloadFileChannelPacket(FriendlyByteBuf buffer) {
        message = buffer.readUtf();
    }

    public DownloadFileChannelPacket(String message) {
        this.message = message;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.message);
    }
// IF <= forge-1.18.2
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