package io.github.kituin.chatimage.tool;


public class SimpleUtil {

    public static #MutableComponent# createTranslatableComponent(String text) {
        return #Component#.translatable(text);
    }

    public static #MutableComponent# createTranslatableComponent(String key, Object... args) {
        return #Component#.translatable(key, args);
    }

    public static #MutableComponent# createLiteralComponent(String text) {
        return #Component#.literal(text);
    }

    public static #MutableComponent# composeGenericOptionComponent(#Component# text, #Component# value) {
        return net.minecraft.network.chat.CommonComponents.optionNameValue(text, value);
    }
}