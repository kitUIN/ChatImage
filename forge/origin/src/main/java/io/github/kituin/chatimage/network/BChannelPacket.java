package io.github.kituin.chatimage.network;

public class BChannelPacket {
    public String message;

    public BChannelPacket(String message) {
        this.message = message;
    }
    public BChannelPacket(#FriendlyByteBuf# buffer) {
        this.message = buffer.readUtf();
    }
    public void toBytes(#FriendlyByteBuf# buffer) {
        buffer.writeUtf(this.message);
    }
}