package io.github.kituin.chatimage.widget;

import io.github.kituin.chatimage.ChatImage;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TimeOutSlider extends SettingSliderWidget {


    public TimeOutSlider(int x, int y, int width, int height,SettingSliderWidget.OnTooltip tooltip) {
        super(x, y, width, height, new TranslationTextComponent("timeout.chatimage.gui"),
                new TranslationTextComponent("seconds.chatimage.gui"),
                ChatImage.CONFIG.timeout, 3, 60, tooltip,new TimeOutSliderUpdate());
    }


    public static ITextComponent tooltip() {
        return new TranslationTextComponent("timeout.chatimage.tooltip");
    }
}
