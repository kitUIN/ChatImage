package github.kituin.chatimage;

import com.github.chatimagecode.ChatImageCode;
import com.github.chatimagecode.ChatImageFrame;
import com.github.chatimagecode.ChatImageUrl;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import github.kituin.chatimage.command.Help;
import github.kituin.chatimage.command.ReloadConfig;
import github.kituin.chatimage.command.SendChatImage;
import github.kituin.chatimage.config.ChatImageConfig;
import github.kituin.chatimage.gui.ConfigScreen;
import github.kituin.chatimage.network.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.List;

import static com.github.chatimagecode.ChatImagePacketHelper.createFilePacket;
import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;
import static github.kituin.chatimage.network.ChatImagePacket.*;


@Mod(ChatImage.MOD_ID)
public class ChatImage {
    public static final Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "chatimage";

    public static ChatImageConfig CONFIG = ChatImageConfig.loadConfig();

    public ChatImage() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(ChatImage::init);
    }

    public static void init(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            FileChannel.register();
            FileInfoChannel.registerMessage();
            FileBackChannel.register();
            DownloadFileChannel.register();
        });
        LOGGER.info("Cannel Register");
    }


    @SubscribeEvent
    public void onServerStarting(RegisterCommandsEvent  event) {
        CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();
        LiteralCommandNode<CommandSource> cmd = dispatcher.register(
                Commands.literal(MOD_ID)
                        .then(Commands.literal("send")
                                .then(Commands.argument("name", StringArgumentType.string())
                                        .then(Commands.argument("url", greedyString())
                                                .executes(SendChatImage.instance)
                                        )
                                )
                        )
                        .then(Commands.literal("url")
                                .then(Commands.argument("url", greedyString())
                                        .executes(SendChatImage.instance)
                                )
                        )
                        .then(Commands.literal("help")
                                .executes(Help.instance)
                        )
                        .then(Commands.literal("reload")
                                .executes(ReloadConfig.instance)
                        )

        );

        LOGGER.info("Server starting");
    }


    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            ChatImageFrame.textureHelper = image -> {
                NativeImage nativeImage = NativeImage.read(image);
                return new ChatImageFrame.TextureReader<>(
                        Minecraft.getInstance().getTextureManager()
                                .getDynamicTextureLocation(MOD_ID + "/chatimage",new DynamicTexture(nativeImage)),
                        nativeImage.getWidth(),
                        nativeImage.getHeight()
                );
            };
            ChatImageUrl.networkHelper = (url, file, isServer) -> {
                if (isServer) {
                    List<String> bufs = createFilePacket(url, file);
                    sendFilePackets(bufs);
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
            LOGGER.info("[ChatImage]Client start");
            KeyBindings.init();
            LOGGER.info("KeyBindings Register");
            ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY,
                    () -> (mc, screen) -> new ConfigScreen(screen));
            MinecraftForge.EVENT_BUS.addListener(ClientModEvents::onKeyInput);
            MinecraftForge.EVENT_BUS.addListener(ClientModEvents::onClientStaring);
        }

        public static void onKeyInput(InputEvent.KeyInputEvent event) {
            if (KeyBindings.gatherManaKeyMapping.isKeyDown()) {
                Minecraft.getInstance().displayGuiScreen(new ConfigScreen(Minecraft.getInstance().currentScreen));
            }
        }

        public static void onClientStaring(RegisterCommandsEvent event) {

        }
    }

}
