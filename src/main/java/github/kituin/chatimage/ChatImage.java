package github.kituin.chatimage;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import github.kituin.chatimage.command.Help;
import github.kituin.chatimage.command.ReloadConfig;
import github.kituin.chatimage.command.SendChatImage;
import github.kituin.chatimage.config.ChatImageConfig;
import github.kituin.chatimage.gui.ConfigScreen;
import github.kituin.chatimage.network.DownloadFileChannel;
import github.kituin.chatimage.network.FileBackChannel;
import github.kituin.chatimage.network.FileChannel;
import github.kituin.chatimage.network.FileInfoChannel;
import io.github.kituin.ChatImageCode.ChatImageCode;
import io.github.kituin.ChatImageCode.ChatImageFrame;
import io.github.kituin.ChatImageCode.ChatImageUrl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigGuiHandler.ConfigGuiFactory;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.List;

import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;
import static github.kituin.chatimage.network.ChatImagePacket.loadFromServer;
import static github.kituin.chatimage.network.ChatImagePacket.sendFilePackets;
import static io.github.kituin.ChatImageCode.ChatImagePacketHelper.createFilePacket;


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
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Server starting");
    }


    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        public static void onClientCommand(RegisterClientCommandsEvent event) {
            CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
            LiteralCommandNode<CommandSourceStack> cmd = dispatcher.register(
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
        }
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            ChatImageFrame.textureHelper = image -> {
                NativeImage nativeImage = NativeImage.read(image);
                return new ChatImageFrame.TextureReader<>(
                        Minecraft.getInstance().getTextureManager().register(MOD_ID + "/chatimage",
                                new DynamicTexture(nativeImage)),
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
            ModLoadingContext.get().registerExtensionPoint(ConfigGuiFactory.class, () -> new ConfigGuiFactory((minecraft, screen) -> new ConfigScreen(screen)));
            MinecraftForge.EVENT_BUS.addListener(ClientModEvents::onKeyInput);
            MinecraftForge.EVENT_BUS.addListener(ClientModEvents::onClientCommand);
            MinecraftForge.EVENT_BUS.addListener(ClientModEvents::onClientStaring);
        }

        public static void onKeyInput(InputEvent.KeyInputEvent event) {
            if (KeyBindings.gatherManaKeyMapping.consumeClick()) {
                Minecraft.getInstance().setScreen(new ConfigScreen(Minecraft.getInstance().screen));
            }
        }

        public static void onClientStaring(RegisterCommandsEvent event) {

        }
    }

}
