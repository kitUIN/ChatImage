package github.kituin.chatimage;

import github.kituin.chatimage.tool.ChatImagePacket;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;


/**
 * @author kitUIN
 */
public class ChatImage implements ModInitializer {
    @Override
    public void onInitialize() {
        ServerPlayNetworking.registerGlobalReceiver(ChatImagePacket.FILE_CHANNEL, (server, player, handler, buf, responseSender) -> {
            ChatImagePacket.serverFileChannelReceived(server, buf);
        });
        ServerPlayNetworking.registerGlobalReceiver(ChatImagePacket.GET_FILE_CHANNEL, (server, player, handler, buf, responseSender) -> {
            ChatImagePacket.serverGetFileChannelReceived(player, buf);
        });
    }
}
