package io.github.kituin.chatimage.client;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import io.github.kituin.ChatImageCode.ChatImageCodeInstance;
import io.github.kituin.ChatImageCode.ChatImageConfig;
import io.github.kituin.chatimage.command.ChatImageCommand;
import io.github.kituin.chatimage.gui.ConfigScreen;
import io.github.kituin.chatimage.integration.ChatImageClientAdapter;
import io.github.kituin.chatimage.integration.ChatImageLogger;
import io.github.kituin.chatimage.network.ChatImagePacket;
import io.github.kituin.chatimage.network.DownloadFileChannelPacket;
import io.github.kituin.chatimage.network.FileInfoChannelPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.io.File;

import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;
import static com.mojang.brigadier.arguments.StringArgumentType.string;

/**
 * @author kitUIN
 */
@Environment(EnvType.CLIENT)
public class ChatImageClient implements ClientModInitializer {
    public static String MOD_ID = "chatimage";

    public static ChatImageConfig CONFIG;
    private static KeyBinding configKeyBinding;
    static {
        io.github.kituin.ChatImageCode.ChatImageConfig.configFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), "chatimageconfig.json");
        CONFIG = io.github.kituin.ChatImageCode.ChatImageConfig.loadConfig();
        ChatImageCodeInstance.CLIENT_ADAPTER = new ChatImageClientAdapter();
        ChatImageCodeInstance.LOGGER = new ChatImageLogger();
    }
    @Override
    public void onInitializeClient() {
        
        configKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "config.chatimage.key",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_END,
                "config.chatimage.category"
        ));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (configKeyBinding.wasPressed()) {
                client.setScreen(new ConfigScreen());
            }
        });
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(
                LiteralArgumentBuilder.<FabricClientCommandSource>literal("chatimage").executes(ChatImageCommand::help)
                        .then(LiteralArgumentBuilder.<FabricClientCommandSource>literal("send")
                                .then(RequiredArgumentBuilder.<FabricClientCommandSource, String>argument("name", string())
                                        .then(RequiredArgumentBuilder.<FabricClientCommandSource, String>argument("url", greedyString())
                                                .executes(ChatImageCommand::sendChatImage)
                                        )
                                )
                        )
                        .then(LiteralArgumentBuilder.<FabricClientCommandSource>literal("url")
                                .then(RequiredArgumentBuilder.<FabricClientCommandSource, String>argument("url", greedyString())
                                        .executes(ChatImageCommand::sendChatImage)
                                )
                        )
                        .then(LiteralArgumentBuilder.<FabricClientCommandSource>literal("help")
                                .executes(ChatImageCommand::help)
                        )
                        .then(LiteralArgumentBuilder.<FabricClientCommandSource>literal("reload")
                                .executes(ChatImageCommand::reloadConfig)
                        )

        ));
        PayloadTypeRegistry.playS2C().register(DownloadFileChannelPacket.ID, DownloadFileChannelPacket.CODEC);
        ClientPlayNetworking.registerGlobalReceiver(DownloadFileChannelPacket.ID, (payload, context) -> ChatImagePacket.clientDownloadFileChannelReceived(payload));
        PayloadTypeRegistry.playS2C().register(FileInfoChannelPacket.ID, FileInfoChannelPacket.CODEC);
        ClientPlayNetworking.registerGlobalReceiver(FileInfoChannelPacket.ID, (payload, context) -> ChatImagePacket.clientGetFileChannelReceived(payload));
    }
}
