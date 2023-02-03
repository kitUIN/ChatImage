package github.kituin.chatimage.widget;

import github.kituin.chatimage.config.ChatImageConfig;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

import static github.kituin.chatimage.client.ChatImageClient.CONFIG;

public class TimeOutSlider extends SettingSliderWidget {


    public TimeOutSlider() {
        super(100, 100, 150, 20, CONFIG.timeout, 3, 60);
        this.updateMessage();
    }

    @Override
    protected void updateMessage() {
        this.setMessage(ScreenTexts.composeGenericOptionText(Text.translatable("timeout.chatimage.gui"), Text.literal(String.valueOf(this.position))).append(" ").append(Text.translatable("seconds.chatimage.gui")));
        CONFIG.timeout = this.position;
        ChatImageConfig.saveConfig(CONFIG);
    }
}
