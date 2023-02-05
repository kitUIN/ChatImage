package github.kituin.chatimage.widget;

import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.quiltmc.loader.api.minecraft.ClientOnly;


@ClientOnly
public abstract class SettingSliderWidget extends SliderWidget {

	protected final double min;
	protected final double max;
	protected int position;

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

	protected abstract void tooltip();

	protected void removeTooltip() {
		this.setTooltip(null);
	}

	public void onClick(double mouseX, double mouseY) {
		super.onClick(mouseX, mouseY);
		this.removeTooltip();
	}

	public void onRelease(double mouseX, double mouseY) {
		super.onClick(mouseX, mouseY);
		this.tooltip();
	}
}
