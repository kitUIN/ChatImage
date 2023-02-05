package github.kituin.chatimage.widget;

import github.kituin.chatimage.config.ChatImageConfig;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static github.kituin.chatimage.Chatimage.CONFIG;

/**
 * @author kitUIN
 */
@OnlyIn(Dist.CLIENT)
public class GifSlider extends SettingSliderWidget {

    public GifSlider(int x, int y, int width, int height) {
        super(x, y, width, height, CONFIG.gifSpeed, 1, 20);
        this.updateMessage();
        this.tooltip();
    }


    @Override
    protected void updateMessage() {
        this.setMessage(CommonComponents.optionNameValue(Component.translatable("gif.chatimage.gui"), Component.literal(String.valueOf(this.position))));
        CONFIG.gifSpeed = this.position;
        ChatImageConfig.saveConfig(CONFIG);
    }

    @Override
    protected void tooltip() {
        this.setTooltip(Tooltip.create(Component.translatable("gif.chatimage.tooltip")));
    }
}
