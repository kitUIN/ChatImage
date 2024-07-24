package io.github.kituin.chatimage.widget;

import io.github.kituin.ChatImageCode.ChatImageConfig;
import io.github.kituin.chatimage.ChatImage;
import net.minecraftforge.fml.client.gui.widget.Slider;

public final class GifSliderUpdate implements Slider.ISlider {
    @Override
    public void onChangeSliderValue(Slider slider) {
        ChatImage.CONFIG.gifSpeed = (int) (slider.sliderValue * (slider.maxValue - slider.minValue) + slider.minValue);
        ChatImageConfig.saveConfig(ChatImage.CONFIG);
    }
}
