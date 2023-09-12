package github.kituin.chatimage.widget;

import github.kituin.chatimage.config.ChatImageConfig;
import net.minecraftforge.fml.client.gui.widget.Slider;

import static github.kituin.chatimage.ChatImage.CONFIG;

public final class LimitSliderUpdate implements Slider.ISlider {

    private final LimitSlider.LimitType limitType;

    public LimitSliderUpdate(LimitSlider.LimitType limitType) {
        this.limitType = limitType;
    }

    @Override
    public void onChangeSliderValue(Slider slider) {
        int position = (int) (slider.sliderValue * (slider.maxValue - slider.minValue) + slider.minValue);
        switch (limitType) {
            case WIDTH:
                CONFIG.limitWidth = position;
                break;
            case HEIGHT:
                CONFIG.limitHeight = position;
                break;
        }
        ChatImageConfig.saveConfig(CONFIG);
    }
}
