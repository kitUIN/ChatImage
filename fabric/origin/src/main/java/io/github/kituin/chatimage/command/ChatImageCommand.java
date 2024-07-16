package io.github.kituin.chatimage.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.github.kituin.ChatImageCode.ChatImageCode;
import io.github.kituin.ChatImageCode.ChatImageCodeInstance;
import io.github.kituin.ChatImageCode.ChatImageConfig;
// IF fabric-1.16.5 || fabric-1.18.2
//import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
// ELSE
//import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
// END IF
import net.minecraft.text.*;
import net.minecraft.util.Formatting;

import static io.github.kituin.ChatImageCode.ChatImageCodeInstance.LOGGER;
import static io.github.kituin.chatimage.client.ChatImageClient.CONFIG;
import static io.github.kituin.chatimage.tool.SimpleUtil.createTranslatableText;

public class ChatImageCommand {
    public static int sendChatImage(CommandContext<FabricClientCommandSource> context) {
        String url = StringArgumentType.getString(context, "url");
        ChatImageCode.Builder builder = ChatImageCodeInstance.createBuilder().setUrlForce(url);
        try {
            String name = StringArgumentType.getString(context, "name");
            builder.setName(name);
        } catch (java.lang.IllegalArgumentException e) {
            LOGGER.info("arg: `name` is omitted, use the default string");
        }
// IF fabric-1.16.5 || fabric-1.18.2
//        context.getSource().getPlayer().sendChatMessage(builder.build().toString());
// ELSE
//        context.getSource().getPlayer().networkHandler.sendChatMessage(builder.build().toString());
// END IF
        return Command.SINGLE_SUCCESS;
    }

    public static int help(CommandContext<FabricClientCommandSource> context) {

        context.getSource().sendFeedback(
                getHelpText("/chatimage help", "", "help.chatimage.command")
                        .append(getHelpText("/chatimage send ", "<name> <url>", "send.chatimage.command"))
                        .append(getHelpText("/chatimage url ", "<url>", "url.chatimage.command"))
                        .append(getHelpText("/chatimage reload ", "", "reload.chatimage.command"))
        );
        return Command.SINGLE_SUCCESS;
    }

    public static int reloadConfig(CommandContext<FabricClientCommandSource> context) {
        CONFIG = ChatImageConfig.loadConfig();
        context.getSource().sendFeedback(createTranslatableText("success.reload.chatimage.command").setStyle(Style.EMPTY.withColor(Formatting.GREEN)));
        return Command.SINGLE_SUCCESS;
    }

    private static MutableText getHelpText(String help, String arg, String usage) {
        String all = help + arg;
        StringBuilder sb = new StringBuilder(all);
        if (all.length() <= 35) {
            for (int i = 0; i < 35 - all.length(); i++) {
                sb.append(" ");
            }
        }
        MutableText text = (MutableText) Text.of(sb.toString());
// IF fabric-1.16.5 || fabric-1.18.2
//        MutableText info = new TranslatableText(usage);
// ELSE
//        MutableText info = Text.translatable(usage);
// END IF
        return text.setStyle(Style.EMPTY.withColor(Formatting.GOLD).withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, help))).append(info).append(Text.of("\n"));
    }

}