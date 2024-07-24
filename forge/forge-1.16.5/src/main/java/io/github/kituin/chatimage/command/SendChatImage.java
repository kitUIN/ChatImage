package io.github.kituin.chatimage.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.github.kituin.ChatImageCode.ChatImageCode;
import io.github.kituin.ChatImageCode.ChatImageCodeInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SendChatImage implements Command<CommandSource> {
    public static final Logger LOGGER = LogManager.getLogger();
    public final static SendChatImage instance = new SendChatImage();

    @Override
    public int run(CommandContext<CommandSource> context) {
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
