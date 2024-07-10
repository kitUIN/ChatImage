package io.github.kituin.chatimage.widget;

import io.github.kituin.ChatImageCode.ChatImageConfig;
import io.github.kituin.chatimage.client.ChatImageClient;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

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
                ChatImageClient.CONFIG.paddingTop = this.position;
                break;
            case BOTTOM:
                ChatImageClient.CONFIG.paddingBottom = this.position;
                break;
            case LEFT:
                ChatImageClient.CONFIG.paddingLeft = this.position;
                break;
            case RIGHT:
                ChatImageClient.CONFIG.paddingRight = this.position;
                break;
            default:
                return;
        }
        ChatImageConfig.saveConfig(ChatImageClient.CONFIG);
    }


    public enum PaddingType {
        LEFT, RIGHT, TOP, BOTTOM

    }
}
