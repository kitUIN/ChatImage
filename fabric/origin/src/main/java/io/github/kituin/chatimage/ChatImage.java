package io.github.kituin.chatimage;

import io.github.kituin.ChatImageCode.ChatImageCodeInstance;
import io.github.kituin.chatimage.integration.ChatImageLogger;
import io.github.kituin.chatimage.network.ChatImagePacket;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

// IF >= fabric-1.20.5
//import io.github.kituin.chatimage.network.DownloadFileChannelPacket;
//import io.github.kituin.chatimage.network.FileChannelPacket;
//import io.github.kituin.chatimage.network.FileInfoChannelPacket;
//import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
// END IF
// IF fabric-1.16.5
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
// ELSE
//import com.mojang.logging.LogUtils;
//import org.slf4j.Logger;
// END IF

/**
 * @author kitUIN
 */
public class ChatImage implements ModInitializer {
// IF fabric-1.16.5
//    public static final Logger LOGGER = LogManager.getLogger();
// ELSE
//    public static final Logger LOGGER = LogUtils.getLogger();
// END IF
    static {
        ChatImageCodeInstance.LOGGER = new ChatImageLogger();
    }

    @Override
    public void onInitialize() {
// IF >= fabric-1.20.5
//       PayloadTypeRegistry.playC2S().register(FileChannelPacket.ID, FileChannelPacket.CODEC);
//       PayloadTypeRegistry.playC2S().register(FileInfoChannelPacket.ID, FileInfoChannelPacket.CODEC);
//       PayloadTypeRegistry.playS2C().register(DownloadFileChannelPacket.ID, DownloadFileChannelPacket.CODEC);
//       PayloadTypeRegistry.playS2C().register(FileInfoChannelPacket.ID, FileInfoChannelPacket.CODEC);
//       ServerPlayNetworking.registerGlobalReceiver(FileChannelPacket.ID, ChatImagePacket::serverFileChannelReceived);
//       ServerPlayNetworking.registerGlobalReceiver(FileInfoChannelPacket.ID, ChatImagePacket::serverGetFileChannelReceived);
// ELSE
//         ServerPlayNetworking.registerGlobalReceiver(ChatImagePacket.FILE_CHANNEL, (server, player, handler, buf, responseSender) -> ChatImagePacket.serverFileChannelReceived(server, buf));
//         ServerPlayNetworking.registerGlobalReceiver(ChatImagePacket.GET_FILE_CHANNEL, (server, player, handler, buf, responseSender) -> ChatImagePacket.serverGetFileChannelReceived(player, buf));
// END IF
    }
}