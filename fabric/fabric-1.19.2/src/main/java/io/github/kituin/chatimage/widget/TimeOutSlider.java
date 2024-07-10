package io.github.kituin.chatimage.widget;

import io.github.kituin.ChatImageCode.ChatImageConfig;
import io.github.kituin.chatimage.client.ChatImageClient;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

public class TimeOutSlider extends SettingSliderWidget {


    public TimeOutSlider(int x, int y, int width, int height, TooltipSupplier tooltipSupplier) {
        super(x, y, width, height, ChatImageClient.CONFIG.timeout, 3, 60, tooltipSupplier);
        this.updateMessage();
    }

    @Override
    protected void updateMessage() {
        this.setMessage(ScreenTexts.composeGenericOptionText(Text.translatable("timeout.chatimage.gui"), Text.literal(String.valueOf(this.position))).append(" ").append(Text.translatable("seconds.chatimage.gui")));
        ChatImageClient.CONFIG.timeout = this.position;
        ChatImageConfig.saveConfig(ChatImageClient.CONFIG);
    }
}
