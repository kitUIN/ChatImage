package io.github.kituin.chatimage.tool;

import net.minecraft.client.Minecraft;
// IF forge-1.16.5
//import net.minecraft.client.gui.screen.Screen;
//import net.minecraft.util.text.*;
// ELSE
//import net.minecraft.network.chat.*;
//import net.minecraft.client.gui.screens.Screen;
// END IF
public class SimpleUtil {

    public static void setScreen(Minecraft client, Screen screen) {
        client.setScreen(screen);
    }

// IF forge-1.16.5
//    public static TextComponent createTranslatableComponent(String text) {
// ELSE
//    public static MutableComponent createTranslatableComponent(String text) {
// END IF
// IF forge-1.16.5
//        return new TranslationTextComponent(text);
// ELSE IF forge-1.18.2
//        return new TranslatableComponent(text);
// ELSE
//        return Component.translatable(text);
// END IF
}

// IF forge-1.16.5
//    public static TextComponent createTranslatableComponent(String key, Object... args) {
// ELSE
//    public static MutableComponent createTranslatableComponent(String key, Object... args) {
// END IF
// IF forge-1.16.5
//        return new TranslationTextComponent(key, args);
// ELSE IF forge-1.18.2
//        return new TranslatableComponent(key, args);
// ELSE
//        return Component.translatable(key, args);
// END IF
    }

// IF forge-1.16.5
//    public static TextComponent createLiteralComponent(String text) {
// ELSE
//    public static MutableComponent createLiteralComponent(String text) {
// END IF
// IF forge-1.16.5 || forge-1.18.2
//        return new StringTextComponent(text);
// ELSE IF forge-1.18.2
//        return new TextComponent(text);
// ELSE
//        return Component.literal(text);
// END IF
            }
// IF forge-1.16.5
//    public static TextComponent composeGenericOptionComponent(TextComponent text, TextComponent value) {
// ELSE
//    public static MutableComponent composeGenericOptionComponent(TextComponent text, TextComponent value) {
// END IF
// IF forge-1.16.5
//        return new TranslationTextComponent("options.generic_value", text, value);
// ELSE
//        return net.minecraft.network.chat.CommonComponents.optionNameValue(text, value);
// END IF
    }

}