package github.kituin.chatimage;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import github.kituin.chatimage.commands.ChatImageCommand;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.server.command.ServerCommandSource;


import java.util.concurrent.CompletableFuture;

import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static com.mojang.brigadier.builder.RequiredArgumentBuilder.argument;
import static net.fabricmc.fabric.api.message.v1.ServerMessageEvents.CHAT_MESSAGE;


/**
 * @author kitUIN
 */
public class ChatImage implements ModInitializer {

    @Override
    public void onInitialize() {

        CHAT_MESSAGE.register((message, sender, params)->{
            System.out.println(message.getSignedContent().describeConstable());




        });

    }

}
