// ONLY forge-1.16.5

package net.minecraft.server.level;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.world.server.ServerWorld;

public class ServerPlayer extends ServerPlayerEntity {

    public ServerPlayer(MinecraftServer p_i45285_1_, ServerWorld p_i45285_2_, GameProfile p_i45285_3_, PlayerInteractionManager p_i45285_4_) {
        super(p_i45285_1_, p_i45285_2_, p_i45285_3_, p_i45285_4_);
    }
}