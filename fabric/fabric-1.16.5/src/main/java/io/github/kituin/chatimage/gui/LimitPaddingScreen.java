package io.github.kituin.chatimage.gui;

import io.github.kituin.chatimage.widget.LimitSlider;
import io.github.kituin.chatimage.widget.PaddingSlider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import static io.github.kituin.chatimage.client.ChatImageClient.CONFIG;
import static io.github.kituin.chatimage.widget.LimitSlider.LimitType.HEIGHT;
import static io.github.kituin.chatimage.widget.LimitSlider.LimitType.WIDTH;
import static io.github.kituin.chatimage.widget.PaddingSlider.PaddingType.*;

@Environment(EnvType.CLIENT)
public class LimitPaddingScreen extends ConfigRawScreen {
    public LimitPaddingScreen(Screen screen) {
        super(new TranslatableText("padding.chatimage.gui"), screen);
    }

    protected void init() {
        super.init();
        this.addButton(new PaddingSlider(
                this.width / 2 - 154, this.height / 4 + 24 + -16, 150, 20,
                new TranslatableText("left.padding.chatimage.gui"), CONFIG.paddingLeft,
                (float) this.width / 2, LEFT, getSliderTooltip(getPaddingTooltipText(LEFT))));
        this.addButton(new PaddingSlider(
                this.width / 2 + 4, this.height / 4 + 24 + -16, 150, 20,
                new TranslatableText("right.padding.chatimage.gui"), CONFIG.paddingRight,
                (float) this.width / 2, RIGHT, getSliderTooltip(getPaddingTooltipText(RIGHT))));
        this.addButton(new PaddingSlider(
                this.width / 2 - 154, this.height / 4 + 48 + -16, 150, 20,
                new TranslatableText("top.padding.chatimage.gui"), CONFIG.paddingTop,
                (float) this.height / 2, TOP, getSliderTooltip(getPaddingTooltipText(TOP))));
        this.addButton(new PaddingSlider(
                this.width / 2 + 4, this.height / 4 + 48 + -16, 150, 20,
                new TranslatableText("bottom.padding.chatimage.gui"), CONFIG.paddingBottom,
                (float) this.height / 2, BOTTOM, getSliderTooltip(getPaddingTooltipText(BOTTOM))));
        this.addButton(new LimitSlider(
                this.width / 2 - 154, this.height / 4 + 72 + -16, 150, 20,
                new TranslatableText("width.limit.chatimage.gui"), CONFIG.limitWidth,
                this.width, WIDTH, getSliderTooltip(getLimitTooltipText(WIDTH))));
        this.addButton(new LimitSlider(
                this.width / 2 + 4, this.height / 4 + 72 + -16, 150, 20,
                new TranslatableText("height.limit.chatimage.gui"), CONFIG.limitHeight,
                this.height, HEIGHT, getSliderTooltip(getLimitTooltipText(HEIGHT))));
        this.addButton(new ButtonWidget(
                this.width / 2 - 77, this.height / 4 + 96 + -16, 150, 20,
                new TranslatableText("gui.back"), (button) -> {
            if (this.client != null) {
                this.client.openScreen(this.parent);
            }
        }));


    }

    private Text getLimitTooltipText(LimitSlider.LimitType limitType) {
        if (limitType == WIDTH) {
            return new TranslatableText("width.limit.chatimage.tooltip");
        } else {
            return new TranslatableText("height.limit.chatimage.tooltip");
        }
    }

    private Text getPaddingTooltipText(PaddingSlider.PaddingType paddingType) {
        Text text;
        switch (paddingType) {
            case TOP:
                text = new TranslatableText("top.padding.chatimage.tooltip");
                break;
            case BOTTOM:
                text = new TranslatableText("bottom.padding.chatimage.tooltip");
                break;
            case LEFT:
                text = new TranslatableText("left.padding.chatimage.tooltip");
                break;
            case RIGHT:
                text = new TranslatableText("right.padding.chatimage.tooltip");
                break;
            default:
                return null;
        }
        return text;
    }
}
