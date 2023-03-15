package github.kituin.chatimage;

import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import github.kituin.chatimage.network.ChatImagePacket;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;


/**
 * @author kitUIN
 */
public class ChatImage implements ModInitializer {
    @Override
    public void onInitialize() {
        ServerPlayNetworking.registerGlobalReceiver(ChatImagePacket.FILE_CHANNEL, (server, player, handler, buf, responseSender) -> ChatImagePacket.serverFileChannelReceived(server, buf));
        ServerPlayNetworking.registerGlobalReceiver(ChatImagePacket.GET_FILE_CHANNEL, (server, player, handler, buf, responseSender) -> ChatImagePacket.serverGetFileChannelReceived(player, buf));
    }
}
