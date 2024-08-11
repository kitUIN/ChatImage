package io.github.kituin.chatimage.widget;

import io.github.kituin.ChatImageCode.ChatImageConfig;
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

    @Override
    protected void updateMessage() {
        this.setMessage(composeGenericOptionComponent(createTranslatableComponent("gif.chatimage.gui"),
                createLiteralComponent(String.valueOf(this.position))));
        CONFIG.gifSpeed = this.position;
        ChatImageConfig.saveConfig(CONFIG);
    }


    public static #Component# tooltip() {
        return createTranslatableComponent("gif.chatimage.tooltip");
    }
}