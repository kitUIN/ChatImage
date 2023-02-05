package github.kituin.chatimage.gui;


import github.kituin.chatimage.widget.LimitSlider;
import github.kituin.chatimage.widget.PaddingSlider;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.quiltmc.loader.api.minecraft.ClientOnly;

import static github.kituin.chatimage.client.ChatImageClient.CONFIG;


@ClientOnly
public class LimitPaddingScreen extends ConfigRawScreen {


	public LimitPaddingScreen(Screen screen) {
		super(Text.translatable("padding.chatimage.gui"), screen);

	}

	protected void init() {
		super.init();

		addDrawableChild(new PaddingSlider(this.width / 2 - 154, this.height / 4 + 24 + -16, 150, 20, Text.translatable("left.padding.chatimage.gui"),
				CONFIG.paddingLeft, this.width / 2, PaddingSlider.PaddingType.LEFT));
		addDrawableChild(new PaddingSlider(this.width / 2 + 4, this.height / 4 + 24 + -16, 150, 20, Text.translatable("right.padding.chatimage.gui"),
				CONFIG.paddingRight, this.width / 2, PaddingSlider.PaddingType.RIGHT));
		addDrawableChild(new PaddingSlider(this.width / 2 - 154, this.height / 4 + 48 + -16, 150, 20, Text.translatable("top.padding.chatimage.gui"),
				CONFIG.paddingTop, this.height / 2, PaddingSlider.PaddingType.TOP));
		addDrawableChild(new PaddingSlider(this.width / 2 + 4, this.height / 4 + 48 + -16, 150, 20, Text.translatable("bottom.padding.chatimage.gui"),
				CONFIG.paddingBottom, this.height / 2, PaddingSlider.PaddingType.BOTTOM));
		addDrawableChild(new LimitSlider(this.width / 2 - 154, this.height / 4 + 72 + -16, 150, 20, Text.translatable("width.limit.chatimage.gui"),
				CONFIG.limitWidth, this.width, LimitSlider.LimitType.WIDTH));
		addDrawableChild(new LimitSlider(this.width / 2 + 4, this.height / 4 + 72 + -16, 150, 20, Text.translatable("height.limit.chatimage.gui"),
				CONFIG.limitHeight, this.height, LimitSlider.LimitType.HEIGHT));
		addDrawableChild(ButtonWidget.builder(Text.translatable("gui.back"), (button) -> {
			this.client.setScreen(this.parent);
		}).positionAndSize(this.width / 2 - 77, this.height / 4 + 96 + -16, 150, 20).build());
	}


}
