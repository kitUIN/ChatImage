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

import static github.kituin.chatimage.client.ChatImageClient.CONFIG;

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
            this.client.setScreen(new LimitPaddingScreen(this));
        }, getButtonTooltip(Text.translatable("padding.chatimage.tooltip"))));
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 77, this.height / 4 + 72 + -16, 150, 20, Text.translatable("gui.back"), (button) -> {
            this.client.setScreen(this.parent);
        }));
    }

//    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
//        super.render(matrices, mouseX, mouseY, delta);
//    }

    private MutableText getNsfw(boolean enable) {
        return Text.translatable(enable ? "open.nsfw.chatimage.gui" : "close.nsfw.chatimage.gui");
    }

}
