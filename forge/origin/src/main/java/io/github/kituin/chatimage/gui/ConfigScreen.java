package io.github.kituin.chatimage.gui;

import io.github.kituin.ChatImageCode.ChatImageConfig;
import io.github.kituin.chatimage.widget.GifSlider;
import io.github.kituin.chatimage.widget.TimeOutSlider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static io.github.kituin.chatimage.ChatImage.CONFIG;
import static io.github.kituin.chatimage.tool.SimpleUtil.createTranslatableComponent;
import static io.github.kituin.chatimage.tool.SimpleUtil.composeGenericOptionComponent;
import static io.github.kituin.chatimage.tool.SimpleUtil.createButton;
@OnlyIn(Dist.CLIENT)
public class ConfigScreen extends ConfigRawScreen {
    public ConfigScreen(#Screen# screen) {
        super(createTranslatableComponent("config.chatimage.category"), screen);
    }
    
    protected void init() {
        super.init();
        this.#kituin$addRenderableWidget#(createButton(this.width / 2 - 154, this.height / 4 + 24 - 16, 150, 20, getNsfw(CONFIG.nsfw), (p_96270_) -> {
            CONFIG.nsfw = !CONFIG.nsfw;
            p_96270_.setMessage(getNsfw(CONFIG.nsfw));
            ChatImageConfig.saveConfig(CONFIG);
        }, #kituin$createButtonTooltip#(createTranslatableComponent("nsfw.chatimage.tooltip"))));
        this.#kituin$addRenderableWidget#(new GifSlider(this.width / 2 + 4, this.height / 4 + 24 - 16, 150, 20, createSliderTooltip(GifSlider.tooltip())));
        this.#kituin$addRenderableWidget#(new TimeOutSlider(this.width / 2 - 154, this.height / 4 + 48 - 16, 150, 20, createSliderTooltip(TimeOutSlider.tooltip())));
        this.#kituin$addRenderableWidget#(createButton(this.width / 2 + 4, this.height / 4 + 48 - 16, 150, 20,
                createTranslatableComponent("padding.chatimage.gui"),
                (button) -> {
                    if (this.minecraft != null) {
                        this.minecraft.setScreen(new LimitPaddingScreen(this));
                    }
                },
                #kituin$createButtonTooltip#(createTranslatableComponent("padding.chatimage.tooltip"))));
        this.#kituin$addRenderableWidget#(createButton(this.width / 2 - 154, this.height / 4 + 72 - 16, 150, 20, getCq(CONFIG.cqCode), (button) -> {
            CONFIG.cqCode = !CONFIG.cqCode;
            button.setMessage(getCq(CONFIG.cqCode));
            ChatImageConfig.saveConfig(CONFIG);
        }, #kituin$createButtonTooltip#(createTranslatableComponent("cq.chatimage.tooltip"))));
        this.#kituin$addRenderableWidget#(createButton(this.width / 2 + 4, this.height / 4 + 72 - 16, 150, 20,getDrag(CONFIG.dragUseCicode), (button) -> {
                    CONFIG.dragUseCicode = !CONFIG.dragUseCicode;
                    button.setMessage(getDrag(CONFIG.dragUseCicode));
                    ChatImageConfig.saveConfig(CONFIG);
                },#kituin$createButtonTooltip#(createTranslatableComponent("drag.chatimage.tooltip"))));
        this.#kituin$addRenderableWidget#(createButton(this.width / 2 - 154, this.height / 4 + 96 - 16, 150, 20,getUri(CONFIG.checkImageUri), (button) -> {
                    CONFIG.checkImageUri = !CONFIG.checkImageUri;
                    button.setMessage(getUri(CONFIG.checkImageUri));
                    ChatImageConfig.saveConfig(CONFIG);
                }));
        this.#kituin$addRenderableWidget#(createButton(this.width / 2 - 77, this.height / 4 + 120 - 16, 150, 20,
                createTranslatableComponent("gui.back"),
                (button) -> {
                    if (this.minecraft != null) {
                        this.minecraft.setScreen(this.parent);
                    }
                }));
    }

    private #MutableComponent# getCq(boolean enable) {
        return getEnable( "cq.chatimage.gui", enable);
    }
    private #MutableComponent# getNsfw(boolean enable) {
        return getEnable( "nsfw.chatimage.gui", !enable);
    }
    private #MutableComponent# getDrag(boolean enable) {
        return getEnable("drag.chatimage.gui", enable);
    }
    private #MutableComponent# getUri(boolean enable) {
        return getEnable("uri.chatimage.gui", enable);
    }
    public static #MutableComponent# getEnable(String key, boolean enable)
    {
        return composeGenericOptionComponent(createTranslatableComponent(key), createTranslatableComponent((enable ? "open" : "close") + ".chatimage.common"));
    }

}