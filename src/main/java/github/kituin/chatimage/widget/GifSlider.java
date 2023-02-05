package github.kituin.chatimage.widget;

import github.kituin.chatimage.config.ChatImageConfig;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.text.ScreenTexts;
import net.minecraft.text.Text;

import static github.kituin.chatimage.client.ChatImageClient.CONFIG;

/**
 * @author kitUIN
 */
public class GifSlider extends SettingSliderWidget {

    public GifSlider(int x, int y, int width, int height) {
        super(x, y, width, height, CONFIG.gifSpeed, 1, 20);
        this.updateMessage();
    }

    @Override
    protected void updateMessage() {
        this.setMessage(ScreenTexts.composeGenericOptionText(Text.translatable("gif.chatimage.gui"), Text.literal(String.valueOf(this.position))));
        CONFIG.gifSpeed = this.position;
        ChatImageConfig.saveConfig(CONFIG);
    }
	@Override
	protected void tooltip() {
		this.setTooltip(Tooltip.create(Text.translatable("gif.chatimage.tooltip")));
	}

}
