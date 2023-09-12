package github.kituin.chatimage.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.ClickEvent;

public class Help implements Command<CommandSource> {
    public final static Help instance = new Help();

    @Override
    public int run(CommandContext<CommandSource> context) {
        context.getSource().sendFeedback(
                getHelpText("/chatimage help", "", "help.chatimage.command")
                        .appendSibling(getHelpText("/chatimage send ", "<name> <url>", "send.chatimage.command"))
                        .appendSibling(getHelpText("/chatimage url ", "<url>", "url.chatimage.command"))
                        .appendSibling(getHelpText("/chatimage reload ", "", "reload.chatimage.command"))
        ,false);
        return Command.SINGLE_SUCCESS;
    }
    private static StringTextComponent getHelpText(String help, String arg, String usage) {
        String all = help + arg;
        StringTextComponent text =  new StringTextComponent(all);
        TranslationTextComponent info = new TranslationTextComponent(usage);
        return (StringTextComponent) text.setStyle(Style.EMPTY.setFormatting(TextFormatting.GOLD).setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, help))).appendSibling(info).appendSibling(new StringTextComponent("\n"));
    }

}