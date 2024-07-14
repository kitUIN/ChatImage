package io.github.kituin.chatimage;

import com.mojang.logging.LogUtils;
import io.github.kituin.ChatImageCode.ChatImageCodeInstance;
import io.github.kituin.chatimage.integration.ChatImageLogger;
import io.github.kituin.chatimage.network.ChatImagePacket;
import io.github.kituin.chatimage.network.DownloadFileChannelPacket;
import io.github.kituin.chatimage.network.FileChannelPacket;
import io.github.kituin.chatimage.network.FileInfoChannelPacket;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
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
        PayloadTypeRegistry.playC2S().register(FileChannelPacket.ID, FileChannelPacket.CODEC);
        PayloadTypeRegistry.playC2S().register(FileInfoChannelPacket.ID, FileInfoChannelPacket.CODEC);
        PayloadTypeRegistry.playS2C().register(DownloadFileChannelPacket.ID, DownloadFileChannelPacket.CODEC);
        PayloadTypeRegistry.playS2C().register(FileInfoChannelPacket.ID, FileInfoChannelPacket.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(FileChannelPacket.ID, ChatImagePacket::serverFileChannelReceived);
        ServerPlayNetworking.registerGlobalReceiver(FileInfoChannelPacket.ID, ChatImagePacket::serverGetFileChannelReceived);
    }
}
