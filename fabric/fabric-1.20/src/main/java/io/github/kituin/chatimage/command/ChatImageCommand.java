package io.github.kituin.chatimage.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.github.kituin.ChatImageCode.ChatImageCode;
import io.github.kituin.ChatImageCode.ChatImageCodeInstance;
import io.github.kituin.ChatImageCode.ChatImageConfig;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static io.github.kituin.chatimage.client.ChatImageClient.CONFIG;
import static io.github.kituin.ChatImageCode.ChatImageCodeInstance.LOGGER;

public class ChatImageCommand {


    public static int sendChatImage(CommandContext<FabricClientCommandSource> context) {
        String url = StringArgumentType.getString(context, "url");
        ChatImageCode.Builder builder = ChatImageCodeInstance.createBuilder().setUrlForce(url);
        try {
            String name = StringArgumentType.getString(context, "name");
            builder.setName(name);
        } catch (java.lang.IllegalArgumentException e) {
            LOGGER.info("arg: `name` is omitted, use the default string");
        }
        context.getSource().getPlayer().networkHandler.sendChatMessage(builder.build().toString());
        return Command.SINGLE_SUCCESS;
    }

    public static int help(CommandContext<FabricClientCommandSource> context) {

        context.getSource().sendFeedback(
                getHelpText("/chatimage help", "", "help.chatimage.command")
                        .append(getHelpText("/chatimage send ", "<name> <url>", "send.chatimage.command"))
                        .append(getHelpText("/chatimage url ", "<url>", "url.chatimage.command"))
                        .append(getHelpText("/chatimage reload ", "", "reload.chatimage.command"))
        );
        return Command.SINGLE_SUCCESS;
    }

    public static int reloadConfig(CommandContext<FabricClientCommandSource> context) {
        CONFIG = ChatImageConfig.loadConfig();
        context.getSource().sendFeedback(Text.translatable("success.reload.chatimage.command").setStyle(Style.EMPTY.withColor(Formatting.GREEN)));
        return Command.SINGLE_SUCCESS;
    }

    private static MutableText getHelpText(String help, String arg, String usage) {
        String all = help + arg;
        StringBuilder sb = new StringBuilder(all);
        if (all.length() <= 35) {
            for (int i = 0; i < 35 - all.length(); i++) {
                sb.append(" ");
            }
        }
        MutableText text = (MutableText) Text.of(sb.toString());
        MutableText info = Text.translatable(usage);
        return text.setStyle(Style.EMPTY.withColor(Formatting.GOLD).withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, help))).append(info).append(Text.of("\n"));
    }

}
