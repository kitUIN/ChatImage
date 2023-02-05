package github.kituin.chatimage.gui;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.ScreenTexts;
import net.minecraft.text.Text;
import org.quiltmc.loader.api.minecraft.ClientOnly;

@ClientOnly
public class ConfirmNsfwScreen extends ConfirmScreen {

	public ConfirmNsfwScreen(BooleanConsumer callback, String link) {
		this(callback, Text.translatable("nsfw.chatimage.open"), Text.literal(link));
	}

	public ConfirmNsfwScreen(BooleanConsumer callback, Text title, Text message) {
		super(callback, title, message);
		this.yesTranslated = ScreenTexts.YES;
		this.noTranslated = ScreenTexts.NO;
	}

	@Override
	protected void addButtons(int y) {
		this.addDrawableChild(ButtonWidget.builder(this.yesTranslated, (button) -> {
			this.callback.accept(true);
		}).positionAndSize(this.width / 2 - 50 - 52, y, 100, 20).build());
		this.addDrawableChild(ButtonWidget.builder(this.noTranslated, (button) -> {
			this.callback.accept(false);
		}).positionAndSize(this.width / 2 - 50 + 52, y, 100, 20).build());
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		super.render(matrices, mouseX, mouseY, delta);
		drawCenteredText(matrices, this.textRenderer,
				Text.translatable("nsfw.chatimage.warning"), this.width / 2, 110, 16764108);
	}
}
