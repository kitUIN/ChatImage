package io.github.kituin.chatimage.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.util.function.Consumer;


@Environment(EnvType.CLIENT)
public abstract class SettingSliderWidget extends SliderWidget {
    public static final TooltipSupplier EMPTY = (button, matrices, mouseX, mouseY) -> {
    };
    protected final double min;
    protected final double max;
    protected int position;
    protected final TooltipSupplier tooltipSupplier;
    protected boolean isClick = false;

    public SettingSliderWidget(int x, int y, int width, int height, int value, float min, float max, TooltipSupplier tooltipSupplier) {
        super(x, y, width, height, ScreenTexts.EMPTY, 0.0);
        this.min = min;
        this.max = max;
        this.value = ((MathHelper.clamp((float) value, min, max) - min) / (max - min));
        this.applyValue();
        this.tooltipSupplier = tooltipSupplier;
    }

    public void renderTooltip(MatrixStack matrices, int mouseX, int mouseY) {
        this.tooltipSupplier.onTooltip(this, matrices, mouseX, mouseY);
    }

    public void applyValue() {
        this.position = (int) MathHelper.lerp(MathHelper.clamp(this.value, 0.0, 1.0), this.min, this.max);
    }
    @Override
    protected void renderBackground(MatrixStack matrices, MinecraftClient client, int mouseX, int mouseY) {
        super.renderBackground(matrices, client, mouseX, mouseY);
        if (this.isHovered() && !this.isClick) {
            this.renderTooltip(matrices, mouseX, mouseY);
        }
    }

    @Environment(EnvType.CLIENT)
    public interface TooltipSupplier {
        void onTooltip(SettingSliderWidget button, MatrixStack matrices, int mouseX, int mouseY);

        default void supply(Consumer<Text> consumer) {
        }
    }
    @Override
    public void onClick(double mouseX, double mouseY) {
        super.onClick(mouseX, mouseY);
        this.isClick = true;
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        super.onRelease(mouseX, mouseY);
        this.isClick = false;
    }
}