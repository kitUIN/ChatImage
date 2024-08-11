package io.github.kituin.chatimage.tool;

// IF forge-1.16.5
//import net.minecraft.util.text.*;
// ELSE IF forge-1.18.2
import net.minecraft.network.chat.*;
// END IF

public class SimpleUtil {

    public static #MutableComponent# createTranslatableComponent(String text) {
// IF forge-1.16.5
//        return new TranslationTextComponent(text);
// ELSE IF forge-1.18.2
//        return new TranslatableComponent(text);
// ELSE
//        return Component.translatable(text);
// END IF
}

    public static #MutableComponent# createTranslatableComponent(String key, Object... args) {
// IF forge-1.16.5
//        return new TranslationTextComponent(key, args);
// ELSE IF forge-1.18.2
//        return new TranslatableComponent(key, args);
// ELSE
//        return Component.translatable(key, args);
// END IF
    }

    public static #MutableComponent# createLiteralComponent(String text) {
// IF forge-1.16.5 || forge-1.18.2
//        return new StringTextComponent(text);
// ELSE IF forge-1.18.2
//        return new TextComponent(text);
// ELSE
//        return Component.literal(text);
// END IF
            }
    public static #MutableComponent# composeGenericOptionComponent(#Component# text, #Component# value) {
// IF forge-1.16.5
//        return new TranslationTextComponent("options.generic_value", text, value);
// ELSE
//        return net.minecraft.network.chat.CommonComponents.optionNameValue(text, value);
// END IF
    }
}