package io.github.kituin.chatimage.network;

import net.minecraft.network.FriendlyByteBuf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// IF forge-1.16.5
//import net.minecraft.network.FriendlyByteBuf;
//import net.minecraftforge.fml.network.NetworkEvent;
//import java.util.function.Supplier;
// ELSE IF forge-1.18.2
//import java.util.function.Supplier;
//import net.minecraftforge.network.NetworkEvent;
// ELSE
//import net.minecraftforge.event.network.CustomPayloadEvent;
// END IF

import static io.github.kituin.chatimage.network.ChatImagePacket.*;

public class FileInfoChannelPacket {
    public final String message;
    private static final Logger LOGGER = LogManager.getLogger();

    public FileInfoChannelPacket(FriendlyByteBuf buffer) {
        message = buffer.readUtf();
    }

    public FileInfoChannelPacket(String message) {
        this.message = message;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.message);
    }
    // IF <= forge-1.18.2
//    public boolean serverHandle(Supplier<NetworkEvent.Context> supplier) {
//        NetworkEvent.Context ctx = supplier.get();
//        ctx.enqueueWork(() -> serverFileInfoChannelReceived(ctx.getSender(), this.message));
// ELSE
//    public static boolean serverHandle(FileInfoChannelPacket packet, CustomPayloadEvent.Context ctx) {
//        ctx.enqueueWork(() -> serverFileInfoChannelReceived(ctx.getSender(), packet.message));
// END IF
        return true;
    }
// IF <= forge-1.18.2
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