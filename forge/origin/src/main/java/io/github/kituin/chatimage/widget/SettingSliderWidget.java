package io.github.kituin.chatimage.widget;

// IF <= forge-1.19
//import net.minecraft.client.Minecraft;
// END IF
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import java.util.function.Consumer;

import static io.github.kituin.chatimage.tool.SimpleUtil.createLiteralComponent;


@OnlyIn(Dist.CLIENT)
public abstract class SettingSliderWidget extends #AbstractSliderButton# {
    public static final SettingSliderWidget.OnTooltip NO_TOOLTIP = (p_93740_, p_93741_, p_93742_, p_93743_) -> {
    };
    protected final SettingSliderWidget.OnTooltip onTooltip;
    protected final double min;
    protected final double max;
    protected int position;
    protected boolean canTooltip = true;

    public SettingSliderWidget(int x, int y, int width, int height, int value, float min, float max) {
        this(x, y, width, height, value, min, max, NO_TOOLTIP);
    }

    public SettingSliderWidget(int x, int y, int width, int height, int value, float min, float max, SettingSliderWidget.OnTooltip tooltip) {
        super(x, y, width, height, createLiteralComponent(""), 0.0);
        this.min = min;
        this.max = max;
        this.value = ((#Mth#.clamp((float) value, min, max) - min) / (max - min));
        this.applyValue();
        this.onTooltip = tooltip;
    }

    public void applyValue() {
        this.position = (int) #Mth#.lerp(#Mth#.clamp(this.value, 0.0, 1.0), this.min, this.max);
    }
    public void renderToolTip(#GuiGraphics# p_283427_, int p_281447_, int p_282852_) {
        if(this.canTooltip){
            this.onTooltip.onTooltip(this, p_283427_, p_281447_, p_282852_);
        }
    }
// IF <= forge-1.19
//    public void renderBg(#GuiGraphics# p_283427_, Minecraft pMinecraft, int pMouseX, int pMouseY) {
//        super.renderBg(p_283427_, pMinecraft, pMouseX, pMouseY);
// ELSE
//    public void renderWidget(#GuiGraphics# p_283427_, int pMouseX, int pMouseY, float p_282409_) {
//        super.renderWidget(p_283427_, pMouseX, pMouseY, p_282409_);
// END IF
        if (this.#isHoveredOrFocused#()) {
            this.renderToolTip(p_283427_, pMouseX, pMouseY);
        }
    }
    public void onClick(double p_93588_, double p_93589_) {
        super.onClick(p_93588_, p_93589_);
        this.canTooltip = false;
    }

    public void onRelease(double p_93609_, double p_93610_) {
        // super.onClick(p_93609_, p_93610_);
        this.canTooltip = true;
    }
    @OnlyIn(Dist.CLIENT)
    public interface OnTooltip {
        void onTooltip(SettingSliderWidget p_93753_, #GuiGraphics# p_283427_, int p_281447_, int p_282852_);
        
        default void narrateTooltip(Consumer<#Component#> p_168842_) {
        }
    }
}