package github.kituin.chatimage.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import github.kituin.chatimage.config.ChatImageConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

import static github.kituin.chatimage.Chatimage.CONFIG;

public class ReloadConfig implements Command<CommandSourceStack> {
    public final static ReloadConfig instance = new ReloadConfig();

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        CONFIG = ChatImageConfig.loadConfig();
        context.getSource().sendSystemMessage(Component.translatable("success.reload.chatimage.command").setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)));
        return Command.SINGLE_SUCCESS;
    }


}
