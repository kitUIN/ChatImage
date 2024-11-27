// ONLY > neoforge-1.20.3
package io.github.kituin.chatimage.network;


import static io.github.kituin.chatimage.network.ChatImagePacket.clientDownloadFileChannelReceived;

/**
 * 发送文件分块到客户端通道
 */
public class DownloadFileChannelHandler {
    private static final DownloadFileChannelHandler INSTANCE = new DownloadFileChannelHandler();

    public static DownloadFileChannelHandler getInstance() {
        return INSTANCE;
    }

    public void clientHandle(final DownloadFileChannelPacket data, final #PlayPayloadContext# context) {
        clientDownloadFileChannelReceived(data.message());
    }
}