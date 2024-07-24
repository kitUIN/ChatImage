package io.github.kituin.chatimage.gui;

import io.github.kituin.chatimage.widget.LimitSlider;
import io.github.kituin.chatimage.widget.PaddingSlider;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static io.github.kituin.chatimage.ChatImage.CONFIG;

@OnlyIn(Dist.CLIENT)
public class LimitPaddingScreen extends ConfigRawScreen {


    public LimitPaddingScreen(Screen screen) {
        super(new TranslatableComponent("padding.chatimage.gui"), screen);
    }

    protected void init() {
        super.init();

        addRenderableWidget(new PaddingSlider(this.width / 2 - 154, this.height / 4 + 24 + -16, 150, 20, new TranslatableComponent("left.padding.chatimage.gui"),
                CONFIG.paddingLeft, (float) this.width / 2, PaddingSlider.PaddingType.LEFT, createSliderTooltip(PaddingSlider.tooltip(PaddingSlider.PaddingType.LEFT))));
        addRenderableWidget(new PaddingSlider(this.width / 2 + 4, this.height / 4 + 24 + -16, 150, 20, new TranslatableComponent("right.padding.chatimage.gui"),
                CONFIG.paddingRight, (float) this.width / 2, PaddingSlider.PaddingType.RIGHT, createSliderTooltip(PaddingSlider.tooltip(PaddingSlider.PaddingType.RIGHT))));
        addRenderableWidget(new PaddingSlider(this.width / 2 - 154, this.height / 4 + 48 + -16, 150, 20, new TranslatableComponent("top.padding.chatimage.gui"),
                CONFIG.paddingTop, (float) this.height / 2, PaddingSlider.PaddingType.TOP, createSliderTooltip(PaddingSlider.tooltip(PaddingSlider.PaddingType.TOP))));
        addRenderableWidget(new PaddingSlider(this.width / 2 + 4, this.height / 4 + 48 + -16, 150, 20, new TranslatableComponent("bottom.padding.chatimage.gui"),
                CONFIG.paddingBottom, (float) this.height / 2, PaddingSlider.PaddingType.BOTTOM, createSliderTooltip(PaddingSlider.tooltip(PaddingSlider.PaddingType.BOTTOM))));
        addRenderableWidget(new LimitSlider(this.width / 2 - 154, this.height / 4 + 72 + -16, 150, 20, new TranslatableComponent("width.limit.chatimage.gui"),
                CONFIG.limitWidth, this.width, LimitSlider.LimitType.WIDTH, createSliderTooltip(LimitSlider.tooltip(LimitSlider.LimitType.WIDTH))));
        addRenderableWidget(new LimitSlider(this.width / 2 + 4, this.height / 4 + 72 + -16, 150, 20, new TranslatableComponent("height.limit.chatimage.gui"),
                CONFIG.limitHeight, this.height, LimitSlider.LimitType.HEIGHT, createSliderTooltip(LimitSlider.tooltip(LimitSlider.LimitType.HEIGHT))));
        addRenderableWidget(new Button(this.width / 2 - 77, this.height / 4 + 96 - 16, 150, 20,
                new TranslatableComponent("gui.back"),
                (button) -> {
                    if (this.minecraft != null) {
                        this.minecraft.setScreen(this.parent);
                    }
                }));

    }

}
