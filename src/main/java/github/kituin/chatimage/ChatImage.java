package github.kituin.chatimage;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import github.kituin.chatimage.command.Help;
import github.kituin.chatimage.command.ReloadConfig;
import github.kituin.chatimage.command.SendChatImage;
import github.kituin.chatimage.gui.ConfigScreen;
import github.kituin.chatimage.integration.ChatImageClientAdapter;
import github.kituin.chatimage.integration.ChatImageLogger;
import github.kituin.chatimage.network.DownloadFileChannel;
import github.kituin.chatimage.network.FileBackChannel;
import github.kituin.chatimage.network.FileChannel;
import github.kituin.chatimage.network.FileInfoChannel;
import io.github.kituin.ChatImageCode.ChatImageCodeInstance;
import io.github.kituin.ChatImageCode.ChatImageConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler.ConfigScreenFactory;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
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
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;

@Mod(ChatImage.MOD_ID)
public class ChatImage {
    public static final Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "chatimage";

    public static ChatImageConfig CONFIG;

    static {
        ChatImageCodeInstance.LOGGER = new ChatImageLogger();
    }
    public ChatImage() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(ChatImage::init);
    }

    public static void init(FMLCommonSetupEvent event) {
        event.enqueueWork(FileChannel::register);
        event.enqueueWork(FileInfoChannel::registerMessage);
        event.enqueueWork(FileBackChannel::register);
        event.enqueueWork(DownloadFileChannel::register);
        LOGGER.info("[ChatImage]Channel Register");
    }


    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("[ChatImage]Server starting");

    }


    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        static{
            ChatImageConfig.configFile = new File(FMLPaths.CONFIGDIR.get().toFile(), "chatimageconfig.json");
            CONFIG = ChatImageConfig.loadConfig();
            ChatImageCodeInstance.CLIENT_ADAPTER = new ChatImageClientAdapter();
        }
        @SubscribeEvent
        public static void onKeyBindRegister(RegisterKeyMappingsEvent event) {
            KeyBindings.init(event);
            LOGGER.info("KeyBindings Register");
        }
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

            LOGGER.info("[ChatImage]Client start");
            ModLoadingContext.get().registerExtensionPoint(ConfigScreenFactory.class, () -> new ConfigScreenFactory((minecraft, screen) -> new ConfigScreen(screen)));
            MinecraftForge.EVENT_BUS.addListener(ClientModEvents::onKeyInput);
            MinecraftForge.EVENT_BUS.addListener(ClientModEvents::onClientStaring);
            MinecraftForge.EVENT_BUS.addListener(ClientModEvents::onClientCommand);

        }
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
        public static void onKeyInput(InputEvent.Key event) {
            if (KeyBindings.gatherManaKeyMapping.consumeClick()) {
                Minecraft.getInstance().setScreen(new ConfigScreen(Minecraft.getInstance().screen));
            }
        }

        public static void onClientStaring(RegisterCommandsEvent event) {

        }
    }

}
