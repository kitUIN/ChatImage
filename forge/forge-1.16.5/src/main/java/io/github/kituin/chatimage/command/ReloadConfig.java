package io.github.kituin.chatimage.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.github.kituin.ChatImageCode.ChatImageConfig;
// IF forge-1.16.5
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
// ELSE
//import com.mojang.brigadier.context.CommandContext;
//import net.minecraft.ChatFormatting;
//import net.minecraft.commands.CommandSourceStack;
//import net.minecraft.network.chat.Style;
// END IF

import static io.github.kituin.chatimage.ChatImage.CONFIG;
import static io.github.kituin.chatimage.tool.SimpleUtil.createTranslatableComponent;

// IF forge-1.16.5
public class ReloadConfig implements Command<CommandSource> {
    @Override
    public int run(CommandContext<CommandSource> context) {
// ELSE
//public class ReloadConfig implements Command<CommandSourceStack> {
//    @Override
//    public int run(CommandContext<CommandSourceStack> context) {
// END IF
        CONFIG = ChatImageConfig.loadConfig();
// IF forge-1.16.5 || forge-1.18.2
        context.getSource().sendSuccess(
// ELSE
//        context.getSource().sendSystemMessage(
// END IF
                createTranslatableComponent("success.reload.chatimage.command").setStyle(Style.EMPTY.withColor(
// IF forge-1.16.5
                        TextFormatting.GREEN
// ELSE
//                        ChatFormatting.GREEN
// END IF
                ))
// IF forge-1.16.5 || forge-1.18.2
                , false
// END IF
        );
        return Command.SINGLE_SUCCESS;
    }
    public final static ReloadConfig instance = new ReloadConfig();
}