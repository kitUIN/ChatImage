package github.kituin.chatimage.widget;

import github.kituin.chatimage.config.ChatImageConfig;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static github.kituin.chatimage.Chatimage.CONFIG;

@OnlyIn(Dist.CLIENT)
public class PaddingSlider extends SettingSliderWidget {
    protected final Component title;
    protected final PaddingType paddingType;

    public PaddingSlider(int x, int y, int width, int height, Component title, int value, float max, PaddingType paddingType) {
        super(x, y, width, height, value, 0F, max);
        this.title = title;
        this.paddingType = paddingType;
        this.updateMessage();
        this.tooltip();
    }

    @Override
    protected void updateMessage() {
        this.setMessage(CommonComponents.optionNameValue(title, Component.literal(String.valueOf(this.position))));
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

    public void tooltip() {
        Component text;
        switch (paddingType) {
            case TOP:
                text = Component.translatable("top.padding.chatimage.tooltip");
                break;
            case BOTTOM:
                text = Component.translatable("bottom.padding.chatimage.tooltip");
                break;
            case LEFT:
                text = Component.translatable("left.padding.chatimage.tooltip");
                break;
            case RIGHT:
                text = Component.translatable("right.padding.chatimage.tooltip");
                break;
            default:
                return;
        }
        this.setTooltip(Tooltip.create(text));
    }

    public enum PaddingType {
        LEFT, RIGHT, TOP, BOTTOM

    }
}
