package io.github.kituin.chatimage.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;

import static io.github.kituin.chatimage.tool.SimpleUtil.createLiteralComponent;
import static io.github.kituin.chatimage.tool.SimpleUtil.createTranslatableComponent;
public class Help implements Command<#CommandSourceStack#> {
    @Override
    public int run(CommandContext<#CommandSourceStack#> context) {
        context.getSource().#sendSystemMessage#(
                getHelpText("/chatimage help", "", "help.chatimage.command")
                        .append(getHelpText("/chatimage send ", "<name> <url>", "send.chatimage.command"))
                        .append(getHelpText("/chatimage url ", "<url>", "url.chatimage.command"))
                        .append(getHelpText("/chatimage reload ", "", "reload.chatimage.command"))
// IF forge-1.16.5 || forge-1.18.2
//                , false
// END IF
        );
        return Command.SINGLE_SUCCESS;
    }
    public final static Help instance = new Help();


    private static #MutableComponent# getHelpText(String help, String arg, String usage) {
        String all = help + arg;
        StringBuilder sb = new StringBuilder(all);
        if (all.length() <= 35) {
            for (int i = 0; i < 35 - all.length(); i++) {
                sb.append(" ");
            }
        }
        return createLiteralComponent(sb.toString()).setStyle(
                #Style#.EMPTY.withColor(
                        #ChatFormatting#.GOLD
                ).withClickEvent(new #ClickEvent#(#ClickEvent#.Action.SUGGEST_COMMAND, help))
        ).append(createTranslatableComponent(usage)).append(createLiteralComponent("\n"));
    }

}