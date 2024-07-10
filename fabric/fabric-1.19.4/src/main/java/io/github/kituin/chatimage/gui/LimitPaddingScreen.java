package io.github.kituin.chatimage.gui;

import io.github.kituin.chatimage.widget.LimitSlider;
import io.github.kituin.chatimage.widget.PaddingSlider;
import io.github.kituin.chatimage.client.ChatImageClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class LimitPaddingScreen extends Screen {
    private Screen parent;
    public LimitPaddingScreen(Screen screen) {
        super(Text.translatable("padding.chatimage.gui"));
        this.parent = screen;
    }
    protected void init() {
        super.init();
        GridWidget gridWidget = new GridWidget();
        gridWidget.getMainPositioner().marginX(5).marginBottom(4).alignHorizontalCenter();
        GridWidget.Adder adder = gridWidget.createAdder(2);
        adder.add(new PaddingSlider(Text.translatable("left.padding.chatimage.gui"),
                ChatImageClient.CONFIG.paddingLeft, 0F, (float) this.width / 2, PaddingSlider.PaddingType.LEFT));
        adder.add(new PaddingSlider(Text.translatable("right.padding.chatimage.gui"),
                ChatImageClient.CONFIG.paddingRight, 0F, (float) this.width / 2, PaddingSlider.PaddingType.RIGHT));
        adder.add(new PaddingSlider(Text.translatable("top.padding.chatimage.gui"),
                ChatImageClient.CONFIG.paddingTop, 0F, (float) this.height / 2, PaddingSlider.PaddingType.TOP));
        adder.add(new PaddingSlider(Text.translatable("bottom.padding.chatimage.gui"),
                ChatImageClient.CONFIG.paddingBottom, 0F, (float) this.height / 2, PaddingSlider.PaddingType.BOTTOM));
        adder.add(new LimitSlider(Text.translatable("width.limit.chatimage.gui"),
                ChatImageClient.CONFIG.limitWidth, 1F, this.width, LimitSlider.LimitType.WIDTH));
        adder.add(new LimitSlider(Text.translatable("height.limit.chatimage.gui"),
                ChatImageClient.CONFIG.limitHeight, 1F, this.height, LimitSlider.LimitType.HEIGHT));
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
}
