package io.github.kituin.chatimage.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.github.kituin.ChatImageCode.ChatImageConfig;
import io.github.kituin.chatimage.ChatImage;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;

public class ReloadConfig implements Command<CommandSourceStack> {
    public final static ReloadConfig instance = new ReloadConfig();

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        ChatImage.CONFIG = ChatImageConfig.loadConfig();
        context.getSource().sendSuccess(new TranslatableComponent("success.reload.chatimage.command").setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)),false);
        return Command.SINGLE_SUCCESS;
    }


}
