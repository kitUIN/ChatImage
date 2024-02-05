package github.kituin.chatimage.gui;

import github.kituin.chatimage.widget.GifSlider;
import github.kituin.chatimage.widget.TimeOutSlider;
import io.github.kituin.ChatImageCode.ChatImageConfig;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static github.kituin.chatimage.ChatImage.CONFIG;
import static net.minecraft.network.chat.CommonComponents.optionNameValue;

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
        this.addRenderableWidget(new Button(this.width / 2 + 4, this.height / 4 + 72 - 16, 150, 20,getDrag(CONFIG.dragUseCicode), (button) -> {
            CONFIG.dragUseCicode = !CONFIG.dragUseCicode;
            button.setMessage(getDrag(CONFIG.dragUseCicode));
            ChatImageConfig.saveConfig(CONFIG);
        },createButtonTooltip(new TranslatableComponent("drag.chatimage.tooltip"))));
        this.addRenderableWidget(new Button(this.width / 2 - 154, this.height / 4 + 96 - 16, 150, 20,getUri(CONFIG.checkImageUri), (button) -> {
            CONFIG.checkImageUri = !CONFIG.checkImageUri;
            button.setMessage(getUri(CONFIG.checkImageUri));
            ChatImageConfig.saveConfig(CONFIG);
        }));
        this.addRenderableWidget(new Button(this.width / 2 - 77, this.height / 4 + 120 - 16, 150, 20,
                new TranslatableComponent("gui.back"),
                (button) -> {
                    if (this.minecraft != null) {
                        this.minecraft.setScreen(this.parent);
                    }
                }));
    }


    private MutableComponent getCq(boolean enable) {
        return getEnable( "cq.chatimage.gui", enable);
    }
    private MutableComponent getNsfw(boolean enable) {
        return getEnable( "nsfw.chatimage.gui", !enable);
    }
    private MutableComponent getDrag(boolean enable) {
        return getEnable("drag.chatimage.gui", enable);
    }
    private MutableComponent getUri(boolean enable) {
        return getEnable("uri.chatimage.gui", enable);
    }
    public static MutableComponent getEnable(String key,boolean enable)
    {
        return optionNameValue(new TranslatableComponent(key),new TranslatableComponent((enable ? "open" : "close") + ".chatimage.common"));
    }


}
