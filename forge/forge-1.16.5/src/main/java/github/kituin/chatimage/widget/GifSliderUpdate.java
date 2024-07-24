package github.kituin.chatimage.widget;

import io.github.kituin.ChatImageCode.ChatImageConfig;
import net.minecraftforge.fml.client.gui.widget.Slider;

import static github.kituin.chatimage.ChatImage.CONFIG;

public final class GifSliderUpdate implements Slider.ISlider {
    @Override
    public void onChangeSliderValue(Slider slider) {
        CONFIG.gifSpeed = (int) (slider.sliderValue * (slider.maxValue - slider.minValue) + slider.minValue);
        ChatImageConfig.saveConfig(CONFIG);
    }
}
