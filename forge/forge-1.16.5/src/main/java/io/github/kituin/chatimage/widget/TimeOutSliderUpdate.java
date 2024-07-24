package io.github.kituin.chatimage.widget;

import io.github.kituin.ChatImageCode.ChatImageConfig;
import io.github.kituin.chatimage.ChatImage;
import net.minecraftforge.fml.client.gui.widget.Slider;

public class TimeOutSliderUpdate  implements Slider.ISlider {
    @Override
    public void onChangeSliderValue(Slider slider) {
        ChatImage.CONFIG.timeout = (int) (slider.sliderValue * (slider.maxValue - slider.minValue) + slider.minValue);
        ChatImageConfig.saveConfig(ChatImage.CONFIG);
    }
}