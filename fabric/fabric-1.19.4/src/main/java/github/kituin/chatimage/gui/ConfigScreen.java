package github.kituin.chatimage.gui;


import github.kituin.chatimage.widget.GifSlider;
import github.kituin.chatimage.widget.TimeOutSlider;
import io.github.kituin.ChatImageCode.ChatImageConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import static github.kituin.chatimage.client.ChatImageClient.CONFIG;
import static net.minecraft.screen.ScreenTexts.composeGenericOptionText;

@Environment(EnvType.CLIENT)
public class ConfigScreen extends Screen {
    private Screen parent;
    public ConfigScreen() {
        super(Text.translatable("config.chatimage.category"));

    }

    public ConfigScreen(Screen screen) {
        super(Text.translatable("config.chatimage.category"));
        this.parent = screen;
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
            if (this.client != null) {
                this.client.setScreen(new LimitPaddingScreen(this));
            }
        }).tooltip(Tooltip.of(Text.translatable("padding.chatimage.tooltip"))).build());
        adder.add(ButtonWidget.builder(getCq(CONFIG.cqCode), (button) -> {
            CONFIG.cqCode = !CONFIG.cqCode;
            button.setMessage(getCq(CONFIG.cqCode));
            ChatImageConfig.saveConfig(CONFIG);
        }).tooltip(Tooltip.of(Text.translatable("cq.chatimage.tooltip"))).build());
        adder.add(ButtonWidget.builder(getDrag(CONFIG.dragUseCicode), (button) -> {
            CONFIG.dragUseCicode = !CONFIG.dragUseCicode;
            button.setMessage(getDrag(CONFIG.dragUseCicode));
            ChatImageConfig.saveConfig(CONFIG);
        }).tooltip(Tooltip.of(Text.translatable("drag.chatimage.tooltip"))).build());
        adder.add(ButtonWidget.builder(getUri(CONFIG.checkImageUri), (button) -> {
            CONFIG.checkImageUri = !CONFIG.checkImageUri;
            button.setMessage(getUri(CONFIG.checkImageUri));
            ChatImageConfig.saveConfig(CONFIG);
        }).build());
        adder.add(ButtonWidget.builder(Text.translatable("gui.back"), (button) -> {
            if (this.client != null) {
                this.client.setScreen(this.parent);
            }
        }).build(), 2);
        gridWidget.refreshPositions();
        SimplePositioningWidget.setPos(gridWidget, 0, this.height / 3 - 12, this.width, this.height, 0.5F, 0.0F);
        gridWidget.forEachChild(this::addDrawableChild);
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawCenteredTextWithShadow(matrices, this.textRenderer,
                title, this.width / 2, this.height / 3 - 32, 16764108);
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
