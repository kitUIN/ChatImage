package io.github.kituin.chatimage.widget;

import io.github.kituin.chatimage.ChatImage;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author kitUIN
 */
@OnlyIn(Dist.CLIENT)
public class GifSlider extends SettingSliderWidget {

    public GifSlider(int x, int y, int width, int height, SettingSliderWidget.OnTooltip tooltip) {
        super(x, y, width, height, new TranslationTextComponent("gif.chatimage.gui"),
                StringTextComponent.EMPTY,
                ChatImage.CONFIG.gifSpeed, 1, 20, tooltip, new GifSliderUpdate());
    }

    public static ITextComponent tooltip() {
        return new TranslationTextComponent("gif.chatimage.tooltip");
    }

}
