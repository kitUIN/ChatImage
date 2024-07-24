package io.github.kituin.chatimage.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

public class NetworkUtil {
    public static void sendToServer(CustomPacketPayload payload) {
        PacketDistributor.SERVER.noArg().send(payload);
    }
    public static void sendToPlayer(CustomPacketPayload payload, ServerPlayer player) {
        player.connection.send(payload);
    }
}
