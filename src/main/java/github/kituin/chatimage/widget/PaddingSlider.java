package github.kituin.chatimage.widget;

import github.kituin.chatimage.config.ChatImageConfig;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

import static github.kituin.chatimage.client.ChatImageClient.CONFIG;

public class PaddingSlider extends SettingSliderWidget {
    protected final Text title;
    protected final PaddingType paddingType;

    public PaddingSlider(Text title, int value, float min, float max, PaddingType paddingType) {
        super(100, 100, 150, 20, value, min, max);
        this.title = title;
        this.paddingType = paddingType;
        this.updateMessage();
        this.tooltip();
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

    private void tooltip() {
        Text text;
        switch (paddingType) {
            case TOP:
                text = Text.translatable("top.padding.chatimage.tooltip");
                break;
            case BOTTOM:
                text = Text.translatable("bottom.padding.chatimage.tooltip");
                break;
            case LEFT:
                text = Text.translatable("left.padding.chatimage.tooltip");
                break;
            case RIGHT:
                text = Text.translatable("right.padding.chatimage.tooltip");
                break;
            default:
                return;
        }
        this.setTooltip(Tooltip.of(text));
    }

    public enum PaddingType {
        LEFT, RIGHT, TOP, BOTTOM

    }
}
