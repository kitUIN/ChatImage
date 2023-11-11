package github.kituin.chatimage.command;

import com.github.chatimagecode.ChatImageCode;
import com.github.chatimagecode.exception.InvalidChatImageUrlException;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.ServerPlayer;

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
                //{@link ChatScreen#handleChatInput}
                Minecraft.getInstance().player.chat(code.toString());
            }
        } catch (InvalidChatImageUrlException e) {
            MutableComponent text = new TextComponent(e.getMode().toString() + ": " + e.getMessage());
            context.getSource().sendFailure(text.setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
        }
        return Command.SINGLE_SUCCESS;
    }


}
