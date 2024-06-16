package io.github.kituin.chatimage.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;


import static io.github.kituin.chatimage.ChatImage.MOD_ID;

public record FileInfoChannelPacket(String message) implements CustomPacketPayload {
    public static ResourceLocation FILE_INFO = new ResourceLocation(MOD_ID, "file_info");

    public FileInfoChannelPacket(FriendlyByteBuf buffer) {
        this(buffer.readUtf());
    }

    public void write(final FriendlyByteBuf buffer) {
        buffer.writeUtf(message());
    }

    @Override
    public @NotNull ResourceLocation id() {
        return FILE_INFO;
    }

}
