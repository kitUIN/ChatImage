package io.github.kituin.chatimage.network;

// IF forge-1.16.5
//import net.minecraft.network.PacketBuffer;
// ELSE
//import net.minecraft.network.FriendlyByteBuf;
// END IF
public class BChannelPacket {
    public String message;

    public BChannelPacket(String message) {
        this.message = message;
    }

// IF forge-1.16.5
//    public BChannelPacket(PacketBuffer buffer) {
//        this.message = buffer.readUtf();
//    }
//    public void toBytes(PacketBuffer buffer) {
//        buffer.writeUtf(this.message);
//    }
// ELSE
//    public BChannelPacket(FriendlyByteBuf buffer) {
//        this.message = buffer.readUtf();
//    }
//    public void toBytes(FriendlyByteBuf buffer) {
//        buffer.writeUtf(this.message);
//    }
// END IF
}