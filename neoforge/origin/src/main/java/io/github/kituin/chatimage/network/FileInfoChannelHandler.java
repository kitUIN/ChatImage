// ONLY > neoforge-1.20.3
package io.github.kituin.chatimage.network;

import com.google.common.collect.Lists;
import io.github.kituin.ChatImageCode.ChatImageFrame;
import net.minecraft.server.level.ServerPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.kituin.ChatImageCode.ClientStorage.AddImageError;
import static io.github.kituin.ChatImageCode.ServerStorage.*;
import static io.github.kituin.chatimage.network.ChatImagePacket.loadFromServer;
import static io.github.kituin.chatimage.network.ChatImagePacket.serverFileInfoChannelReceived;

public class FileInfoChannelHandler {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final FileInfoChannelHandler INSTANCE = new FileInfoChannelHandler();

    public static FileInfoChannelHandler getInstance() {
        return INSTANCE;
    }

    public void serverHandle(final FileInfoChannelPacket packet, final #PlayPayloadContext#  ctx) {
        ServerPlayer player = (ServerPlayer) ctx.player();
        if (player == null) {
            serverFileInfoChannelReceived(player, packet.message());
        }
    }
    public void clientHandle(final FileInfoChannelPacket packet, final #PlayPayloadContext# ctx) {
        String data = packet.message();
        String url = data.substring(6);
        LOGGER.info(url);
        if (data.startsWith("null")) {
            LOGGER.info("[GetFileChannel-NULL]" + url);
            AddImageError(url, ChatImageFrame.FrameError.FILE_NOT_FOUND);
        } else if (data.startsWith("true")) {
            LOGGER.info("[GetFileChannel-Retry]" + url);
            loadFromServer(url);
        }
    }
}