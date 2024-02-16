package io.github.kituin.chatimage.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import static io.github.kituin.ChatImageCode.NetworkHelper.MAX_STRING;
import static io.github.kituin.chatimage.ChatImage.MOD_ID;

public record FileChannelPacket(String message) implements CustomPacketPayload {
    /**
     * 客户端发送文件分块到服务器通道(Map)
     */
    public static ResourceLocation FILE_CHANNEL = new ResourceLocation(MOD_ID, "file_channel");
    public FileChannelPacket(FriendlyByteBuf buffer) {
        this(buffer.readUtf(MAX_STRING)) ;
    }
    public void write(final FriendlyByteBuf buffer) {
        buffer.writeUtf(message(),MAX_STRING);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return FILE_CHANNEL;
    }





}
