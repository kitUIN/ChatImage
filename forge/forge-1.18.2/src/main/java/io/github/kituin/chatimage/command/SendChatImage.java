package io.github.kituin.chatimage.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.github.kituin.ChatImageCode.ChatImageCode;
import io.github.kituin.ChatImageCode.ChatImageCodeInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;

import static io.github.kituin.ChatImageCode.ChatImageCodeInstance.LOGGER;

public class SendChatImage implements Command<CommandSourceStack> {
    public final static SendChatImage instance = new SendChatImage();

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        String url = StringArgumentType.getString(context, "url");
        ChatImageCode.Builder builder = ChatImageCodeInstance.createBuilder().setUrlForce(url);
        try {
            String name = StringArgumentType.getString(context, "name");
            builder.setName(name);
        } catch (IllegalArgumentException e) {
            LOGGER.info("arg: `name` is omitted, use the default string");
        }
        if (Minecraft.getInstance().player != null) {
            Minecraft.getInstance().player.chat(builder.build().toString());
        }
        return Command.SINGLE_SUCCESS;
    }


}
