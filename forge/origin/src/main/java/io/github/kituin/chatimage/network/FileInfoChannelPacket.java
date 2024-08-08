package io.github.kituin.chatimage.network;


import net.minecraft.network.PacketBuffer;
import net.minecraft.server.level.ServerPlayer;
// IF forge-1.16.5
//import net.minecraft.network.PacketBuffer;
// ELSE
//import net.minecraft.network.FriendlyByteBuf;
// END IF
// IF forge-1.16.5
//import net.minecraftforge.fml.network.NetworkEvent;
//import java.util.function.Supplier;
// ELSE IF  <= forge-1.20
//import java.util.function.Supplier;
//import net.minecraftforge.network.NetworkEvent;
//import net.minecraft.network.FriendlyByteBuf;
// ELSE
//import net.minecraftforge.event.network.CustomPayloadEvent;
//import net.minecraft.network.FriendlyByteBuf;
// END IF

import static io.github.kituin.chatimage.network.ChatImagePacket.*;

public class FileInfoChannelPacket extends BChannelPacket {
    public FileInfoChannelPacket(String message) {
        super(message);
    }
// IF forge-1.16.5
//    public FileInfoChannelPacket(PacketBuffer buffer) {
//        super(buffer);
//    }
// ELSE
//    public FileInfoChannelPacket(FriendlyByteBuf buffer) {
//        super(buffer);
//    }
// END IF

    // IF <= forge-1.20
//    public boolean serverHandle(Supplier<NetworkEvent.Context> supplier) {
//        NetworkEvent.Context ctx = supplier.get();
//        ctx.enqueueWork(() -> serverFileInfoChannelReceived((ServerPlayer)ctx.getSender(), this.message));
// ELSE
//    public static boolean serverHandle(FileInfoChannelPacket packet, CustomPayloadEvent.Context ctx) {
//        ctx.enqueueWork(() -> serverFileInfoChannelReceived(ctx.getSender(), packet.message));
// END IF
        return true;
    }
// IF <= forge-1.20
//    public boolean clientHandle(Supplier<NetworkEvent.Context> supplier) {
//        NetworkEvent.Context ctx = supplier.get();
//        ctx.enqueueWork(() -> clientFileInfoChannelReceived(this.message));
// ELSE
//    public static boolean clientHandle(FileInfoChannelPacket packet, CustomPayloadEvent.Context ctx) {
//        ctx.enqueueWork(() -> clientFileInfoChannelReceived(packet.message));
// END IF
        return true;
    }
}