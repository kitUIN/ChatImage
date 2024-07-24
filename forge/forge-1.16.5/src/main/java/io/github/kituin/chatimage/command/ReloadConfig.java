package io.github.kituin.chatimage.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.github.kituin.ChatImageCode.ChatImageConfig;
import io.github.kituin.chatimage.ChatImage;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class ReloadConfig implements Command<CommandSource> {
    public final static ReloadConfig instance = new ReloadConfig();

    @Override
    public int run(CommandContext<CommandSource> context) {
        ChatImage.CONFIG = ChatImageConfig.loadConfig();
        context.getSource().sendSuccess(new TranslationTextComponent("success.reload.chatimage.command").withStyle(TextFormatting.GREEN),false);
        return Command.SINGLE_SUCCESS;
    }


}
