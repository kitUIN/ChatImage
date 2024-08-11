package io.github.kituin.chatimage.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.github.kituin.ChatImageCode.ChatImageConfig;


import static io.github.kituin.chatimage.ChatImage.CONFIG;
import static io.github.kituin.chatimage.tool.SimpleUtil.createTranslatableComponent;

public class ReloadConfig implements Command<CommandSourceStack> {
    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        CONFIG = ChatImageConfig.loadConfig();
        context.getSource().#sendSystemMessage#(
                createTranslatableComponent("success.reload.chatimage.command").setStyle(Style.EMPTY.withColor(
                        #ChatFormatting#.GREEN
                ))
// IF forge-1.16.5 || forge-1.18.2
//                , false
// END IF
        );
        return Command.SINGLE_SUCCESS;
    }
    public final static ReloadConfig instance = new ReloadConfig();
}