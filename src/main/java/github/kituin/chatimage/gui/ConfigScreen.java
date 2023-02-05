package github.kituin.chatimage.gui;


import github.kituin.chatimage.config.ChatImageConfig;
import github.kituin.chatimage.widget.GifSlider;
import github.kituin.chatimage.widget.TimeOutSlider;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.quiltmc.loader.api.minecraft.ClientOnly;

import static github.kituin.chatimage.client.ChatImageClient.CONFIG;

@ClientOnly
public class ConfigScreen extends ConfigRawScreen {

    public ConfigScreen(Screen screen) {
        super(Text.translatable("config.chatimage.category"),screen);
    }


    protected void init() {
        super.init();
        this.addDrawableChild(ButtonWidget.builder(getNsfw(CONFIG.nsfw), (button) -> {
            CONFIG.nsfw = !CONFIG.nsfw;
            button.setMessage(getNsfw(CONFIG.nsfw));
            ChatImageConfig.saveConfig(CONFIG);
        }).tooltip(Tooltip.create(Text.translatable("nsfw.chatimage.tooltip")))
                .positionAndSize(this.width / 2 - 154, this.height / 4 + 24 + -16, 150, 20).build());
        this.addDrawableChild(new GifSlider(this.width / 2 + 4, this.height / 4 + 24 + -16, 150, 20));
        this.addDrawableChild(new TimeOutSlider(this.width / 2 - 154, this.height / 4 + 48 + -16, 150, 20));
		addDrawableChild(ButtonWidget.builder(Text.translatable("padding.chatimage.gui"), (button) -> {
            this.client.setScreen(new LimitPaddingScreen(this));
        }).tooltip(Tooltip.create(Text.translatable("padding.chatimage.tooltip")))
                .positionAndSize(this.width / 2 + 4, this.height / 4 + 48 + -16, 150, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.translatable("gui.back"), (button) -> {
            this.client.setScreen(this.parent);
        }).positionAndSize(this.width / 2 - 77, this.height / 4 + 72 + -16, 150, 20).build());

    }


    private Text getNsfw(boolean enable) {
        return Text.translatable(enable ? "close.nsfw.chatimage.gui" : "open.nsfw.chatimage.gui");
    }


}
