package io.github.kituin.chatimage.gui;

import io.github.kituin.chatimage.widget.GifSlider;
import io.github.kituin.chatimage.widget.TimeOutSlider;
import io.github.kituin.ChatImageCode.ChatImageConfig;
import io.github.kituin.chatimage.ChatImage;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ConfigScreen extends ConfigRawScreen {
    public ConfigScreen(Screen screen) {
        super(new TranslationTextComponent("config.chatimage.category"), screen);
    }


    protected void init() {
        super.init();
        this.addButton(new GifSlider(this.width / 2 + 4, this.height / 4 + 24 - 16, 150, 20, createSliderTooltip(GifSlider.tooltip())));
        this.addButton(new TimeOutSlider(this.width / 2 - 154, this.height / 4 + 48 - 16, 150, 20, createSliderTooltip(TimeOutSlider.tooltip())));
        this.addButton(new Button(this.width / 2 - 154, this.height / 4 + 24 - 16, 150, 20, getNsfw(ChatImage.CONFIG.nsfw), (p_96270_) -> {
            ChatImage.CONFIG.nsfw = !ChatImage.CONFIG.nsfw;
            p_96270_.setMessage(getNsfw(ChatImage.CONFIG.nsfw));
            ChatImageConfig.saveConfig(ChatImage.CONFIG);
        }, createButtonTooltip(new TranslationTextComponent("nsfw.chatimage.tooltip"))));
        this.addButton(new Button(this.width / 2 + 4, this.height / 4 + 48 - 16, 150, 20,
                new TranslationTextComponent("padding.chatimage.gui"),
                (button) -> {
                    if (this.minecraft != null) {
                        this.minecraft.setScreen(new LimitPaddingScreen(this));
                    }
                },createButtonTooltip(new TranslationTextComponent("padding.chatimage.tooltip"))));
        this.addButton(new Button(this.width / 2 - 154, this.height / 4 + 72 - 16, 150, 20, getCq(ChatImage.CONFIG.cqCode), (button) -> {
            ChatImage.CONFIG.cqCode = !ChatImage.CONFIG.cqCode;
            button.setMessage(getCq(ChatImage.CONFIG.cqCode));
            ChatImageConfig.saveConfig(ChatImage.CONFIG);
        }, createButtonTooltip(new TranslationTextComponent("cq.chatimage.tooltip"))));
        this.addButton(new Button(this.width / 2 + 4, this.height / 4 + 72 - 16, 150, 20,getDrag(ChatImage.CONFIG.dragUseCicode), (button) -> {
            ChatImage.CONFIG.dragUseCicode = !ChatImage.CONFIG.dragUseCicode;
            button.setMessage(getDrag(ChatImage.CONFIG.dragUseCicode));
            ChatImageConfig.saveConfig(ChatImage.CONFIG);
        },createButtonTooltip(new TranslationTextComponent("drag.chatimage.tooltip"))));
        this.addButton(new Button(this.width / 2 - 154, this.height / 4 + 96 - 16, 150, 20,getUri(ChatImage.CONFIG.checkImageUri), (button) -> {
            ChatImage.CONFIG.checkImageUri = !ChatImage.CONFIG.checkImageUri;
            button.setMessage(getUri(ChatImage.CONFIG.checkImageUri));
            ChatImageConfig.saveConfig(ChatImage.CONFIG);
        }));

        this.addButton(new Button(this.width / 2 - 77, this.height / 4 + 120 - 16, 150, 20,
                new TranslationTextComponent("gui.back"),
                (button) -> {
                    if (this.minecraft != null) {
                        this.minecraft.setScreen(this.parent);
                    }
                }));
    }


    private ITextComponent getCq(boolean enable) {
        return getEnable( "cq.chatimage.gui", enable);
    }
    private ITextComponent getNsfw(boolean enable) {
        return getEnable( "nsfw.chatimage.gui", !enable);
    }
    private ITextComponent getDrag(boolean enable) {
        return getEnable("drag.chatimage.gui", enable);
    }
    private ITextComponent getUri(boolean enable) {
        return getEnable("uri.chatimage.gui", enable);
    }
    public static ITextComponent getEnable(String key,boolean enable)
    {
        return optionNameValue(new TranslationTextComponent(key),new TranslationTextComponent((enable ? "open" : "close") + ".chatimage.common"));
    }

    public static ITextComponent optionNameValue(ITextComponent pCaption, ITextComponent pValueMessage) {
        return new TranslationTextComponent("options.generic_value", pCaption, pValueMessage);
    }

}
