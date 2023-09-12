package github.kituin.chatimage.gui;

import github.kituin.chatimage.config.ChatImageConfig;
import github.kituin.chatimage.widget.GifSlider;
import github.kituin.chatimage.widget.TimeOutSlider;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static github.kituin.chatimage.ChatImage.CONFIG;

@OnlyIn(Dist.CLIENT)
public class ConfigScreen extends ConfigRawScreen {
    public ConfigScreen(Screen screen) {
        super(new TranslationTextComponent("config.chatimage.category"), screen);
    }


    protected void init() {
        super.init();
        this.addButton(new GifSlider(this.width / 2 + 4, this.height / 4 + 24 - 16, 150, 20, createSliderTooltip(GifSlider.tooltip())));
        this.addButton(new TimeOutSlider(this.width / 2 - 154, this.height / 4 + 48 - 16, 150, 20, createSliderTooltip(TimeOutSlider.tooltip())));
        this.addButton(new Button(this.width / 2 - 154, this.height / 4 + 24 - 16, 150, 20, getNsfw(CONFIG.nsfw), (p_96270_) -> {
            CONFIG.nsfw = !CONFIG.nsfw;
            p_96270_.setMessage(getNsfw(CONFIG.nsfw));
            ChatImageConfig.saveConfig(CONFIG);
        }, createButtonTooltip(new TranslationTextComponent("nsfw.chatimage.tooltip"))));
        this.addButton(new Button(this.width / 2 + 4, this.height / 4 + 48 - 16, 150, 20,
                new TranslationTextComponent("padding.chatimage.gui"),
                (button) -> {
                    if (this.minecraft != null) {
                        this.minecraft.displayGuiScreen(new LimitPaddingScreen(this));
                    }
                },createButtonTooltip(new TranslationTextComponent("padding.chatimage.tooltip"))));
        this.addButton(new Button(this.width / 2 - 77, this.height / 4 + 72 - 16, 150, 20,
                new TranslationTextComponent("gui.back"),
                (button) -> {
                    if (this.minecraft != null) {
                        this.minecraft.displayGuiScreen(this.parent);
                    }
                }));
    }


    private TextComponent getNsfw(boolean enable) {
        return new TranslationTextComponent(enable ? "close.nsfw.chatimage.gui" : "open.nsfw.chatimage.gui");
    }


}
