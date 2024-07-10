package github.kituin.chatimage.gui;

import github.kituin.chatimage.widget.SettingSliderWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.function.Consumer;

public abstract class ConfigRawScreen extends Screen {
    protected Screen parent;

    protected ConfigRawScreen(Text title, Screen screen) {
        super(title);
        this.parent = screen;
    }
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawCenteredText(matrices, this.textRenderer,
                title, this.width / 2, this.height / 4 - 16, 16764108);
    }
    protected ButtonWidget.TooltipSupplier getButtonTooltip(Text text){
        return new ButtonWidget.TooltipSupplier() {
            public void onTooltip(ButtonWidget buttonWidget, MatrixStack matrixStack, int i, int j) {
                ConfigRawScreen.this.renderOrderedTooltip(matrixStack,  ConfigRawScreen.this.client.textRenderer.wrapLines(text, Math.max(ConfigRawScreen.this.width / 2 - 43, 170)), i, j);
            }

            public void supply(Consumer<Text> consumer) {
                consumer.accept(text);
            }
        };
    }
    protected SettingSliderWidget.TooltipSupplier getSliderTooltip(Text text){
        return new SettingSliderWidget.TooltipSupplier() {
            public void onTooltip(SettingSliderWidget buttonWidget, MatrixStack matrixStack, int i, int j) {
                ConfigRawScreen.this.renderOrderedTooltip(matrixStack,  ConfigRawScreen.this.client.textRenderer.wrapLines(text, Math.max(ConfigRawScreen.this.width / 2 - 43, 170)), i, j);
            }

            public void supply(Consumer<Text> consumer) {
                consumer.accept(text);
            }
        };
    }
}
