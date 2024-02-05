package github.kituin.chatimage.widget;

import io.github.kituin.ChatImageCode.ChatImageConfig;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static github.kituin.chatimage.ChatImage.CONFIG;

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
        this.setMessage(CommonComponents.optionNameValue(new TranslatableComponent("gif.chatimage.gui"), new TextComponent(String.valueOf(this.position))));
        CONFIG.gifSpeed = this.position;
        ChatImageConfig.saveConfig(CONFIG);
    }


    public static Component tooltip() {
        return new TranslatableComponent("gif.chatimage.tooltip");
    }
}
