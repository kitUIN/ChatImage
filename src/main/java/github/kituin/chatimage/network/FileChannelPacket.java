package github.kituin.chatimage.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

import static github.kituin.chatimage.network.ChatImagePacket.serverFileChannelReceived;

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

    /**
     * 服务端接收 图片文件分块 的处理
     */
    public boolean serverHandle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> serverFileChannelReceived(ctx.getSender(), this.message));
        return true;
    }

}
