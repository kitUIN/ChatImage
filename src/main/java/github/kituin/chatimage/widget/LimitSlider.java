package github.kituin.chatimage.widget;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


@OnlyIn(Dist.CLIENT)
public class LimitSlider extends SettingSliderWidget {
    protected final LimitType limitType;

    public LimitSlider(int x, int y, int width, int height, ITextComponent title, int value, float max, LimitType limitType, SettingSliderWidget.OnTooltip tooltip) {
        super(x, y, width, height, title,
                StringTextComponent.EMPTY,
                value, 1F, max, tooltip,new LimitSliderUpdate(limitType));
        this.limitType = limitType;
    }


    public static ITextComponent tooltip(LimitType limitType) {
        switch (limitType) {
            case WIDTH:
                return new TranslationTextComponent("width.limit.chatimage.tooltip");
            case HEIGHT:
                return new TranslationTextComponent("height.limit.chatimage.tooltip");
        }
        return null;
    }

    public enum LimitType {
        WIDTH, HEIGHT
    }
}