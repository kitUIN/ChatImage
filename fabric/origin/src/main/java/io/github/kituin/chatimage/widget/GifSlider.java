package io.github.kituin.chatimage.widget;

import io.github.kituin.ChatImageCode.ChatImageConfig;
import io.github.kituin.chatimage.client.ChatImageClient;

import static io.github.kituin.chatimage.tool.SimpleUtil.*;

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
        this.setMessage(composeGenericOptionText(createTranslatableText("gif.chatimage.gui"), createLiteralText(String.valueOf(this.position))));
        ChatImageClient.CONFIG.gifSpeed = this.position;
        ChatImageConfig.saveConfig(ChatImageClient.CONFIG);
    }

}