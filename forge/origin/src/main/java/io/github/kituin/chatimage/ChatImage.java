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
import io.github.kituin.chatimage.network.DownloadFileChannel;
import io.github.kituin.chatimage.network.FileBackChannel;
import io.github.kituin.chatimage.network.FileChannel;
import io.github.kituin.chatimage.network.FileInfoChannel;
import io.github.kituin.ChatImageCode.ChatImageCodeInstance;
import io.github.kituin.ChatImageCode.ChatImageConfig;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
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
// IF > forge-1.18.2
//import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
// END IF
// IF forge-1.16.5
//import net.minecraftforge.fml.ExtensionPoint;
// ELSE
//import net.minecraft.commands.Commands;
//import net.minecraftforge.client.event.RegisterClientCommandsEvent;
//import net.minecraftforge.event.server.ServerStartingEvent;
// END IF

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

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        static{
            ChatImageConfig.configFile = new File(FMLPaths.CONFIGDIR.get().toFile(), "chatimageconfig.json");
            CONFIG = ChatImageConfig.loadConfig();
            ChatImageCodeInstance.CLIENT_ADAPTER = new ChatImageClientAdapter();
        }
// IF >= forge-1.19
//    @SubscribeEvent
//    public static void onKeyBindRegister(RegisterKeyMappingsEvent event) {
//        KeyBindings.init(event);
//        LOGGER.info("KeyBindings Register");
//    }
// END IF

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("[ChatImage]Client start");
// IF >= forge-1.16 && < forge-1.19
//           KeyBindings.init();
//           LOGGER.info("KeyBindings Register");
// END IF
// IF forge-1.16.5
//            ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY,
//                    () -> (mc, screen) -> new ConfigScreen(screen));
// ELSE
//            ModLoadingContext.get().registerExtensionPoint(#ConfigScreenFactory#.class, () -> new #ConfigScreenFactory#((minecraft, screen) -> new ConfigScreen(screen)));
//            MinecraftForge.EVENT_BUS.addListener(ClientModEvents::onClientCommand);
// END IF
            MinecraftForge.EVENT_BUS.addListener(ClientModEvents::onKeyInput);
            MinecraftForge.EVENT_BUS.addListener(ClientModEvents::onClientStaring);
        }
// IF > forge-1.16.5
//        public static void onClientCommand(RegisterClientCommandsEvent event) {
//            CommandDispatcher<#CommandSourceStack#> dispatcher = event.getDispatcher();
//            LiteralCommandNode<#CommandSourceStack#> cmd = dispatcher.register(
//                    Commands.literal(MOD_ID)
//                            .then(Commands.literal("send")
//                                    .then(Commands.argument("name", StringArgumentType.string())
//                                            .then(Commands.argument("url", greedyString())
//                                                    .executes(SendChatImage.instance)
//                                            )
//                                    )
//                            )
//                            .then(Commands.literal("url")
//                                    .then(Commands.argument("url", greedyString())
//                                            .executes(SendChatImage.instance)
//                                    )
//                            )
//                            .then(Commands.literal("help")
//                                    .executes(Help.instance)
//                            )
//                            .then(Commands.literal("reload")
//                                    .executes(ReloadConfig.instance)
//                            )
//
//            );
//        }
// END IF

        public static void onKeyInput(#InputEvent.Key# event) {
            if (KeyBindings.gatherManaKeyMapping.consumeClick()) {
                Minecraft.getInstance().setScreen(new ConfigScreen(Minecraft.getInstance().screen));
            }
        }

        public static void onClientStaring(RegisterCommandsEvent event) {
        }
    }
}