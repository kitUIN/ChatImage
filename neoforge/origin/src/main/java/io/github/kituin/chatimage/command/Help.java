package io.github.kituin.chatimage.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import #ClickEvent#;
import #Component#;
import #MutableComponent#;
import #Style#;

public class Help implements Command<CommandSourceStack> {
    public final static Help instance = new Help();

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        context.getSource().sendSystemMessage(
                getHelpText("/chatimage help", "", "help.chatimage.command")
                        .append(getHelpText("/chatimage send ", "<name> <url>", "send.chatimage.command"))
                        .append(getHelpText("/chatimage url ", "<url>", "url.chatimage.command"))
                        .append(getHelpText("/chatimage reload ", "", "reload.chatimage.command"))
        );
        return Command.SINGLE_SUCCESS;
    }

    private static MutableComponent getHelpText(String help, String arg, String usage) {
        String all = help + arg;
        StringBuilder sb = new StringBuilder(all);
        if (all.length() <= 30) {
            for (int i = 0; i < 30 - all.length(); i++) {
                sb.append(" ");
            }
        }
        MutableComponent text = Component.literal(sb.toString());
        MutableComponent info = Component.translatable(usage);
        return text.setStyle(Style.EMPTY.withColor(ChatFormatting.GOLD).withClickEvent(
// IF >= neoforge-1.21.5
//                new ClickEvent.SuggestCommand(
// ELSE
//             new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
// END IF

                        help)
        )).append(info).append(Component.literal("\n"));
    }

}