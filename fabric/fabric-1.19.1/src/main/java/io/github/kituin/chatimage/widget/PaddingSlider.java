package io.github.kituin.chatimage.widget;

import io.github.kituin.ChatImageCode.ChatImageConfig;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

import static io.github.kituin.chatimage.client.ChatImageClient.CONFIG;

public class PaddingSlider extends SettingSliderWidget {
    protected final Text title;
    protected final PaddingType paddingType;

    public PaddingSlider(int x, int y, int width, int height, Text title, int value, float max, PaddingType paddingType, TooltipSupplier tooltipSupplier) {
        super(x, y, width, height, value, 0F, max, tooltipSupplier);
        this.title = title;
        this.paddingType = paddingType;
        this.updateMessage();
    }

    @Override
    protected void updateMessage() {
        this.setMessage(ScreenTexts.composeGenericOptionText(title, Text.literal(String.valueOf(this.position))));
        switch (paddingType) {
            case TOP:
                CONFIG.paddingTop = this.position;
                break;
            case BOTTOM:
                CONFIG.paddingBottom = this.position;
                break;
            case LEFT:
                CONFIG.paddingLeft = this.position;
                break;
            case RIGHT:
                CONFIG.paddingRight = this.position;
                break;
            default:
                return;
        }
        ChatImageConfig.saveConfig(CONFIG);
    }


    public enum PaddingType {
        LEFT, RIGHT, TOP, BOTTOM

    }
}
