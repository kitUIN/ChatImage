package github.kituin.chatimage.client;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import github.kituin.chatimage.command.ChatImageCommand;
import github.kituin.chatimage.config.ChatImageConfig;
import github.kituin.chatimage.gui.ConfigScreen;
import github.kituin.chatimage.tool.ChatImageCode;
import github.kituin.chatimage.tool.ChatImageFrame;
import github.kituin.chatimage.tool.ChatImageUrl;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.PacketByteBuf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;
import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static github.kituin.chatimage.ChatImage.*;
import static github.kituin.chatimage.tool.ChatImageCode.CACHE_MAP;


/**
 * @author kitUIN
 */
@Environment(EnvType.CLIENT)
public class ChatImageClient implements ClientModInitializer {
    public static String MOD_ID = "chatimage";

    public static ChatImageConfig CONFIG = ChatImageConfig.loadConfig();
    private static final Logger LOGGER = LogManager.getLogger();
    public static HashMap<String, HashMap<Integer, byte[]>> CLIENT_CACHE_MAP = new HashMap<>();
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
                sendFilePackets(MinecraftClient.getInstance().player, url, file, FILE_CANNEL);
            } else {
                tryGetFromServer(url);
            }
        };
        System.setProperty("java.awt.headless", "false");
        configKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "config.chatimage.key",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_END,
                "config.chatimage.category"
        ));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (configKeyBinding.wasPressed()) {
                client.setScreen(new ConfigScreen(null));
            }
        });
        ClientCommandManager.DISPATCHER.register(
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
        ClientPlayNetworking.registerGlobalReceiver(DOWNLOAD_FILE_CANNEL, (client, handler, buf, responseSender) -> {
            for (Map.Entry<String, byte[]> entry : buf.readMap(PacketByteBuf::readString, PacketByteBuf::readByteArray).entrySet()) {
                String[] order = entry.getKey().split("->");
                if (order[1].equals(order[0]) && "0".equals(order[1])) {
                    CACHE_MAP.put(order[2], new ChatImageFrame(ChatImageFrame.FrameError.FILE_NOT_FOUND));
                    return;
                }
                HashMap<Integer, byte[]> list = new HashMap<>();
                if (CLIENT_CACHE_MAP.containsKey(order[2])) {
                    list = CLIENT_CACHE_MAP.get(order[2]);
                }
                list.put(Integer.valueOf(order[0]), entry.getValue());
                CLIENT_CACHE_MAP.put(order[2], list);
                if (list.size() == Integer.parseInt(order[1])) {
                    LOGGER.info("[merge]" + order[2]);
                    int length = 0;
                    for (Map.Entry<Integer, byte[]> en : list.entrySet()) {
                        length += en.getValue().length;
                    }
                    ByteBuffer bb = ByteBuffer.allocate(length);
                    for (int i = 0; i < list.size(); i++) {
                        bb.put(list.get(i));
                    }
                    try {
                        ChatImageUrl.putLocalFile(new ByteArrayInputStream(bb.array()), order[2]);
                    } catch (IOException e) {
                        LOGGER.error(e.toString());
                        CACHE_MAP.put(order[2], new ChatImageFrame(ChatImageFrame.FrameError.SERVER_FILE_LOAD_ERROR));
                    }
                }
            }

        });

    }
    /**
     * 尝试从服务器获取图片
     *
     * @param url 图片url
     */
    public static void tryGetFromServer(String url) {
        if (MinecraftClient.getInstance().player != null) {
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeString(url);
            sendFilePacketAsync(MinecraftClient.getInstance().player, GET_FILE_CANNEL, buf);
            LogManager.getLogger().info("[try get from server]" + url);
        } else {
            CACHE_MAP.put(url, new ChatImageFrame<>(ChatImageFrame.FrameError.FILE_NOT_FOUND));
        }
    }
}
