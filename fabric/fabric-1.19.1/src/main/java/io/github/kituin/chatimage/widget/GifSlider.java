package io.github.kituin.chatimage.widget;

import io.github.kituin.ChatImageCode.ChatImageConfig;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

import static io.github.kituin.chatimage.client.ChatImageClient.CONFIG;

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
        this.setMessage(ScreenTexts.composeGenericOptionText(Text.translatable("gif.chatimage.gui"), Text.literal(String.valueOf(this.position))));
        CONFIG.gifSpeed = this.position;
        ChatImageConfig.saveConfig(CONFIG);
    }

}
