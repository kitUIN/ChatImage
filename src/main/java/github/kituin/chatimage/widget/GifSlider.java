package github.kituin.chatimage.widget;

import github.kituin.chatimage.config.ChatImageConfig;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

import static github.kituin.chatimage.client.ChatImageClient.CONFIG;

/**
 * @author kitUIN
 */
public class GifSlider extends SettingSliderWidget {

    public GifSlider(int x, int y, int width, int height, TooltipSupplier tooltipSupplier) {
        super(x, y, width, height, CONFIG.gifSpeed, 1, 20, tooltipSupplier);
        this.updateMessage();
    }

    @Override
    protected void updateMessage() {
        this.setMessage(composeGenericOptionText(new TranslatableText("gif.chatimage.gui"), new LiteralText(String.valueOf(this.position))));
        CONFIG.gifSpeed = this.position;
        ChatImageConfig.saveConfig(CONFIG);
    }

}
