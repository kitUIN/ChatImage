package github.kituin.chatimage;

import com.mojang.logging.LogUtils;
import github.kituin.chatimage.integration.ChatImageLogger;
import github.kituin.chatimage.network.ChatImagePacket;
import io.github.kituin.ChatImageCode.ChatImageCodeInstance;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import org.slf4j.Logger;


/**
 * @author kitUIN
 */
public class ChatImage implements ModInitializer {
    public static final Logger LOGGER = LogUtils.getLogger();
    static {
        ChatImageCodeInstance.LOGGER = new ChatImageLogger();
    }
    @Override
    public void onInitialize() {
        ServerPlayNetworking.registerGlobalReceiver(ChatImagePacket.FILE_CHANNEL, (server, player, handler, buf, responseSender) -> ChatImagePacket.serverFileChannelReceived(server, buf));
        ServerPlayNetworking.registerGlobalReceiver(ChatImagePacket.GET_FILE_CHANNEL, (server, player, handler, buf, responseSender) -> ChatImagePacket.serverGetFileChannelReceived(player, buf));
    }
}
