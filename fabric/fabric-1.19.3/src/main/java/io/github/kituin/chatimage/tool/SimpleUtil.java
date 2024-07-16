package io.github.kituin.chatimage.tool;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.*;

public class SimpleUtil {

    public static void setScreen(MinecraftClient client, Screen screen) {
// IF fabric-1.16.5
//        client.openScreen(screen);
// ELSE
        client.setScreen(screen);
// END IF
    }
    public static MutableText createTranslatableText(String text){
// IF fabric-1.16.5 || fabric-1.18.2
//        return new TranslatableText(text);
// ELSE
        return Text.translatable(text);
// END IF
    }
    

    public static MutableText createLiteralText(String text){
// IF fabric-1.16.5 || fabric-1.18.2
//        return new LiteralText(text);
// ELSE
        return Text.literal(text);
// END IF
    }
    public static MutableText composeGenericOptionText(Text text, Text value) {
// IF fabric-1.16.5
//        return new TranslatableText("options.generic_value", text, value);
// ELSE IF fabric-1.18.2
//        return net.minecraft.client.gui.screen.ScreenTexts.composeGenericOptionText(text,value);
// ELSE
        return net.minecraft.screen.ScreenTexts.composeGenericOptionText(text,value);
// END IF
    }

}