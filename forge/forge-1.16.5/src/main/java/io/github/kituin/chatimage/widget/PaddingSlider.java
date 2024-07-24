package io.github.kituin.chatimage.widget;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
@OnlyIn(Dist.CLIENT)
public class PaddingSlider extends SettingSliderWidget {
    protected final ITextComponent title;
    protected final PaddingType paddingType;

    public PaddingSlider(int x, int y, int width, int height, ITextComponent title, int value, float max, PaddingType paddingType, SettingSliderWidget.OnTooltip tooltip) {
        super(x, y, width, height, title, StringTextComponent.EMPTY,
                value, 0F, max, tooltip,new PaddingSliderUpdate(paddingType));
        this.title = title;
        this.paddingType = paddingType;
    }



    public static ITextComponent tooltip(PaddingType paddingType) {
        switch (paddingType) {
            case TOP:
                return new TranslationTextComponent("top.padding.chatimage.tooltip");
            case BOTTOM:
                return new TranslationTextComponent("bottom.padding.chatimage.tooltip");
            case LEFT:
                return new TranslationTextComponent("left.padding.chatimage.tooltip");
            case RIGHT:
                return new TranslationTextComponent("right.padding.chatimage.tooltip");
        }
        return null;
    }

    public enum PaddingType {
        LEFT, RIGHT, TOP, BOTTOM

    }
}
