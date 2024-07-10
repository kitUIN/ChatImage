package io.github.kituin.chatimage.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.util.math.MathHelper;


@Environment(EnvType.CLIENT)
public abstract class SettingSliderWidget extends SliderWidget {
    protected final double min;
    protected final double max;
    protected int position;
    protected Tooltip tip;
    public SettingSliderWidget(int x, int y, int width, int height, int value, float min, float max) {
        super(x, y, width, height, ScreenTexts.EMPTY, 0.0);
        this.min = min;
        this.max = max;
        this.value = ((MathHelper.clamp((float) value, min, max) - min) / (max - min));
        this.applyValue();
    }

    public void applyValue() {
        this.position = (int) MathHelper.lerp(MathHelper.clamp(this.value, 0.0, 1.0), this.min, this.max);
    }
    @Override
    public void onClick(double mouseX, double mouseY) {
        super.onClick(mouseX, mouseY);
        this.setTooltip(null);
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        super.onRelease(mouseX, mouseY);
        this.setTooltip(tip);
    }
}