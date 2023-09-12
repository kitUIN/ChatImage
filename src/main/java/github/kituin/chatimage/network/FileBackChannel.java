package github.kituin.chatimage.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import static github.kituin.chatimage.ChatImage.MOD_ID;

public class FileBackChannel {
    public static SimpleChannel INSTANCE;
    public static final String VERSION = "1.0";
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }

    public static void register() {
        INSTANCE = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(MOD_ID, "file_back"),
                () -> VERSION,
                (version) -> version.equals(VERSION),
                (version) -> version.equals(VERSION)
        );

        INSTANCE.messageBuilder(FileInfoChannelPacket.class, nextID(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(FileInfoChannelPacket::toBytes)
                .decoder(FileInfoChannelPacket::new)
                .consumer(FileInfoChannelPacket::clientHandle)
                .add();
    }
    public static <MSG> void sendToPlayer(MSG message, ServerPlayerEntity player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
