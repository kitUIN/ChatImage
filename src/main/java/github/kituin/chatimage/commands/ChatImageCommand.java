package github.kituin.chatimage.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import github.kituin.chatimage.tools.ChatImageCode;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.concurrent.Executors;

public class ChatImageCommand {


    public static int sendChatImage(CommandContext<FabricClientCommandSource> context) {
        String url = StringArgumentType.getString(context, "url");
        context.getSource().getPlayer().networkHandler.sendChatMessage(ChatImageCode.parse(url,false,null));
        return  Command.SINGLE_SUCCESS;
    }
}
