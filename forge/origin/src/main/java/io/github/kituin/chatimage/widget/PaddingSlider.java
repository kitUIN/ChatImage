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
public class PaddingSlider extends SettingSliderWidget {
    protected final PaddingType paddingType;

// IF forge-1.16.5
//    protected final ITextComponent title;
//    public PaddingSlider(int x, int y, int width, int height, ITextComponent title, int value, float max, PaddingType paddingType, SettingSliderWidget.OnTooltip tooltip) {
//        super(x, y, width, height, title, createLiteralComponent(""),
//                value, 0F, max, tooltip,new PaddingSliderUpdate(paddingType));
//        this.title = title;
//        this.paddingType = paddingType;
//    }
//
//    public static ITextComponent tooltip(PaddingType paddingType) {
//        switch (paddingType) {
//            case TOP:
//                return createTranslatableComponent("top.padding.chatimage.tooltip");
//            case BOTTOM:
//                return createTranslatableComponent("bottom.padding.chatimage.tooltip");
//            case LEFT:
//                return createTranslatableComponent("left.padding.chatimage.tooltip");
//            case RIGHT:
//                return createTranslatableComponent("right.padding.chatimage.tooltip");
//        }
//        return null;
//    }
//    public static class PaddingSliderUpdate implements Slider.ISlider {
//
//        private final PaddingSlider.PaddingType paddingType;
//
//        public PaddingSliderUpdate(PaddingSlider.PaddingType paddingType) {
//            this.paddingType = paddingType;
//        }
//
//        @Override
//        public void onChangeSliderValue(Slider slider) {
//            int position = (int) (slider.sliderValue * (slider.maxValue - slider.minValue) + slider.minValue);
//            switch (paddingType) {
//                case TOP:
//                    ChatImage.CONFIG.paddingTop = position;
//                    break;
//                case BOTTOM:
//                    ChatImage.CONFIG.paddingBottom = position;
//                    break;
//                case LEFT:
//                    ChatImage.CONFIG.paddingLeft = position;
//                    break;
//                case RIGHT:
//                    ChatImage.CONFIG.paddingRight = position;
//                    break;
//            }
//            ChatImageConfig.saveConfig(ChatImage.CONFIG);
//        }
//    }
//
//
// ELSE
//    protected final Component title;
//    public PaddingSlider(int x, int y, int width, int height, Component title, int value, float max, PaddingType paddingType, SettingSliderWidget.OnTooltip tooltip) {
//        super(x, y, width, height, value, 0F, max, tooltip);
//        this.title = title;
//        this.paddingType = paddingType;
//        this.updateMessage();
//    }
//
//    @Override
//    protected void updateMessage() {
//        this.setMessage(composeGenericOptionComponent(title, createLiteralComponent(String.valueOf(this.position))));
//        switch (paddingType) {
//            case TOP:
//                CONFIG.paddingTop = this.position;
//                break;
//            case BOTTOM:
//                CONFIG.paddingBottom = this.position;
//                break;
//            case LEFT:
//                CONFIG.paddingLeft = this.position;
//                break;
//            case RIGHT:
//                CONFIG.paddingRight = this.position;
//                break;
//            default:
//                return;
//        }
//        ChatImageConfig.saveConfig(CONFIG);
//    }
//
//    public static Component tooltip(PaddingType paddingType) {
//        return switch (paddingType) {
//            case TOP -> createTranslatableComponent("top.padding.chatimage.tooltip");
//            case BOTTOM -> createTranslatableComponent("bottom.padding.chatimage.tooltip");
//            case LEFT -> createTranslatableComponent("left.padding.chatimage.tooltip");
//            case RIGHT -> createTranslatableComponent("right.padding.chatimage.tooltip");
//        };
//    }
// END IF
    public enum PaddingType {
        LEFT, RIGHT, TOP, BOTTOM
    }
}