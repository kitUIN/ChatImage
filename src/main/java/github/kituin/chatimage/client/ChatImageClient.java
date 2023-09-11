package github.kituin.chatimage.client;

import com.github.chatimagecode.ChatImageCode;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import github.kituin.chatimage.command.ChatImageCommand;
import github.kituin.chatimage.config.ChatImageConfig;
import github.kituin.chatimage.gui.ConfigScreen;
import github.kituin.chatimage.network.ChatImagePacket;
import com.github.chatimagecode.ChatImageFrame;
import com.github.chatimagecode.ChatImageUrl;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.PacketByteBuf;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.util.List;

import static com.github.chatimagecode.ChatImagePacketHelper.createFilePacket;
import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;
import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static github.kituin.chatimage.network.ChatImagePacket.*;


/**
 * @author kitUIN
 */
@Environment(EnvType.CLIENT)
public class ChatImageClient implements ClientModInitializer {
    public static String MOD_ID = "chatimage";

    public static ChatImageConfig CONFIG = ChatImageConfig.loadConfig();
    private static KeyBinding configKeyBinding;

    @Override
    public void onInitializeClient() {
        ChatImageFrame.textureHelper = image -> {
            NativeImage nativeImage = NativeImage.read(image);
            return new ChatImageFrame.TextureReader<>(
                    MinecraftClient.getInstance().getTextureManager().registerDynamicTexture(MOD_ID + "/chatimage",
                            new NativeImageBackedTexture(nativeImage)),
                    nativeImage.getWidth(),
                    nativeImage.getHeight()
            );
        };
        ChatImageUrl.networkHelper = (url, file, isServer) -> {
            if (isServer) {
                List<String> stringList = createFilePacket(url, file);
                sendPacketAsync(FILE_CHANNEL, stringList);
            } else {
                loadFromServer(url);
            }
        };
        ChatImageUrl.cachePathHelper = () -> {
            File folder = new File(CONFIG.cachePath);
            if (!folder.exists()) {
                folder.mkdirs();
            }
        };
        ChatImageCode.timeoutHelper = () -> CONFIG.timeout;
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
