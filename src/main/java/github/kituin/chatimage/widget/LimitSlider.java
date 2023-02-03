package github.kituin.chatimage.widget;

import github.kituin.chatimage.config.ChatImageConfig;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

import static github.kituin.chatimage.client.ChatImageClient.CONFIG;

public class LimitSlider extends SettingSliderWidget {
    protected final Text title;
    protected final LimitType limitType;

    public LimitSlider(Text title, int value, float min, float max, LimitType limitType) {
        super(100, 100, 150, 20, value, min, max);
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
                break;
        }
        ChatImageConfig.saveConfig(CONFIG);
    }

    public enum LimitType {
        WIDTH, HEIGHT

    }
}