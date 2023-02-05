package github.kituin.chatimage.widget;

import github.kituin.chatimage.config.ChatImageConfig;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static github.kituin.chatimage.Chatimage.CONFIG;
@OnlyIn(Dist.CLIENT)
public class TimeOutSlider extends SettingSliderWidget {


    public TimeOutSlider() {
        super(100, 100, 150, 20, CONFIG.timeout, 3, 60);
        this.updateMessage();
        this.setTooltip(Tooltip.create(Component.translatable("timeout.chatimage.tooltip")));
    }

    @Override
    protected void updateMessage() {
        this.setMessage(CommonComponents.optionNameValue(Component.translatable("timeout.chatimage.gui"), Component.literal(String.valueOf(this.position))).append(" ").append(Component.translatable("seconds.chatimage.gui")));
        CONFIG.timeout = this.position;
        ChatImageConfig.saveConfig(CONFIG);
    }
}
