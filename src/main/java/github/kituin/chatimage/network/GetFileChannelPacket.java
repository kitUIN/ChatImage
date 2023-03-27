package github.kituin.chatimage.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static github.kituin.chatimage.network.ChatImagePacket.clientGetFileChannelReceived;
import static github.kituin.chatimage.network.ChatImagePacket.serverGetFileChannelReceived;


public class GetFileChannelPacket {

    private final String message;

    public GetFileChannelPacket(FriendlyByteBuf buffer) {
        message = buffer.readUtf();
    }

    public GetFileChannelPacket(String message) {
        this.message = message;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.message);
    }

    /**
     * 服务端接收 客户端试图获取图片文件 的处理
     */
    public boolean serverHandle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> serverGetFileChannelReceived(ctx.getSender(), this.message));
        return true;
    }

    public boolean clientHandle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> clientGetFileChannelReceived(this.message));
        return true;
    }
}
