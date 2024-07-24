package io.github.kituin.chatimage.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
// IF forge-1.16.5
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
// ELSE
//import net.minecraft.ChatFormatting;
//import net.minecraft.commands.CommandSourceStack;
//import net.minecraft.network.chat.ClickEvent;
//import net.minecraft.network.chat.MutableComponent;
//import net.minecraft.network.chat.Style;
// END IF
import static io.github.kituin.chatimage.tool.SimpleUtil.createLiteralComponent;
import static io.github.kituin.chatimage.tool.SimpleUtil.createTranslatableComponent;
// IF forge-1.16.5
public class Help implements Command<CommandSource> {
    @Override
    public int run(CommandContext<CommandSource> context) {
// ELSE
//public class Help implements Command<CommandSourceStack> {
//    @Override
//    public int run(CommandContext<CommandSourceStack> context) {
// END IF
// IF forge-1.16.5 || forge-1.18.2
        context.getSource().sendSuccess(
// ELSE
//          context.getSource().sendSystemMessage(
// END IF
                getHelpText("/chatimage help", "", "help.chatimage.command")
                        .append(getHelpText("/chatimage send ", "<name> <url>", "send.chatimage.command"))
                        .append(getHelpText("/chatimage url ", "<url>", "url.chatimage.command"))
                        .append(getHelpText("/chatimage reload ", "", "reload.chatimage.command"))
// IF forge-1.16.5 || forge-1.18.2
                , false
// END IF
        );
        return Command.SINGLE_SUCCESS;
    }
    public final static Help instance = new Help();


// IF forge-1.16.5
    private static IFormattableTextComponent getHelpText(String help, String arg, String usage) {
// ELSE
//    private static MutableComponent getHelpText(String help, String arg, String usage) {
// END IF
        String all = help + arg;
        StringBuilder sb = new StringBuilder(all);
        if (all.length() <= 35) {
            for (int i = 0; i < 35 - all.length(); i++) {
                sb.append(" ");
            }
        }
        return createLiteralComponent(sb.toString()).setStyle(
                Style.EMPTY.withColor(
// IF forge-1.16.5
                        TextFormatting.GOLD
// ELSE
//                        ChatFormatting.GOLD
// END IF
                ).withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, help))
        ).append(createTranslatableComponent(usage)).append(createLiteralComponent("\n"));
    }

}