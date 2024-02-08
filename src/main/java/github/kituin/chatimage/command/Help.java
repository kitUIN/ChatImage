package github.kituin.chatimage.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.ClickEvent;

public class Help implements Command<CommandSource> {
    public final static Help instance = new Help();

    @Override
    public int run(CommandContext<CommandSource> context) {
        context.getSource().sendSuccess(
                getHelpText("/chatimage help", "", "help.chatimage.command")
                        .append(getHelpText("/chatimage send ", "<name> <url>", "send.chatimage.command"))
                        .append(getHelpText("/chatimage url ", "<url>", "url.chatimage.command"))
                        .append(getHelpText("/chatimage reload ", "", "reload.chatimage.command"))
        ,false);
        return Command.SINGLE_SUCCESS;
    }
    private static StringTextComponent getHelpText(String help, String arg, String usage) {
        String all = help + arg;
        StringTextComponent text =  new StringTextComponent(all);
        TranslationTextComponent info = new TranslationTextComponent(usage);
        return (StringTextComponent) text.setStyle(Style.EMPTY.withColor(TextFormatting.GOLD).withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, help))).append(info).append(new StringTextComponent("\n"));
    }

}