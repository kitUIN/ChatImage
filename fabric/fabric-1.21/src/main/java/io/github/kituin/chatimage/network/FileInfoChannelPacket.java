package io.github.kituin.chatimage.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;


public record FileInfoChannelPacket(String message) implements CustomPayload {

    public static final CustomPayload.Id<FileInfoChannelPacket> ID =
            new CustomPayload.Id<>(Identifier.of("chatimage", "file_info"));
    public static final PacketCodec<RegistryByteBuf, FileInfoChannelPacket> CODEC =
            PacketCodec.tuple(PacketCodecs.STRING, FileInfoChannelPacket::message, FileInfoChannelPacket::new);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
