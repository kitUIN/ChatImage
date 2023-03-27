package github.kituin.chatimage.widget;

import github.kituin.chatimage.config.ChatImageConfig;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static github.kituin.chatimage.ChatImage.CONFIG;

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
        this.setMessage(CommonComponents.optionNameValue(title, new TextComponent(String.valueOf(this.position))));
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
            case TOP -> new TranslatableComponent("top.padding.chatimage.tooltip");
            case BOTTOM -> new TranslatableComponent("bottom.padding.chatimage.tooltip");
            case LEFT -> new TranslatableComponent("left.padding.chatimage.tooltip");
            case RIGHT -> new TranslatableComponent("right.padding.chatimage.tooltip");
        };
    }

    public enum PaddingType {
        LEFT, RIGHT, TOP, BOTTOM

    }
}
