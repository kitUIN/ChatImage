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
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
// IF fabric-1.16.5 || fabric-1.18.2
//import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
//import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
// ELSE
//import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
//import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
// END IF
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.io.File;
// IF fabric-1.21 || fabric-1.20.5
//import io.github.kituin.chatimage.network.DownloadFileChannelPacket;
//import io.github.kituin.chatimage.network.FileInfoChannelPacket;
// ELSE
//import static io.github.kituin.chatimage.network.ChatImagePacket.*;
//import static io.github.kituin.chatimage.tool.SimpleUtil.*;
// END IF
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
        ChatImageConfig.configFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), "chatimageconfig.json");
        CONFIG = ChatImageConfig.loadConfig();
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
                setScreen(client,new ConfigScreen());
            }
        });
// IF fabric-1.16.5 || fabric-1.18.2
//        ClientCommandManager.DISPATCHER
// ELSE
//        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher
// END IF
                .register(
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
// IF fabric-1.16.5 || fabric-1.18.2
//                );
// ELSE
//                ));
// END IF
// IF fabric-1.21 || fabric-1.20.5
//        ClientPlayNetworking.registerGlobalReceiver(DownloadFileChannelPacket.ID, (payload, context) -> ChatImagePacket.clientDownloadFileChannelReceived(payload));
//        ClientPlayNetworking.registerGlobalReceiver(FileInfoChannelPacket.ID, (payload, context) -> ChatImagePacket.clientGetFileChannelReceived(payload));
// ELSE
//        ClientPlayNetworking.registerGlobalReceiver(DOWNLOAD_FILE_CHANNEL, (client, handler, buf, responseSender) -> ChatImagePacket.clientDownloadFileChannelReceived(buf));
//        ClientPlayNetworking.registerGlobalReceiver(GET_FILE_CHANNEL, (client, handler, buf, responseSender) -> ChatImagePacket.clientGetFileChannelReceived(buf));
// END IF

    }
}