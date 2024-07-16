package io.github.kituin.chatimage.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;


public record FileChannelPacket(String message) implements CustomPayload {
    /**
     * 客户端发送文件分块到服务器通道(Map)
     */
    public static final CustomPayload.Id<FileChannelPacket> ID =
            new CustomPayload.Id<>(Identifier.of("chatimage", "get_file_channel"));

    public static final PacketCodec<RegistryByteBuf, FileChannelPacket> CODEC =
            PacketCodec.tuple(PacketCodecs.STRING, FileChannelPacket::message, FileChannelPacket::new);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
