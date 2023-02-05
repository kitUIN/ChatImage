package github.kituin.chatimage.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.function.Supplier;

public class DownloadFileCannelPacket {

    private Map<String, byte[]> message;
    private static final Logger LOGGER = LogManager.getLogger();

    public DownloadFileCannelPacket(FriendlyByteBuf buffer) {
        message = buffer.readMap(FriendlyByteBuf::readUtf, FriendlyByteBuf::readByteArray);
    }

    public DownloadFileCannelPacket(Map<String, byte[]> message) {
        this.message = message;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeMap(this.message, FriendlyByteBuf::writeUtf, FriendlyByteBuf::writeByteArray);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {

        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {

            ServerPlayer player = ctx.getSender();
        });
        return true;
    }


}
