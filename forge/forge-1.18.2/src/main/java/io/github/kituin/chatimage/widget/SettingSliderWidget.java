package io.github.kituin.chatimage.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Consumer;


@OnlyIn(Dist.CLIENT)
public abstract class SettingSliderWidget extends AbstractSliderButton {
    public static final OnTooltip NO_TOOLTIP = (p_93740_, p_93741_, p_93742_, p_93743_) -> {
    };
    protected final OnTooltip onTooltip;
    protected final double min;
    protected final double max;
    protected int position;
    protected boolean canTooltip = true;

    public SettingSliderWidget(int x, int y, int width, int height, int value, float min, float max) {
        this(x, y, width, height, value, min, max, NO_TOOLTIP);
    }

    public SettingSliderWidget(int x, int y, int width, int height, int value, float min, float max, OnTooltip tooltip) {
        super(x, y, width, height, new TextComponent(""), 0.0);
        this.min = min;
        this.max = max;
        this.value = ((Mth.clamp((float) value, min, max) - min) / (max - min));
        this.applyValue();
        this.onTooltip = tooltip;
    }

    public void applyValue() {
        this.position = (int) Mth.lerp(Mth.clamp(this.value, 0.0, 1.0), this.min, this.max);
    }

    public void renderToolTip(PoseStack p_93736_, int p_93737_, int p_93738_) {
        if(this.canTooltip){
            this.onTooltip.onTooltip(this, p_93736_, p_93737_, p_93738_);
        }
    }
    public void renderBg(PoseStack p_93600_, Minecraft p_93601_, int p_93602_, int p_93603_) {
        super.renderBg(p_93600_, p_93601_, p_93602_, p_93603_);
        if (this.isHoveredOrFocused()) {
            this.renderToolTip(p_93600_, p_93602_, p_93603_);
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
        void onTooltip(SettingSliderWidget p_93753_, PoseStack p_93754_, int p_93755_, int p_93756_);

        default void narrateTooltip(Consumer<Component> p_168842_) {
        }
    }
}