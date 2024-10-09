package io.github.kituin.chatimage.widget;

import io.github.kituin.ChatImageCode.ChatImageConfig;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import static io.github.kituin.chatimage.ChatImage.CONFIG;

@OnlyIn(Dist.CLIENT)
public class PaddingSlider extends SettingSliderWidget {
    protected final Component title;
    protected final PaddingType paddingType;

    public PaddingSlider(int x, int y, int width, int height, Component title, int value, float max, PaddingType paddingType, SettingSliderWidget.OnTooltip tooltip) {
        super(x, y, width, height, value, 0F, max, tooltip);
        this.title = title;
        this.paddingType = paddingType;
        this.updateMessage();
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

    public static Component tooltip(PaddingType paddingType) {
        return switch (paddingType) {
            case TOP -> Component.translatable("top.padding.chatimage.tooltip");
            case BOTTOM -> Component.translatable("bottom.padding.chatimage.tooltip");
            case LEFT -> Component.translatable("left.padding.chatimage.tooltip");
            case RIGHT -> Component.translatable("right.padding.chatimage.tooltip");
        };
    }

    public enum PaddingType {
        LEFT, RIGHT, TOP, BOTTOM

    }
}
