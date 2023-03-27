package github.kituin.chatimage.gui;

import github.kituin.chatimage.widget.LimitSlider;
import github.kituin.chatimage.widget.PaddingSlider;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static github.kituin.chatimage.ChatImage.CONFIG;
import static github.kituin.chatimage.widget.LimitSlider.LimitType.HEIGHT;
import static github.kituin.chatimage.widget.LimitSlider.LimitType.WIDTH;
import static github.kituin.chatimage.widget.PaddingSlider.PaddingType.*;

@OnlyIn(Dist.CLIENT)
public class LimitPaddingScreen extends ConfigRawScreen {


    public LimitPaddingScreen(Screen screen) {
        super(new TranslatableComponent("padding.chatimage.gui"), screen);
    }

    protected void init() {
        super.init();

        addRenderableWidget(new PaddingSlider(this.width / 2 - 154, this.height / 4 + 24 + -16, 150, 20, new TranslatableComponent("left.padding.chatimage.gui"),
                CONFIG.paddingLeft, this.width / 2, LEFT, createSliderTooltip(PaddingSlider.tooltip(LEFT))));
        addRenderableWidget(new PaddingSlider(this.width / 2 + 4, this.height / 4 + 24 + -16, 150, 20, new TranslatableComponent("right.padding.chatimage.gui"),
                CONFIG.paddingRight, this.width / 2, RIGHT, createSliderTooltip(PaddingSlider.tooltip(RIGHT))));
        addRenderableWidget(new PaddingSlider(this.width / 2 - 154, this.height / 4 + 48 + -16, 150, 20, new TranslatableComponent("top.padding.chatimage.gui"),
                CONFIG.paddingTop, this.height / 2, TOP, createSliderTooltip(PaddingSlider.tooltip(TOP))));
        addRenderableWidget(new PaddingSlider(this.width / 2 + 4, this.height / 4 + 48 + -16, 150, 20, new TranslatableComponent("bottom.padding.chatimage.gui"),
                CONFIG.paddingBottom, this.height / 2, BOTTOM, createSliderTooltip(PaddingSlider.tooltip(BOTTOM))));
        addRenderableWidget(new LimitSlider(this.width / 2 - 154, this.height / 4 + 72 + -16, 150, 20, new TranslatableComponent("width.limit.chatimage.gui"),
                CONFIG.limitWidth, this.width, WIDTH, createSliderTooltip(LimitSlider.tooltip(WIDTH))));
        addRenderableWidget(new LimitSlider(this.width / 2 + 4, this.height / 4 + 72 + -16, 150, 20, new TranslatableComponent("height.limit.chatimage.gui"),
                CONFIG.limitHeight, this.height, HEIGHT, createSliderTooltip(LimitSlider.tooltip(HEIGHT))));
        addRenderableWidget(new Button(this.width / 2 - 77, this.height / 4 + 96 - 16, 150, 20,
                new TranslatableComponent("gui.back"),
                (button) -> this.minecraft.setScreen(this.parent)));

    }

}
