package github.kituin.chatimage.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.Map;
import java.util.function.Supplier;

import static github.kituin.chatimage.network.ChatImagePacket.clientDownloadFileChannelReceived;

public class DownloadFileChannelPacket {

    private String message;

    public DownloadFileChannelPacket(FriendlyByteBuf buffer) {
        message = buffer.readUtf();
    }

    public DownloadFileChannelPacket(String message) {
        this.message = message;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.message);
    }
    public boolean clientHandle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> clientDownloadFileChannelReceived(this.message));
        return true;
    }


}
