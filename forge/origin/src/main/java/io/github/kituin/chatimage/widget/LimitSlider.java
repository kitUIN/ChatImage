package io.github.kituin.chatimage.widget;

import io.github.kituin.ChatImageCode.ChatImageConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static io.github.kituin.chatimage.ChatImage.CONFIG;
import static io.github.kituin.chatimage.tool.SimpleUtil.*;


@OnlyIn(Dist.CLIENT)
public class LimitSlider extends SettingSliderWidget {
    protected final LimitType limitType;

    protected final #Component# title;
    public LimitSlider(int x, int y, int width, int height, #Component# title, int value, float max, LimitType limitType, SettingSliderWidget.OnTooltip tooltip) {
        super(x, y, width, height, value, 1F, max, tooltip);
        this.title = title;
        this.limitType = limitType;
        this.updateMessage();
    }
    @Override
    protected void updateMessage() {
        switch (limitType) {
            case WIDTH:
                this.setMessage(composeGenericOptionComponent(title, CONFIG.limitWidth == 0 ? createTranslatableComponent("default.chatimage.gui") : createLiteralComponent(String.valueOf(this.position))));
                CONFIG.limitWidth = this.position;
                break;
            case HEIGHT:
                this.setMessage(composeGenericOptionComponent(title, CONFIG.limitHeight == 0 ? createTranslatableComponent("default.chatimage.gui") : createLiteralComponent(String.valueOf(this.position))));
                CONFIG.limitHeight = this.position;
                break;
            default:
                return;
        }
        ChatImageConfig.saveConfig(CONFIG);
    }
    public static #Component# tooltip(LimitType limitType) {
        switch (limitType) {
            case WIDTH:
                return createTranslatableComponent("width.limit.chatimage.tooltip");
            case HEIGHT:
                return createTranslatableComponent("height.limit.chatimage.tooltip");
            default:
                throw new IllegalArgumentException();
        }
    }
    public enum LimitType {
        WIDTH, HEIGHT
    }
}