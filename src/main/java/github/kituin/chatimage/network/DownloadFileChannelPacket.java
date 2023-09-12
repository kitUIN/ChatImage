package github.kituin.chatimage.network;


import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

import static github.kituin.chatimage.network.ChatImagePacket.clientDownloadFileChannelReceived;

public class DownloadFileChannelPacket {

    private String message;

    public DownloadFileChannelPacket(PacketBuffer buffer) {
        message = buffer.readString();
    }

    public DownloadFileChannelPacket(String message) {
        this.message = message;
    }

    public void toBytes(PacketBuffer  buf) {
        buf.writeString(this.message);
    }
    public boolean clientHandle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> clientDownloadFileChannelReceived(this.message));
        return true;
    }


}
