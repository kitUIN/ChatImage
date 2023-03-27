package github.kituin.chatimage;


import github.kituin.chatimage.network.ChatImagePacket;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @author kitUIN
 */
public class ChatImage implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {
        ServerPlayNetworking.registerGlobalReceiver(ChatImagePacket.FILE_CHANNEL, (server, player, handler, buf, responseSender) -> ChatImagePacket.serverFileChannelReceived(server, buf));
        ServerPlayNetworking.registerGlobalReceiver(ChatImagePacket.GET_FILE_CHANNEL, (server, player, handler, buf, responseSender) -> ChatImagePacket.serverGetFileChannelReceived(player, buf));
    }
}
