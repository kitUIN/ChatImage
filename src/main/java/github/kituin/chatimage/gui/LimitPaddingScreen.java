package github.kituin.chatimage.gui;

import github.kituin.chatimage.config.ChatImageConfig;
import github.kituin.chatimage.widget.GifSlider;
import github.kituin.chatimage.widget.LimitSlider;
import github.kituin.chatimage.widget.PaddingSlider;
import github.kituin.chatimage.widget.TimeOutSlider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import static github.kituin.chatimage.client.ChatImageClient.CONFIG;

@Environment(EnvType.CLIENT)
public class LimitPaddingScreen extends Screen {
    private Screen parent;
    public LimitPaddingScreen(Text title) {
        super(title);

    }
    protected void init() {
        super.init();
        GridWidget gridWidget = new GridWidget();
        gridWidget.getMainPositioner().marginX(5).marginBottom(4).alignHorizontalCenter();
        GridWidget.Adder adder = gridWidget.createAdder(2);
        adder.add(new PaddingSlider(Text.translatable("left.padding.chatimage.gui"),
                CONFIG.paddingLeft, 0F, this.width / 2, PaddingSlider.PaddingType.LEFT));
        adder.add(new PaddingSlider(Text.translatable("right.padding.chatimage.gui"),
                CONFIG.paddingRight, 0F, this.width / 2, PaddingSlider.PaddingType.RIGHT));
        adder.add(new PaddingSlider(Text.translatable("top.padding.chatimage.gui"),
                CONFIG.paddingTop, 0F, this.height / 2, PaddingSlider.PaddingType.TOP));
        adder.add(new PaddingSlider(Text.translatable("bottom.padding.chatimage.gui"),
                CONFIG.paddingBottom, 0F, this.height / 2, PaddingSlider.PaddingType.BOTTOM));
        adder.add(new LimitSlider(Text.translatable("width.limit.chatimage.gui"),
                CONFIG.limitWidth, 0F, this.width, LimitSlider.LimitType.WIDTH));
        adder.add(new LimitSlider(Text.translatable("height.limit.chatimage.gui"),
                CONFIG.limitHeight, 0F, this.height, LimitSlider.LimitType.HEIGHT));
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
}
