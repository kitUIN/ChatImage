package github.kituin.chatimage.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.github.kituin.ChatImageCode.ChatImageConfig;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import static github.kituin.chatimage.ChatImage.CONFIG;

public class ReloadConfig implements Command<CommandSource> {
    public final static ReloadConfig instance = new ReloadConfig();

    @Override
    public int run(CommandContext<CommandSource> context) {
        CONFIG = ChatImageConfig.loadConfig();
        context.getSource().sendSuccess(new TranslationTextComponent("success.reload.chatimage.command").withStyle(TextFormatting.GREEN),false);
        return Command.SINGLE_SUCCESS;
    }


}
