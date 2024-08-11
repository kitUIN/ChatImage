package io.github.kituin.chatimage.widget;

import io.github.kituin.ChatImageCode.ChatImageConfig;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static io.github.kituin.chatimage.ChatImage.CONFIG;
import static io.github.kituin.chatimage.tool.SimpleUtil.*;

@OnlyIn(Dist.CLIENT)
public class PaddingSlider extends SettingSliderWidget {
    protected final PaddingType paddingType;

    protected final #Component# title;
    public PaddingSlider(int x, int y, int width, int height, #Component# title, int value, float max, PaddingType paddingType, SettingSliderWidget.OnTooltip tooltip) {

        super(x, y, width, height, value, 0F, max, tooltip);
        this.title = title;
        this.paddingType = paddingType;
        this.updateMessage();
    }

    @Override
    protected void updateMessage() {
        this.setMessage(composeGenericOptionComponent(title, createLiteralComponent(String.valueOf(this.position))));
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

    public static #Component# tooltip(PaddingType paddingType) {
        switch (paddingType) {
            case TOP:
                return createTranslatableComponent("top.padding.chatimage.tooltip");
            case BOTTOM:
                return createTranslatableComponent("bottom.padding.chatimage.tooltip");
            case LEFT:
                return createTranslatableComponent("left.padding.chatimage.tooltip");
            case RIGHT:
                return createTranslatableComponent("right.padding.chatimage.tooltip");
            default:
                throw new IllegalArgumentException();
        }
    }
    public enum PaddingType {
        LEFT, RIGHT, TOP, BOTTOM
    }
}