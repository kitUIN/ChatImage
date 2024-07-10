package github.kituin.chatimage.gui;

import github.kituin.chatimage.widget.GifSlider;
import github.kituin.chatimage.widget.TimeOutSlider;
import io.github.kituin.ChatImageCode.ChatImageConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import static github.kituin.chatimage.client.ChatImageClient.CONFIG;
import static net.minecraft.screen.ScreenTexts.composeGenericOptionText;

@Environment(EnvType.CLIENT)
public class ConfigScreen extends ConfigRawScreen {

    public ConfigScreen(Screen screen) {
        super(Text.translatable("config.chatimage.category"), screen);
    }


    protected void init() {
        super.init();
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 154, this.height / 4 + 24 + -16, 150, 20, getNsfw(CONFIG.nsfw), (button) -> {
            CONFIG.nsfw = !CONFIG.nsfw;
            button.setMessage(getNsfw(CONFIG.nsfw));
            ChatImageConfig.saveConfig(CONFIG);
        }, getButtonTooltip(Text.translatable("nsfw.chatimage.tooltip"))));
        this.addDrawableChild(new GifSlider(this.width / 2 + 4, this.height / 4 + 24 + -16, 150, 20, getSliderTooltip(Text.translatable("gif.chatimage.tooltip"))));
        this.addDrawableChild(new TimeOutSlider(this.width / 2 - 154, this.height / 4 + 48 + -16, 150, 20, getSliderTooltip(Text.translatable("timeout.chatimage.tooltip"))));
        this.addDrawableChild(new ButtonWidget(this.width / 2 + 4, this.height / 4 + 48 + -16, 150, 20, Text.translatable("padding.chatimage.gui"), (button) -> {
            if (this.client != null) {
                this.client.setScreen(new LimitPaddingScreen(this));
            }
        }, getButtonTooltip(Text.translatable("padding.chatimage.tooltip"))));
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 154, this.height / 4 + 72 - 16, 150, 20, getCq(CONFIG.cqCode), (button) -> {
            CONFIG.cqCode = !CONFIG.cqCode;
            button.setMessage(getCq(CONFIG.cqCode));
            ChatImageConfig.saveConfig(CONFIG);
        }, getButtonTooltip(Text.translatable("cq.chatimage.tooltip"))));
        this.addDrawableChild(new ButtonWidget(this.width / 2 + 4, this.height / 4 + 72 - 16, 150, 20, getDrag(CONFIG.dragUseCicode), (button) -> {
            CONFIG.dragUseCicode = !CONFIG.dragUseCicode;
            button.setMessage(getDrag(CONFIG.dragUseCicode));
            ChatImageConfig.saveConfig(CONFIG);
        }, getButtonTooltip(Text.translatable("drag.chatimage.tooltip"))));
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 154, this.height / 4 + 96 - 16, 150, 20, getUri(CONFIG.checkImageUri), (button) -> {
            CONFIG.checkImageUri = !CONFIG.checkImageUri;
            button.setMessage(getUri(CONFIG.checkImageUri));
            ChatImageConfig.saveConfig(CONFIG);
        }));
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 77, this.height / 4 + 120 + -16, 150, 20, Text.translatable("gui.back"), (button) -> {
            if (this.client != null) {
                this.client.setScreen(this.parent);
            }
        }));
    }

    private MutableText getCq(boolean enable) {
        return getEnable( "cq.chatimage.gui", enable);
    }
    private MutableText getNsfw(boolean enable) {
        return getEnable( "nsfw.chatimage.gui", !enable);
    }
    private MutableText getDrag(boolean enable) {
        return getEnable("drag.chatimage.gui", enable);
    }
    private MutableText getUri(boolean enable) {
        return getEnable("uri.chatimage.gui", enable);
    }
    public static MutableText getEnable(String key,boolean enable)
    {
        return composeGenericOptionText(Text.translatable(key),Text.translatable((enable ? "open" : "close") + ".chatimage.common"));
    }
}
