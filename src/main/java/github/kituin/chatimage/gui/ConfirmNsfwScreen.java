package github.kituin.chatimage.gui;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class ConfirmNsfwScreen extends ConfirmScreen {
    private static final Text WARNING = Text.translatable("nsfw.chatimage.warning");
    private final String link;

    public ConfirmNsfwScreen(BooleanConsumer callback, String link) {
        this(callback, Text.translatable("nsfw.chatimage.open"), Text.literal(link), link);
    }

    public ConfirmNsfwScreen(BooleanConsumer callback, Text title, Text message, String link) {
        super(callback, title, message);
        this.yesText = ScreenTexts.YES;
        this.noText = ScreenTexts.NO;
        this.link = link;
    }

    protected void addButtons(int y) {
        this.addDrawableChild(ButtonWidget.builder(this.yesText, (button) -> {
            this.callback.accept(true);
        }).dimensions(this.width / 2 - 50 - 52, y, 100, 20).build());
        this.addDrawableChild(ButtonWidget.builder(this.noText, (button) -> {
            this.callback.accept(false);
        }).dimensions(this.width / 2 - 50 + 52, y, 100, 20).build());
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        drawCenteredText(matrices, this.textRenderer, WARNING, this.width / 2, 110, 16764108);
    }
}
