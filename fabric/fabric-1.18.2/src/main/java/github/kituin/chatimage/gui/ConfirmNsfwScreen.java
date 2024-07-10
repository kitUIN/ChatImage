package github.kituin.chatimage.gui;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

@Environment(EnvType.CLIENT)
public class ConfirmNsfwScreen extends ConfirmScreen {

    public ConfirmNsfwScreen(BooleanConsumer callback, String link) {
        this(callback, new TranslatableText("nsfw.chatimage.open"), new LiteralText(link));
    }

    public ConfirmNsfwScreen(BooleanConsumer callback, Text title, Text message) {
        super(callback, title, message);
        this.yesTranslated = ScreenTexts.YES;
        this.noTranslated = ScreenTexts.NO;
    }

    protected void addButtons(int y) {
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 50 - 52, y, 100, 20, this.yesTranslated, (button) -> {
            this.callback.accept(true);
        }));
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 50 + 52, y, 100, 20, this.noTranslated, (button) -> {
            this.callback.accept(false);
        }));
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        drawCenteredText(matrices, this.textRenderer,
                new TranslatableText("nsfw.chatimage.warning"), this.width / 2, 110, 16764108);
    }
}
