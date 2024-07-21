package io.github.kituin.chatimage.widget;

import io.github.kituin.ChatImageCode.ChatImageConfig;
// IF >= fabric-1.19.4
//import net.minecraft.client.gui.tooltip.Tooltip;
// END IF

import net.minecraft.text.Text;

import static io.github.kituin.chatimage.client.ChatImageClient.CONFIG;
import static io.github.kituin.chatimage.tool.SimpleUtil.composeGenericOptionText;
import static io.github.kituin.chatimage.tool.SimpleUtil.createLiteralText;


public class PaddingSlider extends SettingSliderWidget {
    protected final Text title;
    protected final PaddingType paddingType;


// IF >= fabric-1.19.4
//    public PaddingSlider(Text title, int value, float min, float max, PaddingType paddingType) {
//        super(100, 100, 150, 20, value, min, max);
//        this.title = title;
//        this.paddingType = paddingType;
//        this.updateMessage();
//        this.tooltip();
//}
//
// ELSE
    public PaddingSlider(int x, int y, int width, int height, Text title, int value, float max, PaddingType paddingType, TooltipSupplier tooltipSupplier) {
        super(x, y, width, height, value, 0F, max, tooltipSupplier);
        this.title = title;
        this.paddingType = paddingType;
        this.updateMessage();
    }
// END IF
    @Override
    protected void updateMessage() {
        this.setMessage(composeGenericOptionText(title, createLiteralText(String.valueOf(this.position))));
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
// IF >= fabric-1.19.4
//    private void tooltip() {
//        Text text;
//        switch (paddingType) {
//            case TOP:
//                text = Text.translatable("top.padding.chatimage.tooltip");
//                break;
//            case BOTTOM:
//                text = Text.translatable("bottom.padding.chatimage.tooltip");
//                break;
//            case LEFT:
//                text = Text.translatable("left.padding.chatimage.tooltip");
//                break;
//            case RIGHT:
//                text = Text.translatable("right.padding.chatimage.tooltip");
//                break;
//            default:
//                return;
//        }
//        this.tip = Tooltip.of(text);
//        this.setTooltip(this.tip);
//    }
// END IF
    public enum PaddingType {
        LEFT, RIGHT, TOP, BOTTOM

    }
}