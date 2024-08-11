package io.github.kituin.chatimage.widget;

import io.github.kituin.ChatImageCode.ChatImageConfig;
// IF forge-1.16.5
//import #Component#;
//import net.minecraftforge.fml.client.gui.widget.Slider;
// ELSE
//import net.minecraft.network.chat.Component;
// END IF
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static io.github.kituin.chatimage.ChatImage.CONFIG;
import static io.github.kituin.chatimage.tool.SimpleUtil.*;

/**
 * @author kitUIN
 */
@OnlyIn(Dist.CLIENT)
public class GifSlider extends SettingSliderWidget {

    public GifSlider(int x, int y, int width, int height, SettingSliderWidget.OnTooltip tooltip) {
        super(x, y, width, height, CONFIG.gifSpeed, 1, 20, tooltip);
        this.updateMessage();
    }
    
// IF forge-1.16.5
//public static class GifSliderUpdate implements Slider.ISlider {
//    @Override
//    public void onChangeSliderValue(Slider slider) {
//        CONFIG.gifSpeed = (int) (slider.sliderValue * (slider.maxValue - slider.minValue) + slider.minValue);
//        ChatImageConfig.saveConfig(CONFIG);
//    }
//}
// ELSE
// END IF
    @Override
    protected void updateMessage() {
        this.setMessage(composeGenericOptionComponent(createTranslatableComponent("gif.chatimage.gui"),
                createLiteralComponent(String.valueOf(this.position))));
        CONFIG.gifSpeed = this.position;
        ChatImageConfig.saveConfig(CONFIG);
    }



    
// IF forge-1.16.5
//public static #Component# tooltip() {
// ELSE
//    public static #Component# tooltip() {
// END IF
        return createTranslatableComponent("gif.chatimage.tooltip");
    }
}