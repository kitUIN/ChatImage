package io.github.kituin.chatimage.widget;

import io.github.kituin.ChatImageCode.ChatImageConfig;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

import static io.github.kituin.chatimage.client.ChatImageClient.CONFIG;

public class LimitSlider extends SettingSliderWidget {
    protected final Text title;
    protected final LimitType limitType;

    public LimitSlider(int x, int y, int width, int height, Text title, int value, float max, LimitType limitType, TooltipSupplier tooltipSupplier) {
        super(x, y, width, height, value, 1F, max, tooltipSupplier);
        this.title = title;
        this.limitType = limitType;
        this.updateMessage();
    }

    @Override
    protected void updateMessage() {
        switch (limitType) {
            case WIDTH:
                this.setMessage(ScreenTexts.composeGenericOptionText(title, CONFIG.limitWidth == 0 ? Text.translatable("default.chatimage.gui") : Text.literal(String.valueOf(this.position))));
                CONFIG.limitWidth = this.position;
                break;
            case HEIGHT:
                this.setMessage(ScreenTexts.composeGenericOptionText(title, CONFIG.limitHeight == 0 ? Text.translatable("default.chatimage.gui") : Text.literal(String.valueOf(this.position))));
                CONFIG.limitHeight = this.position;
                break;
            default:
                return;
        }
        ChatImageConfig.saveConfig(CONFIG);
    }

    public enum LimitType {
        WIDTH, HEIGHT

    }
}