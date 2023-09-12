package github.kituin.chatimage.gui;

import github.kituin.chatimage.config.ChatImageConfig;
import github.kituin.chatimage.widget.GifSlider;
import github.kituin.chatimage.widget.TimeOutSlider;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static github.kituin.chatimage.ChatImage.CONFIG;

@OnlyIn(Dist.CLIENT)
public class ConfigScreen extends ConfigRawScreen {
    public ConfigScreen(Screen screen) {
        super(new TranslatableComponent("config.chatimage.category"), screen);
    }


    protected void init() {
        super.init();
        this.addRenderableWidget(new Button(this.width / 2 - 154, this.height / 4 + 24 - 16, 150, 20, getNsfw(CONFIG.nsfw), (p_96270_) -> {
            CONFIG.nsfw = !CONFIG.nsfw;
            p_96270_.setMessage(getNsfw(CONFIG.nsfw));
            ChatImageConfig.saveConfig(CONFIG);
        }, createButtonTooltip(new TranslatableComponent("nsfw.chatimage.tooltip"))));
        this.addRenderableWidget(new GifSlider(this.width / 2 + 4, this.height / 4 + 24 - 16, 150, 20, createSliderTooltip(GifSlider.tooltip())));
        this.addRenderableWidget(new TimeOutSlider(this.width / 2 - 154, this.height / 4 + 48 - 16, 150, 20, createSliderTooltip(TimeOutSlider.tooltip())));
        this.addRenderableWidget(new Button(this.width / 2 + 4, this.height / 4 + 48 - 16, 150, 20,
                new TranslatableComponent("padding.chatimage.gui"),
                (button) -> {
                    if (this.minecraft != null) {
                        this.minecraft.setScreen(new LimitPaddingScreen(this));
                    }
                },
                createButtonTooltip(new TranslatableComponent("padding.chatimage.tooltip"))));
        this.addRenderableWidget(new Button(this.width / 2 - 154, this.height / 4 + 72 - 16, 150, 20, getCq(CONFIG.cqCode), (button) -> {
            CONFIG.cqCode = !CONFIG.cqCode;
            button.setMessage(getCq(CONFIG.cqCode));
            ChatImageConfig.saveConfig(CONFIG);
        }, createButtonTooltip(new TranslatableComponent("cq.chatimage.tooltip"))));
        this.addRenderableWidget(new Button(this.width / 2 - 77, this.height / 4 + 96 - 16, 150, 20,
                new TranslatableComponent("gui.back"),
                (button) -> {
                    if (this.minecraft != null) {
                        this.minecraft.setScreen(this.parent);
                    }
                }));
    }


    private MutableComponent getCq(boolean enable) {
        return new TranslatableComponent(enable ? "open.cq.chatimage.gui" : "close.cq.chatimage.gui");
    }
    private MutableComponent getNsfw(boolean enable) {
        return new TranslatableComponent(enable ? "close.nsfw.chatimage.gui" : "open.nsfw.chatimage.gui");
    }


}
