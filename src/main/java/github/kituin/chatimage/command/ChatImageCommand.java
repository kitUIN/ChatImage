package github.kituin.chatimage.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.logging.LogUtils;
import github.kituin.chatimage.config.ChatImageConfig;
import github.kituin.chatimage.exception.InvalidChatImageUrlException;
import github.kituin.chatimage.tool.ChatImageCode;

import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.quiltmc.qsl.command.api.client.QuiltClientCommandSource;

import static github.kituin.chatimage.client.ChatImageClient.CONFIG;

public class ChatImageCommand {


    public static int sendChatImage(CommandContext<QuiltClientCommandSource> context) {
        String name = null;
        String url = StringArgumentType.getString(context, "url");
        try {
            name = StringArgumentType.getString(context, "name");
        } catch (IllegalArgumentException e) {
            LogUtils.getLogger().info("arg: `name` is omitted, use the default string");
        }
        try {
            ChatImageCode code = new ChatImageCode(url, name);
            context.getSource().getPlayer().sendMessage(Text.of(code.toString()),false);
        } catch (InvalidChatImageUrlException e) {
            MutableText text = Text.literal(e.getMode().toString() + ": " + e.getMessage());
            context.getSource().sendFeedback(text.setStyle(Style.EMPTY.withColor(Formatting.RED)));
        }

        return Command.SINGLE_SUCCESS;
    }

    public static int help(CommandContext<QuiltClientCommandSource> context) {

        context.getSource().sendFeedback(
                getHelpText("/chatimage help", "", "help.chatimage.command")
                        .append(getHelpText("/chatimage send ", "<name> <url>", "send.chatimage.command"))
                        .append(getHelpText("/chatimage url ", "<url>", "url.chatimage.command"))
                        .append(getHelpText("/chatimage reload ", "", "reload.chatimage.command"))
        );
        return Command.SINGLE_SUCCESS;
    }

    public static int reloadConfig(CommandContext<QuiltClientCommandSource> context) {
        CONFIG = ChatImageConfig.loadConfig();
        context.getSource().sendFeedback(Text.translatable("success.reload.chatimage.command").setStyle(Style.EMPTY.withColor(Formatting.GREEN)));
        return Command.SINGLE_SUCCESS;
    }

    private static MutableText getHelpText(String help, String arg, String usage) {
        String all = help + arg;
        StringBuilder sb = new StringBuilder(all);
        if (all.length() <= 35) {
			sb.append(" ".repeat(35 - all.length()));
        }
        MutableText text = (MutableText) Text.of(sb.toString());
        MutableText info = Text.translatable(usage);
        return text.setStyle(Style.EMPTY.withColor(Formatting.GOLD).withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, help))).append(info).append(Text.of("\n"));
    }

}
