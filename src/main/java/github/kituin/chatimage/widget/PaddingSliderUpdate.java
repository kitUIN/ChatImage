package github.kituin.chatimage.widget;

import github.kituin.chatimage.config.ChatImageConfig;
import net.minecraftforge.fml.client.gui.widget.Slider;

import static github.kituin.chatimage.ChatImage.CONFIG;

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
                CONFIG.paddingTop = position;
                break;
            case BOTTOM:
                CONFIG.paddingBottom = position;
                break;
            case LEFT:
                CONFIG.paddingLeft = position;
                break;
            case RIGHT:
                CONFIG.paddingRight = position;
                break;
        }
        ChatImageConfig.saveConfig(CONFIG);
    }
}
