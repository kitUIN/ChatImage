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
import github.kituin.chatimage.network.FileChannel;
import github.kituin.chatimage.network.FileChannelPacket;
import github.kituin.chatimage.network.GetFileChannel;
import github.kituin.chatimage.tool.ChatImageFrame;
import github.kituin.chatimage.tool.ChatImageUrl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler.ConfigScreenFactory;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
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

import java.util.List;

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
        modEventBus.addListener(ChatImage::onKeyBindRegister);
        // modEventBus.addListener(Chatimage::onKeyInput);
    }

    public static void init(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            FileChannel.register();
            GetFileChannel.register();
            DownloadFileChannel.register();

        });
        LOGGER.info("Cannel Register");
    }


    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Server starting");
    }


    public static void onKeyBindRegister(RegisterKeyMappingsEvent event) {
        KeyBindings.init(event);
        LOGGER.info("KeyBindings Register");
    }


    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

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
                    List<FileChannelPacket> bufs = createFilePacket(url, file);
                    if (bufs != null) {
                        sendFilePackets(bufs);
                    }
                } else {
                    loadFromServer(url);
                }
            };
            LOGGER.info("Client start");
            ModLoadingContext.get().registerExtensionPoint(ConfigScreenFactory.class, () -> new ConfigScreenFactory((minecraft, screen) -> new ConfigScreen(screen)));
            MinecraftForge.EVENT_BUS.addListener(ClientModEvents::onKeyInput);
            MinecraftForge.EVENT_BUS.addListener(ClientModEvents::onClientStaring);
        }

        public static void onKeyInput(InputEvent.Key event) {
            if (KeyBindings.gatherManaKeyMapping.consumeClick()) {
                Minecraft.getInstance().setScreen(new ConfigScreen(Minecraft.getInstance().screen));
            }
        }

        public static void onClientStaring(RegisterCommandsEvent event) {
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
    }

}
