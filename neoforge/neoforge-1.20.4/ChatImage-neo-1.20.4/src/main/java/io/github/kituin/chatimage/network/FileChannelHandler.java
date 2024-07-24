package io.github.kituin.chatimage.network;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

import static io.github.kituin.chatimage.network.ChatImagePacket.serverFileChannelReceived;

/**
 * 客户端发送文件分块到服务器通道
 */
public class FileChannelHandler {

    private static final FileChannelHandler INSTANCE = new FileChannelHandler();

    public static FileChannelHandler getInstance() {
        return INSTANCE;
    }

    /**
     * 服务端接收 图片文件分块 的处理
     */
    public void serverHandle(final FileChannelPacket packet, final PlayPayloadContext ctx) {
        ctx.player().ifPresent(player -> serverFileChannelReceived((ServerPlayer) player, packet.message()));
    }
}
