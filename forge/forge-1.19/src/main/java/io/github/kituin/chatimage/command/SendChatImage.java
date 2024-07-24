package io.github.kituin.chatimage.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.github.kituin.ChatImageCode.ChatImageCode;
import io.github.kituin.ChatImageCode.ChatImageCodeInstance;
import net.minecraft.client.Minecraft;
// IF forge-1.16.5
//import net.minecraft.command.CommandSource;
// ELSE
import net.minecraft.commands.CommandSourceStack;
// END IF

import static io.github.kituin.ChatImageCode.ChatImageCodeInstance.LOGGER;
import static io.github.kituin.chatimage.tool.SimpleUtil.createLiteralComponent;

// IF forge-1.16.5
//public class SendChatImage implements Command<CommandSource> {
//    @Override
//    public int run(CommandContext<CommandSource> context) {
// ELSE
public class SendChatImage implements Command<CommandSourceStack> {
    @Override
    public int run(CommandContext<CommandSourceStack> context) {
// END IF
        String url = StringArgumentType.getString(context, "url");
        ChatImageCode.Builder builder = ChatImageCodeInstance.createBuilder().setUrlForce(url);
        try {
            String name = StringArgumentType.getString(context, "name");
            builder.setName(name);
        } catch (IllegalArgumentException e) {
            LOGGER.info("arg: `name` is omitted, use the default string");
        }
        if (Minecraft.getInstance().player != null) {
// IF forge-1.16.5 || forge-1.18.2
//            Minecraft.getInstance().player.chat(builder.build().toString());
// ELSE IF forge-1.19
            String str = builder.build().toString();
            Minecraft.getInstance().player.chatSigned(str, createLiteralComponent(str));
// ELSE
//            Minecraft.getInstance().player.connection.sendChat(builder.build().toString());
// END IF
        }
        return Command.SINGLE_SUCCESS;
    }

    public final static SendChatImage instance = new SendChatImage();

}