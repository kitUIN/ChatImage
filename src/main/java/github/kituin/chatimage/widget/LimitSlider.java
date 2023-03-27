package github.kituin.chatimage.widget;

import github.kituin.chatimage.config.ChatImageConfig;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static github.kituin.chatimage.ChatImage.CONFIG;

@OnlyIn(Dist.CLIENT)
public class LimitSlider extends SettingSliderWidget {
    protected final Component title;
    protected final LimitType limitType;

    public LimitSlider(int x, int y, int width, int height, Component title, int value, float max, LimitType limitType, SettingSliderWidget.OnTooltip tooltip) {
        super(x, y, width, height, value, 1F, max, tooltip);
        this.title = title;
        this.limitType = limitType;
        this.updateMessage();

    }

    @Override
    protected void updateMessage() {
        switch (limitType) {
            case WIDTH -> {
                this.setMessage(CommonComponents.optionNameValue(title, CONFIG.limitWidth == 0 ? Component.translatable("default.chatimage.gui") : Component.literal(String.valueOf(this.position))));
                CONFIG.limitWidth = this.position;
            }
            case HEIGHT -> {
                this.setMessage(CommonComponents.optionNameValue(title, CONFIG.limitHeight == 0 ? Component.translatable("default.chatimage.gui") : Component.literal(String.valueOf(this.position))));
                CONFIG.limitHeight = this.position;
            }
            default -> {
                return;
            }
        }
        ChatImageConfig.saveConfig(CONFIG);
    }

    public static Component tooltip(LimitType limitType) {
        return switch (limitType) {
            case WIDTH -> Component.translatable("width.limit.chatimage.tooltip");
            case HEIGHT -> Component.translatable("height.limit.chatimage.tooltip");
        };
    }

    public enum LimitType {
        WIDTH, HEIGHT

    }
}