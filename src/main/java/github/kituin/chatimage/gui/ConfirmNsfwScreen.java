package github.kituin.chatimage.gui;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class ConfirmNsfwScreen extends ConfirmScreen {

    public ConfirmNsfwScreen(BooleanConsumer callback, String link) {
        this(callback, Text.translatable("nsfw.chatimage.open"), Text.literal(link));
    }

    public ConfirmNsfwScreen(BooleanConsumer callback, Text title, Text message) {
        super(callback, title, message);
        this.yesText = ScreenTexts.YES;
        this.noText = ScreenTexts.NO;
    }

    protected void addButtons(int y) {
        this.addDrawableChild(ButtonWidget.builder(this.yesText, (button) -> {
            this.callback.accept(true);
        }).dimensions(this.width / 2 - 50 - 52, y, 100, 20).build());
        this.addDrawableChild(ButtonWidget.builder(this.noText, (button) -> {
            this.callback.accept(false);
        }).dimensions(this.width / 2 - 50 + 52, y, 100, 20).build());
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, Text.translatable("nsfw.chatimage.warning"), this.width / 2, 110, 16764108);
    }
}
