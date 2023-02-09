package github.kituin.chatimage.gui;

import github.kituin.chatimage.widget.LimitSlider;
import github.kituin.chatimage.widget.PaddingSlider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import static github.kituin.chatimage.client.ChatImageClient.CONFIG;
import static github.kituin.chatimage.widget.LimitSlider.LimitType.HEIGHT;
import static github.kituin.chatimage.widget.LimitSlider.LimitType.WIDTH;
import static github.kituin.chatimage.widget.PaddingSlider.PaddingType.*;

@Environment(EnvType.CLIENT)
public class LimitPaddingScreen extends ConfigRawScreen {
    public LimitPaddingScreen(Screen screen) {
        super(Text.translatable("padding.chatimage.gui"), screen);
    }

    protected void init() {
        super.init();
        this.addDrawableChild(new PaddingSlider(
                this.width / 2 - 154, this.height / 4 + 24 + -16, 150, 20,
                Text.translatable("left.padding.chatimage.gui"), CONFIG.paddingLeft,
                this.width / 2, LEFT, getSliderTooltip(getPaddingTooltipText(LEFT))));
        this.addDrawableChild(new PaddingSlider(
                this.width / 2 + 4, this.height / 4 + 24 + -16, 150, 20,
                Text.translatable("right.padding.chatimage.gui"), CONFIG.paddingRight,
                this.width / 2, RIGHT, getSliderTooltip(getPaddingTooltipText(RIGHT))));
        this.addDrawableChild(new PaddingSlider(
                this.width / 2 - 154, this.height / 4 + 48 + -16, 150, 20,
                Text.translatable("top.padding.chatimage.gui"), CONFIG.paddingTop,
                this.height / 2, TOP, getSliderTooltip(getPaddingTooltipText(TOP))));
        this.addDrawableChild(new PaddingSlider(
                this.width / 2 + 4, this.height / 4 + 48 + -16, 150, 20,
                Text.translatable("bottom.padding.chatimage.gui"), CONFIG.paddingBottom,
                this.height / 2, BOTTOM, getSliderTooltip(getPaddingTooltipText(BOTTOM))));
        this.addDrawableChild(new LimitSlider(
                this.width / 2 - 154, this.height / 4 + 72 + -16, 150, 20,
                Text.translatable("width.limit.chatimage.gui"), CONFIG.limitWidth,
                this.width, WIDTH, getSliderTooltip(getLimitTooltipText(WIDTH))));
        this.addDrawableChild(new LimitSlider(
                this.width / 2 + 4, this.height / 4 + 72 + -16, 150, 20,
                Text.translatable("height.limit.chatimage.gui"), CONFIG.limitHeight,
                this.height, HEIGHT, getSliderTooltip(getLimitTooltipText(HEIGHT))));
        this.addDrawableChild(new ButtonWidget(
                this.width / 2 - 77, this.height / 4 + 96 + -16, 150, 20,
                Text.translatable("gui.back"), (button) -> {
            this.client.setScreen(this.parent);
        }));


    }

    private Text getLimitTooltipText(LimitSlider.LimitType limitType) {
        if (limitType == WIDTH) {
            return Text.translatable("width.limit.chatimage.tooltip");
        } else {
            return Text.translatable("height.limit.chatimage.tooltip");
        }
    }

    private Text getPaddingTooltipText(PaddingSlider.PaddingType paddingType) {
        Text text;
        switch (paddingType) {
            case TOP:
                text = Text.translatable("top.padding.chatimage.tooltip");
                break;
            case BOTTOM:
                text = Text.translatable("bottom.padding.chatimage.tooltip");
                break;
            case LEFT:
                text = Text.translatable("left.padding.chatimage.tooltip");
                break;
            case RIGHT:
                text = Text.translatable("right.padding.chatimage.tooltip");
                break;
            default:
                return null;
        }
        return text;
    }
}
