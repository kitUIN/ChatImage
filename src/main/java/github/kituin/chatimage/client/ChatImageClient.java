package github.kituin.chatimage.client;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import github.kituin.chatimage.command.ChatImageCommand;
import github.kituin.chatimage.config.ChatImageConfig;
import github.kituin.chatimage.tool.ChatImageFrame;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;
import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static github.kituin.chatimage.ChatImage.FILE_CANNEL;
import static github.kituin.chatimage.tool.HttpUtils.CACHE_MAP;

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
        ClientPlayNetworking.registerGlobalReceiver(FILE_CANNEL, (client, handler, buf, responseSender) -> {
            for (Map.Entry<String, byte[]> entry : buf.readMap(PacketByteBuf::readString, PacketByteBuf::readByteArray).entrySet()) {
                try {
                    ChatImageFrame frame = new ChatImageFrame(new ByteArrayInputStream(entry.getValue()));
                    CACHE_MAP.put(entry.getKey(), frame);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
