package github.kituin.chatimage.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.Map;
import java.util.function.Supplier;

import static github.kituin.chatimage.network.ChatImagePacket.serverFileChannelReceived;

public class FileChannelPacket {


    private final Map<String, byte[]> message;

    public FileChannelPacket(FriendlyByteBuf buffer) {
        message = buffer.readMap(FriendlyByteBuf::readUtf, FriendlyByteBuf::readByteArray);
    }

    public FileChannelPacket(Map<String, byte[]> message) {
        this.message = message;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeMap(this.message, FriendlyByteBuf::writeUtf, FriendlyByteBuf::writeByteArray);
    }

    /**
     * 服务端接收 图片文件分块 的处理
     */
    public boolean serverHandle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> serverFileChannelReceived(ctx.getSender(), this.message));
        return true;
    }

}
