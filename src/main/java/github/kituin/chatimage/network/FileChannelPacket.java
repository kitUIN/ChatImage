package github.kituin.chatimage.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static github.kituin.chatimage.network.ChatImagePacket.serverFileChannelReceived;
import static io.github.kituin.ChatImageCode.NetworkHelper.MAX_STRING;

public class FileChannelPacket {


    private final String message;

    private static final Logger LOGGER = LogManager.getLogger();
    public FileChannelPacket(FriendlyByteBuf buffer) {
        message = buffer.readUtf(MAX_STRING);
    }

    public FileChannelPacket(String message) {
        this.message = message;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.message,MAX_STRING);
    }

    /**
     * 服务端接收 图片文件分块 的处理
     */
    public static boolean serverHandle(FileChannelPacket packet, CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(() -> serverFileChannelReceived(ctx.getSender(), packet.message));
        return true;
    }

}
