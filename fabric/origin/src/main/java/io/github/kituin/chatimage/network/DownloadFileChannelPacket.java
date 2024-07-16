package io.github.kituin.chatimage.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;


public record DownloadFileChannelPacket(String message) implements CustomPayload {
    /**
     * 发送文件分块到客户端通道(Map)
     */
    public static final Id<DownloadFileChannelPacket> ID =
            new CustomPayload.Id<>(Identifier.of("chatimage", "download_file_channel"));

    public static final PacketCodec<RegistryByteBuf, DownloadFileChannelPacket> CODEC =
            PacketCodec.tuple(PacketCodecs.STRING, DownloadFileChannelPacket::message, DownloadFileChannelPacket::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}