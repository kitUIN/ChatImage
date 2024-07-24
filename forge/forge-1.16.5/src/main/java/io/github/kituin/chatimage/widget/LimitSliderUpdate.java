package io.github.kituin.chatimage.widget;

import io.github.kituin.ChatImageCode.ChatImageConfig;
import io.github.kituin.chatimage.ChatImage;
import net.minecraftforge.fml.client.gui.widget.Slider;

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
                ChatImage.CONFIG.limitWidth = position;
                break;
            case HEIGHT:
                ChatImage.CONFIG.limitHeight = position;
                break;
        }
        ChatImageConfig.saveConfig(ChatImage.CONFIG);
    }
}
