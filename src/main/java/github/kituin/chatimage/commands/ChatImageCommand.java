package github.kituin.chatimage.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.logging.LogUtils;
import github.kituin.chatimage.Exceptions.InvalidChatImageUrlException;
import github.kituin.chatimage.tools.ChatImageCode;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ChatImageCommand {


    public static int sendChatImage(CommandContext<FabricClientCommandSource> context) {
        String name = null;
        String url = StringArgumentType.getString(context, "url");
        try {
            name = StringArgumentType.getString(context, "name");
        } catch (java.lang.IllegalArgumentException e) {
            LogUtils.getLogger().info("arg: `name` is omitted, use the default string");
        }
        try {
            ChatImageCode code = new ChatImageCode(url, name);
            context.getSource().getPlayer().networkHandler.sendChatMessage(code.toString());
        } catch (InvalidChatImageUrlException e) {
            MutableText text = Text.literal(e.getMode().toString() + ": " + e.getMessage());
            context.getSource().sendFeedback(text.setStyle(Style.EMPTY.withColor(Formatting.RED)));
        }

        return Command.SINGLE_SUCCESS;
    }

    public static int help(CommandContext<FabricClientCommandSource> context) {
        MutableText text = (MutableText) getHelpText("/chatimage help", "", "help.chatimage.command");
        context.getSource().sendFeedback(
                text.append(getHelpText("/chatimage send ", "<name> <url>", "send.chatimage.command"))
                        .append(getHelpText("/chatimage url ", "<url>", "url.chatimage.command")));
        return Command.SINGLE_SUCCESS;
    }

    private static Text getHelpText(String help, String arg, String usage) {
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
