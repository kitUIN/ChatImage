package github.kituin.chatimage.command;

import com.github.chatimagecode.ChatImageCode;
import com.github.chatimagecode.exception.InvalidChatImageUrlException;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

public class SendChatImage implements Command<CommandSourceStack> {
    public final static SendChatImage instance = new SendChatImage();

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        String name = null;
        String url = StringArgumentType.getString(context, "url");
        try {
            name = StringArgumentType.getString(context, "name");
        } catch (IllegalArgumentException e) {
            LogUtils.getLogger().info("arg: `name` is omitted, use the default string");
        }
        try {
            ChatImageCode code = new ChatImageCode(url, name);
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.connection.sendChat((code.toString()));
            }
        } catch (InvalidChatImageUrlException e) {
            MutableComponent text = Component.literal(e.getMode().toString() + ": " + e.getMessage());
            context.getSource().sendSystemMessage(text.setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
        }
        return Command.SINGLE_SUCCESS;
    }


}
