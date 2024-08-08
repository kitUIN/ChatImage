package io.github.kituin.chatimage.widget;

import io.github.kituin.ChatImageCode.ChatImageConfig;
// IF forge-1.16.5
//import net.minecraft.util.text.ITextComponent;
//import net.minecraftforge.fml.client.gui.widget.Slider;
// ELSE
//import net.minecraft.network.chat.Component;
// END IF
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static io.github.kituin.chatimage.ChatImage.CONFIG;
import static io.github.kituin.chatimage.tool.SimpleUtil.*;

@OnlyIn(Dist.CLIENT)
public class TimeOutSlider extends SettingSliderWidget {

    public TimeOutSlider(int x, int y, int width, int height, SettingSliderWidget.OnTooltip tooltip) {
        super(x, y, width, height, CONFIG.timeout, 3, 60, tooltip);
        this.updateMessage();
    }

    @Override
    protected void updateMessage() {
        this.setMessage(composeGenericOptionComponent(createTranslatableComponent("timeout.chatimage.gui"),
                createLiteralComponent(String.valueOf(this.position)))
                .append(" ")
                .append(createTranslatableComponent("seconds.chatimage.gui")));
        CONFIG.timeout = this.position;
        ChatImageConfig.saveConfig(CONFIG);
    }

// IF forge-1.16.5
//    public static ITextComponent tooltip() {
// ELSE
//    public static Component tooltip() {
// END IF

        return createTranslatableComponent("timeout.chatimage.tooltip");
    }
}