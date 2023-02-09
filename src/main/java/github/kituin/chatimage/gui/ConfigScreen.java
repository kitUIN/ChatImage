package github.kituin.chatimage.gui;

import github.kituin.chatimage.config.ChatImageConfig;
import github.kituin.chatimage.widget.GifSlider;
import github.kituin.chatimage.widget.TimeOutSlider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import static github.kituin.chatimage.client.ChatImageClient.CONFIG;

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
            this.client.openScreen(new LimitPaddingScreen(this));
        }, getButtonTooltip(new TranslatableText("padding.chatimage.tooltip"))));
        this.addButton(new ButtonWidget(this.width / 2 - 77, this.height / 4 + 72 + -16, 150, 20, new TranslatableText("gui.back"), (button) -> {
            this.client.openScreen(this.parent);
        }));
    }

//    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
//        super.render(matrices, mouseX, mouseY, delta);
//    }

    private MutableText getNsfw(boolean enable) {
        return new TranslatableText(enable ? "close.nsfw.chatimage.gui" : "open.nsfw.chatimage.gui");
    }

}
