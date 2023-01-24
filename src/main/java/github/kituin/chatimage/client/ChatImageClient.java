package github.kituin.chatimage.client;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.logging.LogUtils;
import github.kituin.chatimage.Exceptions.InvalidChatImageCodeException;
import github.kituin.chatimage.commands.ChatImageCommand;
import github.kituin.chatimage.tools.ChatImageCode;
import github.kituin.chatimage.tools.ChatImageStyle;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import org.apache.commons.compress.utils.Lists;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;

import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;
import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static com.mojang.brigadier.builder.RequiredArgumentBuilder.argument;
import static github.kituin.chatimage.tools.ChatImageTool.codePattern2;
import static github.kituin.chatimage.tools.ChatImageTool.replaceCode;
import static net.fabricmc.fabric.api.message.v1.ServerMessageEvents.CHAT_MESSAGE;

/**
 * @author kitUIN
 */
@Environment(EnvType.CLIENT)
public class ChatImageClient implements ClientModInitializer {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static String MOD_ID = "chatimage";
    public static String CACHE_PATH = "ChatImageCache";

    public static int LIMIT_WIDTH = 0;
    public static int LIMIT_HEIGHT = 0;
    public static int PADDING_LEFT = 3;
    public static int PADDING_RIGHT = 3;
    public static int PADDING_TOP = 3;
    public static int PADDING_BOTTOM = 3;


    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(
                    LiteralArgumentBuilder.<FabricClientCommandSource>literal("chatimage")
                            .then(LiteralArgumentBuilder.<FabricClientCommandSource>literal("send")
                                    .then(RequiredArgumentBuilder.<FabricClientCommandSource, String>argument("url",  string())
                                            .executes(ChatImageCommand::sendChatImage))
                            )
            );
        });




    }
}
