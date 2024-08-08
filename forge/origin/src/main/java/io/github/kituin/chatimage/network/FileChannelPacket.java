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
//import net.minecraft.network.FriendlyByteBuf;
// ELSE
//import net.minecraftforge.event.network.CustomPayloadEvent;
//import net.minecraft.network.FriendlyByteBuf;
// END IF
import net.minecraft.server.level.ServerPlayer;

import static io.github.kituin.chatimage.network.ChatImagePacket.serverFileChannelReceived;


public class FileChannelPacket extends BChannelPacket{
    public FileChannelPacket(String message) {
        super(message);
    }
// IF forge-1.16.5
//    public FileChannelPacket(PacketBuffer buffer) {
//        super(buffer);
//    }
// ELSE
//    public FileChannelPacket(FriendlyByteBuf buffer) {
//        super(buffer);
//    }
// END IF
// IF <= forge-1.20
//    public boolean serverHandle(Supplier<NetworkEvent.Context> supplier) {
//        NetworkEvent.Context ctx = supplier.get();
//        ctx.enqueueWork(() -> serverFileChannelReceived((ServerPlayer)ctx.getSender(), this.message));
// ELSE
//    public static boolean serverHandle(FileChannelPacket packet, CustomPayloadEvent.Context ctx) {
//      ctx.enqueueWork(() -> serverFileChannelReceived(ctx.getSender(), packet.message));
// END IF
        return true;
    }
}