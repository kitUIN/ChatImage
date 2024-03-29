package github.kituin.chatimage.client;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import github.kituin.chatimage.command.ChatImageCommand;
import github.kituin.chatimage.gui.ConfigScreen;
import github.kituin.chatimage.integration.ChatImageClientAdapter;
import github.kituin.chatimage.integration.ChatImageLogger;
import github.kituin.chatimage.network.ChatImagePacket;
import io.github.kituin.ChatImageCode.ChatImageCodeInstance;
import io.github.kituin.ChatImageCode.ChatImageConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.io.File;

import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;
import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static github.kituin.chatimage.network.ChatImagePacket.DOWNLOAD_FILE_CHANNEL;
import static github.kituin.chatimage.network.ChatImagePacket.GET_FILE_CHANNEL;


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
        ClientPlayNetworking.registerGlobalReceiver(DOWNLOAD_FILE_CHANNEL, (client, handler, buf, responseSender) -> ChatImagePacket.clientDownloadFileChannelReceived(buf));
        ClientPlayNetworking.registerGlobalReceiver(GET_FILE_CHANNEL, (client, handler, buf, responseSender) -> ChatImagePacket.clientGetFileChannelReceived(buf));
    }
}
