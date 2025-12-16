package io.github.kituin.chatimage.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.widget.SliderWidget;

import net.minecraft.util.math.MathHelper;
// IF < fabric-1.19.3
//import net.minecraft.client.MinecraftClient;
//import #MatrixStack#;
//import #Component#;
//import java.util.function.Consumer;
// ELSE
//import net.minecraft.client.gui.tooltip.Tooltip;
// END IF
import static io.github.kituin.chatimage.tool.SimpleUtil.createLiteralComponent;


@Environment(EnvType.CLIENT)
public abstract class SettingSliderWidget extends SliderWidget {
    protected final double min;
    protected final double max;
    protected int position;

// IF < fabric-1.19.3
//    public static final TooltipSupplier EMPTY = (button, matrices, mouseX, mouseY) -> {
//    };
//    protected final TooltipSupplier tooltipSupplier;
//    protected boolean isClick = false;
//    public SettingSliderWidget(int x, int y, int width, int height, int value, float min, float max, TooltipSupplier tooltipSupplier) {
//        super(x, y, width, height, createLiteralComponent(""), 0.0);
//        this.min = min;
//        this.max = max;
//        this.value = ((MathHelper.clamp((float) value, min, max) - min) / (max - min));
//        this.applyValue();
//        this.tooltipSupplier = tooltipSupplier;
//    }
//    public SettingSliderWidget(int x, int y, int width, int height, int value, float min, float max) {
//        this(x, y, width, height, value, min, max, EMPTY);
//    }
//
//    public void renderTooltip(MatrixStack matrices, int mouseX, int mouseY) {
//        this.tooltipSupplier.onTooltip(this, matrices, mouseX, mouseY);
//    }
// ELSE
//    protected Tooltip tip;
//    public SettingSliderWidget(int x, int y, int width, int height, int value, float min, float max) {
//        super(x, y, width, height, createLiteralComponent(""), 0.0);
//        this.min = min;
//        this.max = max;
//        this.value = ((MathHelper.clamp((float) value, min, max) - min) / (max - min));
//        this.applyValue();
//    }
// END IF

    public void applyValue() {
        this.position = (int) MathHelper.lerp(MathHelper.clamp(this.value, 0.0, 1.0), this.min, this.max);
    }

// IF < fabric-1.19.3
//    @Override
//    protected void renderBackground(MatrixStack matrices, MinecraftClient client, int mouseX, int mouseY) {
//        super.renderBackground(matrices, client, mouseX, mouseY);
//        if (this.isHovered() && !this.isClick) {
//            this.renderTooltip(matrices, mouseX, mouseY);
//        }
//    }
//    @Environment(EnvType.CLIENT)
//    public interface TooltipSupplier {
//        void onTooltip(SettingSliderWidget button, MatrixStack matrices, int mouseX, int mouseY);
//
//        default void supply(Consumer<Text> consumer) {
//        }
//    }
// END IF


// IF >= fabric-1.21.9
//    @Override
//    public void onClick(net.minecraft.client.gui.Click click, boolean doubled) {
//        super.onClick(click, doubled);
//        this.setTooltip(null);
//    }
//    @Override
//    public void onRelease(net.minecraft.client.gui.Click click) {
//        super.onRelease(click);
//        this.setTooltip(tip);
//    }
// ELSE
//     @Override
//     public void onClick(double mouseX, double mouseY) {
//         super.onClick(mouseX, mouseY);
// IF < fabric-1.19.3
//        this.isClick = false;
// ELSE
//        this.setTooltip(null);
// END IF
//     }
//
//     @Override
//     public void onRelease(double mouseX, double mouseY) {
//         super.onRelease(mouseX, mouseY);
// IF < fabric-1.19.3
//        this.isClick = true;
// ELSE
//         this.setTooltip(tip);
// END IF
//     }
// END IF
}