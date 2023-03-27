package github.kituin.chatimage.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.Map;
import java.util.function.Supplier;

import static github.kituin.chatimage.network.ChatImagePacket.clientDownloadFileChannelReceived;

public class DownloadFileChannelPacket {

    private Map<String, byte[]> message;

    public DownloadFileChannelPacket(FriendlyByteBuf buffer) {
        message = buffer.readMap(FriendlyByteBuf::readUtf, FriendlyByteBuf::readByteArray);
    }

    public DownloadFileChannelPacket(Map<String, byte[]> message) {
        this.message = message;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeMap(this.message, FriendlyByteBuf::writeUtf, FriendlyByteBuf::writeByteArray);
    }

    public boolean clientHandle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> clientDownloadFileChannelReceived(this.message));
        return true;
    }


}
