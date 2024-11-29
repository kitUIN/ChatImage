// ONLY > neoforge-1.20.3
package io.github.kituin.chatimage.network;

import net.minecraft.server.level.ServerPlayer;

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
    public void serverHandle(final FileChannelPacket packet, final #PlayPayloadContext# ctx) {
// IF <= neoforge-1.21.0
////         ServerPlayer player = (ServerPlayer) ctx.player();
////         if (player == null) {
////             serverFileChannelReceived(player, packet.message());
////         }
// ELSE
        ctx.player().ifPresent(player -> serverFileChannelReceived((ServerPlayer) player, packet.message()));
// END IF
    }
}