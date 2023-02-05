package github.kituin.chatimage;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import github.kituin.chatimage.command.Help;
import github.kituin.chatimage.command.ReloadConfig;
import github.kituin.chatimage.command.SendChatImage;
import github.kituin.chatimage.config.ChatImageConfig;
import github.kituin.chatimage.gui.ConfigScreen;
import github.kituin.chatimage.network.DownloadFileCannel;
import github.kituin.chatimage.network.FileCannel;
import github.kituin.chatimage.network.GetFileCannel;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientCommandSourceStack;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;

import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;


@Mod(Chatimage.MOD_ID)
public class Chatimage {
    private static final Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "chatimage";
    /**
     * 文件分块
     */
    public static HashMap<String, HashMap<Integer, byte[]>> SERVER_CACHE_MAP = new HashMap<>();
    /**
     * 文件分块总数记录
     */
    public static HashMap<String, Integer> FILE_COUNT_MAP = new HashMap<>();
    /**
     * 广播列表
     */
    public static HashMap<String, List<String>> USER_CACHE_MAP = new HashMap<>();
    public static HashMap<String, HashMap<Integer, byte[]>> CLIENT_CACHE_MAP = new HashMap<>();

    public static ChatImageConfig CONFIG = ChatImageConfig.loadConfig();

    public Chatimage() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(Chatimage::init);
        modEventBus.addListener(Chatimage::onKeyBindRegister);
        // modEventBus.addListener(Chatimage::onKeyInput);
    }

    public static void init(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            FileCannel.register();
            GetFileCannel.register();
            DownloadFileCannel.register();

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
            System.setProperty("java.awt.headless", "false");
            LOGGER.info("Client start");

            MinecraftForge.EVENT_BUS.addListener(ClientModEvents::onKeyInput);
            MinecraftForge.EVENT_BUS.addListener(ClientModEvents::onClientStaring);


        }

        public static void onKeyInput(InputEvent.Key event) {
            if (KeyBindings.gatherManaKeyMapping.consumeClick()) {
                Minecraft.getInstance().setScreen(new ConfigScreen());
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
                            )
            );
        }
    }

}
