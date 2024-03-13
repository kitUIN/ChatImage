package github.kituin.chatimage.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static github.kituin.chatimage.network.ChatImagePacket.clientDownloadFileChannelReceived;
import static io.github.kituin.ChatImageCode.NetworkHelper.MAX_STRING;

public class DownloadFileChannelPacket {

    public String message;

    public DownloadFileChannelPacket(FriendlyByteBuf buffer) {
        message = buffer.readUtf(MAX_STRING);
    }

    public DownloadFileChannelPacket(String message) {
        this.message = message;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.message,MAX_STRING);
    }
    public boolean clientHandle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> clientDownloadFileChannelReceived(this.message));
        return true;
    }


}