package github.kituin.chatimage.widget;


import io.github.kituin.ChatImageCode.ChatImageConfig;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

import static github.kituin.chatimage.client.ChatImageClient.CONFIG;

public class TimeOutSlider extends SettingSliderWidget {


    public TimeOutSlider() {
        super(100, 100, 150, 20, CONFIG.timeout, 3, 60);
        this.updateMessage();
        this.tip = Tooltip.of(Text.translatable("timeout.chatimage.tooltip"));
        this.setTooltip(this.tip);
    }

    @Override
    protected void updateMessage() {
        this.setMessage(ScreenTexts.composeGenericOptionText(Text.translatable("timeout.chatimage.gui"), Text.literal(String.valueOf(this.position))).append(" ").append(Text.translatable("seconds.chatimage.gui")));
        CONFIG.timeout = this.position;
        ChatImageConfig.saveConfig(CONFIG);
    }
}
