package github.kituin.chatimage.gui;

import github.kituin.chatimage.widget.GifSlider;
import github.kituin.chatimage.widget.TimeOutSlider;
import io.github.kituin.ChatImageCode.ChatImageConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;

import static github.kituin.chatimage.client.ChatImageClient.CONFIG;
import static github.kituin.chatimage.widget.SettingSliderWidget.composeGenericOptionText;

@Environment(EnvType.CLIENT)
public class ConfigScreen extends ConfigRawScreen {

    public ConfigScreen(Screen screen) {
        super(new TranslatableText("config.chatimage.category"), screen);
    }


    protected void init() {
        super.init();
        this.addButton(new ButtonWidget(this.width / 2 - 154, this.height / 4 + 24 + -16, 150, 20, getNsfw(CONFIG.nsfw), (button) -> {
            CONFIG.nsfw = !CONFIG.nsfw;
            button.setMessage(getNsfw(CONFIG.nsfw));
            ChatImageConfig.saveConfig(CONFIG);
        }, getButtonTooltip(new TranslatableText("nsfw.chatimage.tooltip"))));
        this.addButton(new GifSlider(this.width / 2 + 4, this.height / 4 + 24 + -16, 150, 20, getSliderTooltip(new TranslatableText("gif.chatimage.tooltip"))));
        this.addButton(new TimeOutSlider(this.width / 2 - 154, this.height / 4 + 48 + -16, 150, 20, getSliderTooltip(new TranslatableText("timeout.chatimage.tooltip"))));
        this.addButton(new ButtonWidget(this.width / 2 + 4, this.height / 4 + 48 + -16, 150, 20, new TranslatableText("padding.chatimage.gui"), (button) -> {
            if (this.client != null) {
                this.client.openScreen(new LimitPaddingScreen(this));
            }
        }, getButtonTooltip(new TranslatableText("padding.chatimage.tooltip"))));
        this.addButton(new ButtonWidget(this.width / 2 - 154, this.height / 4 + 72 - 16, 150, 20, getCq(CONFIG.cqCode), (button) -> {
            CONFIG.cqCode = !CONFIG.cqCode;
            button.setMessage(getCq(CONFIG.cqCode));
            ChatImageConfig.saveConfig(CONFIG);
        }, getButtonTooltip(new TranslatableText("cq.chatimage.tooltip"))));
        this.addButton(new ButtonWidget(this.width / 2 + 4, this.height / 4 + 72 - 16, 150, 20, getDrag(CONFIG.dragUseCicode), (button) -> {
            CONFIG.dragUseCicode = !CONFIG.dragUseCicode;
            button.setMessage(getDrag(CONFIG.dragUseCicode));
            ChatImageConfig.saveConfig(CONFIG);
        }, getButtonTooltip(new TranslatableText("drag.chatimage.tooltip"))));
        this.addButton(new ButtonWidget(this.width / 2 - 154, this.height / 4 + 96 - 16, 150, 20, getUri(CONFIG.checkImageUri), (button) -> {
            CONFIG.checkImageUri = !CONFIG.checkImageUri;
            button.setMessage(getUri(CONFIG.checkImageUri));
            ChatImageConfig.saveConfig(CONFIG);
        }));
        this.addButton(new ButtonWidget(this.width / 2 - 77, this.height / 4 + 120 + -16, 150, 20, new TranslatableText("gui.back"), (button) -> {
            if (this.client != null) {
                this.client.openScreen(this.parent);
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
        return composeGenericOptionText(new TranslatableText(key),new TranslatableText((enable ? "open" : "close") + ".chatimage.common"));
    }
}
