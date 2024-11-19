package io.github.kituin.chatimage;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.github.kituin.chatimage.command.Help;
import io.github.kituin.chatimage.command.ReloadConfig;
import io.github.kituin.chatimage.command.SendChatImage;
import io.github.kituin.chatimage.gui.ConfigScreen;
import io.github.kituin.chatimage.integration.ChatImageClientAdapter;
import io.github.kituin.chatimage.integration.ChatImageLogger;
import io.github.kituin.chatimage.network.*;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import io.github.kituin.ChatImageCode.ChatImageCodeInstance;
import io.github.kituin.ChatImageCode.ChatImageConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.ConfigScreenHandler;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// IF > neoforge-1.20.3
//// import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
// ELSE
//import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
// END IF
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

    public ChatImage(IEventBus modEventBus) {
        NeoForge.EVENT_BUS.register(this);
// IF <= neoforge-1.20.3
//        modEventBus.addListener(ChatImage::init);
// ELSE
//
//        modEventBus.addListener(ChatImage::register);
// END IF
    }

// IF <= neoforge-1.20.3
//     public static void init(FMLCommonSetupEvent event) {
//         event.enqueueWork(FileChannel::register);
//         event.enqueueWork(FileInfoChannel::registerMessage);
//         event.enqueueWork(FileBackChannel::register);
//         event.enqueueWork(DownloadFileChannel::register);
//         LOGGER.info("[ChatImage]Channel Register");
//     }
// ELSE
//    public static void register(final RegisterPayloadHandlerEvent event) {
//        final IPayloadRegistrar registrar = event.registrar(MOD_ID).optional();
//        registrar.play(DownloadFileChannelPacket.DOWNLOAD_FILE_CHANNEL, DownloadFileChannelPacket::new, handler -> handler
//                .client(DownloadFileChannelHandler.getInstance()::clientHandle));
//        registrar.play(FileChannelPacket.FILE_CHANNEL, FileChannelPacket::new, handler -> handler
//                .server(FileChannelHandler.getInstance()::serverHandle));
//        registrar.play(FileInfoChannelPacket.FILE_INFO, FileInfoChannelPacket::new, handler -> handler
//                .client(FileInfoChannelHandler.getInstance()::clientHandle)
//                .server(FileInfoChannelHandler.getInstance()::serverHandle));
//    }
// END IF

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("[ChatImage]Server starting");

    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        static {
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
            ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((minecraft, screen) -> new ConfigScreen(screen)));
            NeoForge.EVENT_BUS.addListener(ClientModEvents::onKeyInput);
            NeoForge.EVENT_BUS.addListener(ClientModEvents::onClientStaring);
            NeoForge.EVENT_BUS.addListener(ClientModEvents::onClientCommand);

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