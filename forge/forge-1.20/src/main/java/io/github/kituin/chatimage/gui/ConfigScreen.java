package io.github.kituin.chatimage.gui;

import io.github.kituin.chatimage.widget.GifSlider;
import io.github.kituin.chatimage.widget.TimeOutSlider;
import io.github.kituin.ChatImageCode.ChatImageConfig;
import io.github.kituin.chatimage.ChatImage;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static net.minecraft.network.chat.CommonComponents.optionNameValue;

@OnlyIn(Dist.CLIENT)
public class ConfigScreen extends ConfigRawScreen {
    public ConfigScreen(Screen screen) {
        super(Component.translatable("config.chatimage.category"), screen);
    }


    protected void init() {
        super.init();
        this.addRenderableWidget(Button.builder(getNsfw(ChatImage.CONFIG.nsfw), (p_96270_) -> {
            ChatImage.CONFIG.nsfw = !ChatImage.CONFIG.nsfw;
            p_96270_.setMessage(getNsfw(ChatImage.CONFIG.nsfw));
            ChatImageConfig.saveConfig(ChatImage.CONFIG);
        }).bounds(this.width / 2 - 154, this.height / 4 + 24 - 16, 150, 20)
                .tooltip(Tooltip.create(Component.translatable("nsfw.chatimage.tooltip"))).build());
        this.addRenderableWidget(new GifSlider(this.width / 2 + 4, this.height / 4 + 24 - 16, 150, 20, createSliderTooltip(GifSlider.tooltip())));
        this.addRenderableWidget(new TimeOutSlider(this.width / 2 - 154, this.height / 4 + 48 - 16, 150, 20, createSliderTooltip(TimeOutSlider.tooltip())));
        this.addRenderableWidget(Button.builder(Component.translatable("padding.chatimage.gui"),
                        (button) -> {
                            if (this.minecraft != null) {
                                this.minecraft.setScreen(new LimitPaddingScreen(this));
                            }
                        }).bounds(this.width / 2 + 4, this.height / 4 + 48 - 16, 150, 20)
                .tooltip(Tooltip.create(Component.translatable("padding.chatimage.tooltip"))).build());
        this.addRenderableWidget(Button.builder(getCq(ChatImage.CONFIG.cqCode), (button) -> {
                    ChatImage.CONFIG.cqCode = !ChatImage.CONFIG.cqCode;
                    button.setMessage(getCq(ChatImage.CONFIG.cqCode));
                    ChatImageConfig.saveConfig(ChatImage.CONFIG);
                }).bounds(this.width / 2 - 154, this.height / 4 + 72 - 16, 150, 20)
                .tooltip(Tooltip.create(Component.translatable("cq.chatimage.tooltip"))).build());
        this.addRenderableWidget(Button.builder(getDrag(ChatImage.CONFIG.dragUseCicode), (button) -> {
                    ChatImage.CONFIG.dragUseCicode = !ChatImage.CONFIG.dragUseCicode;
                    button.setMessage(getDrag(ChatImage.CONFIG.dragUseCicode));
                    ChatImageConfig.saveConfig(ChatImage.CONFIG);
                }).bounds(this.width / 2 + 4, this.height / 4 + 72 - 16, 150, 20)
                .tooltip(Tooltip.create(Component.translatable("drag.chatimage.tooltip"))).build());
        this.addRenderableWidget(Button.builder(getUri(ChatImage.CONFIG.checkImageUri), (button) -> {
                    ChatImage.CONFIG.checkImageUri = !ChatImage.CONFIG.checkImageUri;
                    button.setMessage(getUri(ChatImage.CONFIG.checkImageUri));
                    ChatImageConfig.saveConfig(ChatImage.CONFIG);
                }).bounds(this.width / 2 - 154, this.height / 4 + 96 - 16, 150, 20)
                .build());

        this.addRenderableWidget(Button.builder(Component.translatable("gui.back"),
                        (button) -> {
                            if (this.minecraft != null) {
                                this.minecraft.setScreen(this.parent);
                            }
                        }).bounds(this.width / 2 - 77, this.height / 4 + 120 - 16, 150, 20)
                .build());
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
        return optionNameValue(Component.translatable(key),Component.translatable((enable ? "open" : "close") + ".chatimage.common"));
    }

}
