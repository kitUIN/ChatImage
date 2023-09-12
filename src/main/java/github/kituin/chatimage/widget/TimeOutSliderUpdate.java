package github.kituin.chatimage.widget;

import github.kituin.chatimage.config.ChatImageConfig;
import net.minecraftforge.fml.client.gui.widget.Slider;

import static github.kituin.chatimage.ChatImage.CONFIG;

public class TimeOutSliderUpdate  implements Slider.ISlider {
    @Override
    public void onChangeSliderValue(Slider slider) {
        CONFIG.timeout = (int) (slider.sliderValue * (slider.maxValue - slider.minValue) + slider.minValue);
        ChatImageConfig.saveConfig(CONFIG);
    }
}