package io.github.kituin.chatimage.widget;

import io.github.kituin.ChatImageCode.ChatImageConfig;
import io.github.kituin.chatimage.ChatImage;
import net.minecraftforge.fml.client.gui.widget.Slider;

public final class PaddingSliderUpdate implements Slider.ISlider {

    private final PaddingSlider.PaddingType paddingType;

    public PaddingSliderUpdate(PaddingSlider.PaddingType paddingType) {
        this.paddingType = paddingType;
    }

    @Override
    public void onChangeSliderValue(Slider slider) {
        int position = (int) (slider.sliderValue * (slider.maxValue - slider.minValue) + slider.minValue);
        switch (paddingType) {
            case TOP:
                ChatImage.CONFIG.paddingTop = position;
                break;
            case BOTTOM:
                ChatImage.CONFIG.paddingBottom = position;
                break;
            case LEFT:
                ChatImage.CONFIG.paddingLeft = position;
                break;
            case RIGHT:
                ChatImage.CONFIG.paddingRight = position;
                break;
        }
        ChatImageConfig.saveConfig(ChatImage.CONFIG);
    }
}
