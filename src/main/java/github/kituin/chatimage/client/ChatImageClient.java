package github.kituin.chatimage.client;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import github.kituin.chatimage.command.ChatImageCommand;
import github.kituin.chatimage.config.ChatImageConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;
import static com.mojang.brigadier.arguments.StringArgumentType.string;

/**
 * @author kitUIN
 */
@Environment(EnvType.CLIENT)
public class ChatImageClient implements ClientModInitializer {
    public static String MOD_ID = "chatimage";

    public static ChatImageConfig CONFIG = ChatImageConfig.loadConfig();

    @Override
    public void onInitializeClient() {
        System.setProperty("java.awt.headless", "false");
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(
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
            );
        });


    }
}
