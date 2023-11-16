package github.kituin.chatimage.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import github.kituin.chatimage.config.ChatImageConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;

import static github.kituin.chatimage.ChatImage.CONFIG;

public class ReloadConfig implements Command<CommandSourceStack> {
    public final static ReloadConfig instance = new ReloadConfig();

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        CONFIG = ChatImageConfig.loadConfig();
        context.getSource().sendSuccess(new TranslatableComponent("success.reload.chatimage.command").setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)),false);
        return Command.SINGLE_SUCCESS;
    }


}
