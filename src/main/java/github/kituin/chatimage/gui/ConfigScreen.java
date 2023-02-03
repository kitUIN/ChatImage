package github.kituin.chatimage.gui;

import github.kituin.chatimage.config.ChatImageConfig;
import github.kituin.chatimage.widget.GifSlider;
import github.kituin.chatimage.widget.TimeOutSlider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.Narration;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import static github.kituin.chatimage.client.ChatImageClient.CONFIG;

@Environment(EnvType.CLIENT)
public class ConfigScreen extends Screen {

    public ConfigScreen(Text title) {
        super(title);

    }

    protected void init() {
        super.init();
        GridWidget gridWidget = new GridWidget();
        gridWidget.getMainPositioner().marginX(5).marginBottom(4).alignHorizontalCenter();
        GridWidget.Adder adder = gridWidget.createAdder(2);
        adder.add(ButtonWidget.builder(getNsfw(CONFIG.nsfw), (button) -> {
            CONFIG.nsfw = !CONFIG.nsfw;
            button.setMessage(getNsfw(CONFIG.nsfw));
            ChatImageConfig.saveConfig(CONFIG);
        }).tooltip(Tooltip.of(Text.translatable("nsfw.chatimage.tooltip"))).build());
        adder.add(new GifSlider());
        adder.add(new TimeOutSlider());
        adder.add(ButtonWidget.builder(Text.translatable("padding.chatimage.gui"), (button) -> {
            this.client.setScreen(new LimitPaddingScreen(Text.translatable("padding.chatimage.gui")));
        }).tooltip(Tooltip.of(Text.translatable("padding.chatimage.tooltip"))).build());
        gridWidget.recalculateDimensions();
        SimplePositioningWidget.setPos(gridWidget, 0, this.height / 5 - 12, this.width, this.height, 0.5F, 0.0F);
        this.addDrawableChild(gridWidget);
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawCenteredText(matrices, this.textRenderer,
                title, this.width / 2, this.height / 5 - 32, 16764108);
    }

    private MutableText getNsfw(boolean enable) {
        return Text.translatable(enable ? "open.nsfw.chatimage.gui" : "close.nsfw.chatimage.gui");
    }


}
