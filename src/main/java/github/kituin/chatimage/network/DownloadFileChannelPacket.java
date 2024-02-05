package github.kituin.chatimage.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

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
    public static boolean clientHandle(DownloadFileChannelPacket packet,CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(() -> clientDownloadFileChannelReceived(packet.message));
        return true;
    }


}
