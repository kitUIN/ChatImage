package io.github.kituin.chatimage.gui;

import io.github.kituin.chatimage.widget.GifSlider;
import io.github.kituin.chatimage.widget.TimeOutSlider;
import io.github.kituin.ChatImageCode.ChatImageConfig;
import io.github.kituin.chatimage.client.ChatImageClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import static net.minecraft.screen.ScreenTexts.composeGenericOptionText;

@Environment(EnvType.CLIENT)
public class ConfigScreen extends Screen {
    private final Screen parent;

    public ConfigScreen() {
        this(null);
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
        adder.add(ButtonWidget.builder(getNsfw(ChatImageClient.CONFIG.nsfw), (button) -> {
            ChatImageClient.CONFIG.nsfw = !ChatImageClient.CONFIG.nsfw;
            button.setMessage(getNsfw(ChatImageClient.CONFIG.nsfw));
            ChatImageConfig.saveConfig(ChatImageClient.CONFIG);
        }).tooltip(Tooltip.of(Text.translatable("nsfw.chatimage.tooltip"))).build());
        adder.add(new GifSlider());
        adder.add(new TimeOutSlider());
        adder.add(ButtonWidget.builder(Text.translatable("padding.chatimage.gui"), (button) -> {
            if (this.client != null) {
                this.client.setScreen(new LimitPaddingScreen(this));
            }
        }).tooltip(Tooltip.of(Text.translatable("padding.chatimage.tooltip"))).build());
        adder.add(ButtonWidget.builder(getCq(ChatImageClient.CONFIG.cqCode), (button) -> {
            ChatImageClient.CONFIG.cqCode = !ChatImageClient.CONFIG.cqCode;
            button.setMessage(getCq(ChatImageClient.CONFIG.cqCode));
            ChatImageConfig.saveConfig(ChatImageClient.CONFIG);
        }).tooltip(Tooltip.of(Text.translatable("cq.chatimage.tooltip"))).build());

        adder.add(ButtonWidget.builder(getDrag(ChatImageClient.CONFIG.dragUseCicode), (button) -> {
            ChatImageClient.CONFIG.dragUseCicode = !ChatImageClient.CONFIG.dragUseCicode;
            button.setMessage(getDrag(ChatImageClient.CONFIG.dragUseCicode));
            ChatImageConfig.saveConfig(ChatImageClient.CONFIG);
        }).tooltip(Tooltip.of(Text.translatable("drag.chatimage.tooltip"))).build());
        adder.add(ButtonWidget.builder(getUri(ChatImageClient.CONFIG.checkImageUri), (button) -> {
            ChatImageClient.CONFIG.checkImageUri = !ChatImageClient.CONFIG.checkImageUri;
            button.setMessage(getUri(ChatImageClient.CONFIG.checkImageUri));
            ChatImageConfig.saveConfig(ChatImageClient.CONFIG);
        }).build());
        adder.add(ButtonWidget.builder(Text.translatable("gui.back"), (button) -> {
            if (this.client != null) {
                this.client.setScreen(this.parent);
            }
        }).build(), 2);
        gridWidget.recalculateDimensions();
        SimplePositioningWidget.setPos(gridWidget, 0, this.height / 3 - 12, this.width, this.height, 0.5F, 0.0F);
        this.addDrawableChild(gridWidget);
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawCenteredText(matrices, this.textRenderer,
                title, this.width / 2, this.height / 3 - 32, 16764108);
//        CheckChannelConnect(matrices);
    }

//    private void CheckChannelConnect(MatrixStack matrices) {
//        MutableText channelStatus = composeGenericOptionText(Text.translatable("channel.chatimage.gui"), Text.translatable((ChannelIsConnect ? "online" : "offline") + ".chatimage.gui"));
//        int color = ChannelIsConnect ? 6990707 : 15684440;
//        drawCenteredText(matrices, this.textRenderer, channelStatus,
//                this.width / 2 - 154, this.height / 3 - 64, color);
//    }

    private MutableText getCq(boolean enable) {
        return getEnable("cq.chatimage.gui", enable);
    }

    private MutableText getNsfw(boolean enable) {
        return getEnable("nsfw.chatimage.gui", !enable);
    }

    private MutableText getDrag(boolean enable) {
        return getEnable("drag.chatimage.gui", enable);
    }

    private MutableText getUri(boolean enable) {
        return getEnable("uri.chatimage.gui", enable);
    }

    public static MutableText getEnable(String key, boolean enable) {
        return composeGenericOptionText(Text.translatable(key), Text.translatable((enable ? "open" : "close") + ".chatimage.common"));
    }

}
