package github.kituin.chatimage.widget;

import github.kituin.chatimage.config.ChatImageConfig;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


import static github.kituin.chatimage.Chatimage.CONFIG;
@OnlyIn(Dist.CLIENT)
public class LimitSlider extends SettingSliderWidget {
    protected final Component title;
    protected final LimitType limitType;

    public LimitSlider(Component title, int value, float min, float max, LimitType limitType) {
        super(100, 100, 150, 20, value, min, max);
        this.title = title;
        this.limitType = limitType;
        this.updateMessage();
        this.tooltip();
    }

    @Override
    protected void updateMessage() {
        switch (limitType) {
            case WIDTH:
                this.setMessage(CommonComponents.optionNameValue(title, CONFIG.limitWidth == 0 ? Component.translatable("default.chatimage.gui") : Component.literal(String.valueOf(this.position))));
                CONFIG.limitWidth = this.position;
                break;
            case HEIGHT:
                this.setMessage(CommonComponents.optionNameValue(title, CONFIG.limitHeight == 0 ? Component.translatable("default.chatimage.gui") : Component.literal(String.valueOf(this.position))));
                CONFIG.limitHeight = this.position;
                break;
            default:
                return;
        }
        ChatImageConfig.saveConfig(CONFIG);
    }
    private void tooltip() {
        Component text;
        switch (limitType) {
            case WIDTH:
                text = Component.translatable("width.limit.chatimage.tooltip");
                break;
            case HEIGHT:
                text = Component.translatable("height.limit.chatimage.tooltip");
                break;
            default:
                return;
        }
        this.setTooltip(Tooltip.create(text));
    }
    public enum LimitType {
        WIDTH, HEIGHT

    }
}