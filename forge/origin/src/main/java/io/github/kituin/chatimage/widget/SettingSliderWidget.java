package io.github.kituin.chatimage.widget;

// IF forge-1.16.5
//import net.minecraft.util.text.ITextComponent;
//import net.minecraft.client.Minecraft;
//import net.minecraft.util.math.MathHelper;
//import net.minecraft.client.gui.widget.AbstractSlider;
// ELSE
//import net.minecraft.util.Mth;
//import net.minecraft.network.chat.Component;
//import net.minecraft.client.gui.components.AbstractSliderButton;
// END IF
// IF forge-1.16.5
//import com.mojang.blaze3d.matrix.MatrixStack;
// ELSE IF forge-1.18.2
//import com.mojang.blaze3d.vertex.PoseStack;
// ELSE
//import net.minecraft.client.gui.GuiGraphics;
// END IF
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import java.util.function.Consumer;

import static io.github.kituin.chatimage.tool.SimpleUtil.createLiteralComponent;


@OnlyIn(Dist.CLIENT)
// IF forge-1.16.5
//public abstract class SettingSliderWidget extends AbstractSlider {
// ELSE
//public abstract class SettingSliderWidget extends AbstractSliderButton {
// END IF
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
// IF forge-1.16.5
//        this.value = ((MathHelper.clamp((float) value, min, max) - min) / (max - min));
// ELSE
//        this.value = ((Mth.clamp((float) value, min, max) - min) / (max - min));
// END IF
        this.applyValue();
        this.onTooltip = tooltip;
    }

    public void applyValue() {
// IF forge-1.16.5
//        this.position = (int) MathHelper.lerp(MathHelper.clamp(this.value, 0.0, 1.0), this.min, this.max);
// ELSE
//        this.position = (int) Mth.lerp(Mth.clamp(this.value, 0.0, 1.0), this.min, this.max);
// END IF
    }
// IF forge-1.16.5
//    public void renderToolTip(MatrixStack p_283427_, int p_281447_, int p_282852_) {
// ELSE IF  forge-1.18.2
//    public void renderToolTip(PoseStack p_283427_, int p_281447_, int p_282852_) {
// ELSE
//    public void renderToolTip(GuiGraphics p_283427_, int p_281447_, int p_282852_) {
// END IF
        if(this.canTooltip){
            this.onTooltip.onTooltip(this, p_283427_, p_281447_, p_282852_);
        }
    }
// IF forge-1.16.5
//    public void renderBg(MatrixStack p_283427_, Minecraft pMinecraft, int pMouseX, int pMouseY) {
//        super.renderBg(p_283427_, pMinecraft, pMouseX, pMouseY);
// ELSE IF  forge-1.18.2
//    public void renderBg(PoseStack p_283427_, Minecraft pMinecraft, int pMouseX, int pMouseY) {
//        super.renderBg(p_283427_, pMinecraft, pMouseX, pMouseY);
// ELSE
//    public void renderWidget(GuiGraphics p_283427_, int pMouseX, int pMouseY, float p_282409_) {
//        super.renderWidget(p_283427_, pMouseX, pMouseY, p_282409_);
// END IF
// IF forge-1.16.5
//        if (this.isHovered()) {
// ELSE
//            if (this.isHoveredOrFocused()) {
// END IF
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
// IF forge-1.16.5
//        void onTooltip(SettingSliderWidget p_93753_, MatrixStack p_93754_, int p_93755_, int p_93756_);
// ELSE IF  forge-1.18.2
//        void onTooltip(SettingSliderWidget p_93753_, PoseStack p_93754_, int p_93755_, int p_93756_);
// ELSE
//        void onTooltip(SettingSliderWidget p_93753_, GuiGraphics p_283427_, int p_281447_, int p_282852_);
// END IF

// IF forge-1.16.5
//        default void narrateTooltip(Consumer<ITextComponent> p_168842_) {
// ELSE
//            default void narrateTooltip(Consumer<Component> p_168842_) {
// END IF
        }
    }
}