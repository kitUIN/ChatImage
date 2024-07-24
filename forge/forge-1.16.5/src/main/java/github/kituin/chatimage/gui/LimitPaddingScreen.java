package github.kituin.chatimage.gui;

import github.kituin.chatimage.widget.LimitSlider;
import github.kituin.chatimage.widget.PaddingSlider;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static github.kituin.chatimage.ChatImage.CONFIG;
import static github.kituin.chatimage.widget.LimitSlider.LimitType.HEIGHT;
import static github.kituin.chatimage.widget.LimitSlider.LimitType.WIDTH;
import static github.kituin.chatimage.widget.PaddingSlider.PaddingType.*;

@OnlyIn(Dist.CLIENT)
public class LimitPaddingScreen extends ConfigRawScreen {


    public LimitPaddingScreen(Screen screen) {
        super(new TranslationTextComponent("padding.chatimage.gui"), screen);
    }

    protected void init() {
        super.init();


        addButton(new PaddingSlider(this.width / 2 + 4, this.height / 4 + 24 + -16, 150, 20,
                new TranslationTextComponent("right.padding.chatimage.gui"),
                CONFIG.paddingRight, (float) this.width / 2, RIGHT, createSliderTooltip(PaddingSlider.tooltip(RIGHT))));

        addButton(new PaddingSlider(this.width / 2 + 4, this.height / 4 + 48 + -16, 150, 20,
                new TranslationTextComponent("bottom.padding.chatimage.gui"),
                CONFIG.paddingBottom, (float) this.height / 2, BOTTOM, createSliderTooltip(PaddingSlider.tooltip(BOTTOM))));
        addButton(new LimitSlider(this.width / 2 + 4, this.height / 4 + 72 + -16, 150, 20,
                new TranslationTextComponent("height.limit.chatimage.gui"),
                CONFIG.limitHeight, this.height, HEIGHT, createSliderTooltip(LimitSlider.tooltip(HEIGHT))));
        addButton(new PaddingSlider(this.width / 2 - 154, this.height / 4 + 24 + -16, 150, 20,
                new TranslationTextComponent("left.padding.chatimage.gui"),
                CONFIG.paddingLeft, (float) this.width / 2, LEFT, createSliderTooltip(PaddingSlider.tooltip(LEFT))));
        addButton(new PaddingSlider(this.width / 2 - 154, this.height / 4 + 48 + -16, 150, 20,
                new TranslationTextComponent("top.padding.chatimage.gui"),
                CONFIG.paddingTop, (float) this.height / 2, TOP, createSliderTooltip(PaddingSlider.tooltip(TOP))));
        addButton(new LimitSlider(this.width / 2 - 154, this.height / 4 + 72 + -16, 150, 20,
                new TranslationTextComponent("width.limit.chatimage.gui"),
                CONFIG.limitWidth, this.width, WIDTH, createSliderTooltip(LimitSlider.tooltip(WIDTH))));

        addButton(new Button(this.width / 2 - 77, this.height / 4 + 96 - 16, 150, 20,
                new TranslationTextComponent("gui.back"),
                (button) -> {
                    if (this.minecraft != null) {
                        this.minecraft.setScreen(this.parent);
                    }
                }));

    }

}
