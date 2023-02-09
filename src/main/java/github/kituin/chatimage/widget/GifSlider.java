package github.kituin.chatimage.widget;

import github.kituin.chatimage.config.ChatImageConfig;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

import static github.kituin.chatimage.client.ChatImageClient.CONFIG;

/**
 * @author kitUIN
 */
public class GifSlider extends SettingSliderWidget {


    public GifSlider() {
        super(100, 100, 150, 20, CONFIG.gifSpeed, 1, 20);
        this.updateMessage();
        this.tip = Tooltip.of(Text.translatable("gif.chatimage.tooltip"));
        this.setTooltip(this.tip);
    }

    @Override
    protected void updateMessage() {
        this.setMessage(ScreenTexts.composeGenericOptionText(Text.translatable("gif.chatimage.gui"), Text.literal(String.valueOf(this.position))));
        CONFIG.gifSpeed = this.position;
        ChatImageConfig.saveConfig(CONFIG);
    }

}
