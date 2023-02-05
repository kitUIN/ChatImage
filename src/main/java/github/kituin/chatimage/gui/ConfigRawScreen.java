package github.kituin.chatimage.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

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
}
