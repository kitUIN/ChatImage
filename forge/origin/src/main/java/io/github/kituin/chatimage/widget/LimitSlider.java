package io.github.kituin.chatimage.widget;

import io.github.kituin.ChatImageCode.ChatImageConfig;
// IF forge-1.16.5
//import net.minecraft.util.text.ITextComponent;
//import net.minecraftforge.fml.client.gui.widget.Slider;
// ELSE
//import net.minecraft.network.chat.Component;
// END IF
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static io.github.kituin.chatimage.ChatImage.CONFIG;
import static io.github.kituin.chatimage.tool.SimpleUtil.*;


@OnlyIn(Dist.CLIENT)
public class LimitSlider extends SettingSliderWidget {
    protected final LimitType limitType;


// IF forge-1.16.5
//    public LimitSlider(int x, int y, int width, int height, ITextComponent title, int value, float max, LimitType limitType, SettingSliderWidget.OnTooltip tooltip) {
//        super(x, y, width, height, title,
//                createLiteralComponent(""),
//                value, 1F, max, tooltip,new LimitSliderUpdate(limitType));
//        this.limitType = limitType;
//    }
//    public static ITextComponent tooltip(LimitType limitType) {
//        switch (limitType) {
//            case WIDTH:
//                return createTranslatableComponent("width.limit.chatimage.tooltip");
//            case HEIGHT:
//                return createTranslatableComponent("height.limit.chatimage.tooltip");
//        }
//        return null;
//    }
//    public static class LimitSliderUpdate implements Slider.ISlider {
//
//        private final LimitSlider.LimitType limitType;
//
//        public LimitSliderUpdate(LimitSlider.LimitType limitType) {
//            this.limitType = limitType;
//        }
//
//        @Override
//        public void onChangeSliderValue(Slider slider) {
//            int position = (int) (slider.sliderValue * (slider.maxValue - slider.minValue) + slider.minValue);
//            switch (limitType) {
//                case WIDTH:
//                    CONFIG.limitWidth = position;
//                    break;
//                case HEIGHT:
//                    CONFIG.limitHeight = position;
//                    break;
//            }
//            ChatImageConfig.saveConfig(CONFIG);
//        }
//    }
// ELSE
//    protected final Component title;
//    public LimitSlider(int x, int y, int width, int height, Component title, int value, float max, LimitType limitType, SettingSliderWidget.OnTooltip tooltip) {
//        super(x, y, width, height, value, 1F, max, tooltip);
//        this.title = title;
//        this.limitType = limitType;
//        this.updateMessage();
//    }
//    @Override
//    protected void updateMessage() {
//        switch (limitType) {
//            case WIDTH -> {
//                this.setMessage(composeGenericOptionComponent(title, CONFIG.limitWidth == 0 ? createTranslatableComponent("default.chatimage.gui") : createLiteralComponent(String.valueOf(this.position))));
//                CONFIG.limitWidth = this.position;
//            }
//            case HEIGHT -> {
//                this.setMessage(composeGenericOptionComponent(title, CONFIG.limitHeight == 0 ? createTranslatableComponent("default.chatimage.gui") : createLiteralComponent(String.valueOf(this.position))));
//                CONFIG.limitHeight = this.position;
//            }
//            default -> {
//                return;
//            }
//        }
//        ChatImageConfig.saveConfig(CONFIG);
//    }
// END IF
    public enum LimitType {
        WIDTH, HEIGHT
    }
}