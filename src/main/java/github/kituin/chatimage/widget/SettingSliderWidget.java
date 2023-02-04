package github.kituin.chatimage.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.text.LiteralText;
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

    public SettingSliderWidget(int x, int y, int width, int height, int value, float min, float max, TooltipSupplier tooltipSupplier) {
        super(x, y, width, height, new LiteralText(""), 0.0);
        this.min = min;
        this.max = max;
        this.value = ((MathHelper.clamp((float) value, min, max) - min) / (max - min));
        this.applyValue();
        this.tooltipSupplier = tooltipSupplier;
    }

    public SettingSliderWidget(int x, int y, int width, int height, int value, float min, float max) {
        this(x, y, width, height, value, min, max, EMPTY);
    }

    public void renderTooltip(MatrixStack matrices, int mouseX, int mouseY) {
        this.tooltipSupplier.onTooltip(this, matrices, mouseX, mouseY);
    }

    public void applyValue() {
        this.position = (int) MathHelper.lerp(MathHelper.clamp(this.value, 0.0, 1.0), this.min, this.max);
    }

    @Environment(EnvType.CLIENT)
    public interface TooltipSupplier {
        void onTooltip(SettingSliderWidget button, MatrixStack matrices, int mouseX, int mouseY);

        default void supply(Consumer<Text> consumer) {
        }
    }
}