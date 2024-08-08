package io.github.kituin.chatimage.network;

// IF forge-1.16.5
//import net.minecraftforge.fml.network.NetworkEvent;
//import java.util.function.Supplier;
// ELSE IF forge-1.18.2
//import java.util.function.Supplier;
//import net.minecraftforge.network.NetworkEvent;
// ELSE
//import net.minecraftforge.event.network.CustomPayloadEvent;
// END IF
import net.minecraft.network.FriendlyByteBuf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static io.github.kituin.chatimage.network.ChatImagePacket.serverFileChannelReceived;


public class FileChannelPacket {


    private final String message;

    private static final Logger LOGGER = LogManager.getLogger();

    public FileChannelPacket(FriendlyByteBuf buffer) {
        message = buffer.readUtf();
    }

    public FileChannelPacket(String message) {
        this.message = message;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.message);
    }


// IF <= forge-1.18.2
//    public boolean serverHandle(Supplier<NetworkEvent.Context> supplier) {
//        NetworkEvent.Context ctx = supplier.get();
//        ctx.enqueueWork(() -> serverFileChannelReceived(ctx.getSender(), this.message));
// ELSE
//    public static boolean serverHandle(FileChannelPacket packet, CustomPayloadEvent.Context ctx) {
//      ctx.enqueueWork(() -> serverFileChannelReceived(ctx.getSender(), packet.message));
// END IF
        return true;
    }
}