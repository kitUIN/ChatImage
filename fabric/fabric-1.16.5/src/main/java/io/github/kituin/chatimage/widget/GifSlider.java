package io.github.kituin.chatimage.widget;

import io.github.kituin.ChatImageCode.ChatImageConfig;
import io.github.kituin.chatimage.client.ChatImageClient;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

/**
 * @author kitUIN
 */
public class GifSlider extends SettingSliderWidget {

    public GifSlider(int x, int y, int width, int height, TooltipSupplier tooltipSupplier) {
        super(x, y, width, height, ChatImageClient.CONFIG.gifSpeed, 1, 20, tooltipSupplier);
        this.updateMessage();
    }

    @Override
    protected void updateMessage() {
        this.setMessage(composeGenericOptionText(new TranslatableText("gif.chatimage.gui"), new LiteralText(String.valueOf(this.position))));
        ChatImageClient.CONFIG.gifSpeed = this.position;
        ChatImageConfig.saveConfig(ChatImageClient.CONFIG);
    }

}
