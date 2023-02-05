package github.kituin.chatimage.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


@OnlyIn(Dist.CLIENT)
public abstract class SettingSliderWidget extends AbstractSliderButton {
    protected final double min;
    protected final double max;
    protected int position;

    public SettingSliderWidget(int x, int y, int width, int height, int value, float min, float max) {
        super(x, y, width, height, CommonComponents.EMPTY, 0.0);
        this.min = min;
        this.max = max;
        this.value = ((Mth.clamp((float) value, min, max) - min) / (max - min));
        this.applyValue();

    }

    public void applyValue() {
        this.position = (int) Mth.lerp(Mth.clamp(this.value, 0.0, 1.0), this.min, this.max);
    }

    protected abstract void tooltip();
    protected void removeTooltip(){
        this.setTooltip(null);
    }
    public void onClick(double p_93588_, double p_93589_) {
        super.onClick(p_93588_,p_93589_);
        this.removeTooltip();
    }
    public void onRelease(double p_93609_, double p_93610_) {
        super.onClick(p_93609_,  p_93610_);
        this.tooltip();
    }
}