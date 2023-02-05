package github.kituin.chatimage.widget;

import github.kituin.chatimage.config.ChatImageConfig;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.text.ScreenTexts;
import net.minecraft.text.Text;

import static github.kituin.chatimage.client.ChatImageClient.CONFIG;

public class LimitSlider extends SettingSliderWidget {
    protected final Text title;
    protected final LimitType limitType;

    public LimitSlider(int x, int y, int width, int height, Text title, int value, float max, LimitType limitType) {
        super(x, y, width, height, value, 0F, max);
        this.title = title;
        this.limitType = limitType;
        this.updateMessage();
    }

    @Override
    protected void updateMessage() {
        switch (limitType) {
            case WIDTH:
                this.setMessage(ScreenTexts.composeGenericOptionText(title, CONFIG.limitWidth == 0 ? Text.translatable("default.chatimage.gui") : Text.literal(String.valueOf(this.position))));
                CONFIG.limitWidth = this.position;
                break;
            case HEIGHT:
                this.setMessage(ScreenTexts.composeGenericOptionText(title, CONFIG.limitHeight == 0 ? Text.translatable("default.chatimage.gui") : Text.literal(String.valueOf(this.position))));
                CONFIG.limitHeight = this.position;
                break;
            default:
                return;
        }
        ChatImageConfig.saveConfig(CONFIG);
    }
	public void tooltip() {
		Text text;
		switch (limitType) {
			case WIDTH:
				text = Text.translatable("width.limit.chatimage.tooltip");
				break;
			case HEIGHT:
				text = Text.translatable("height.limit.chatimage.tooltip");
				break;
			default:
				return;
		}
		this.setTooltip(Tooltip.create(text));
	}

    public enum LimitType {
        WIDTH, HEIGHT

    }
}
