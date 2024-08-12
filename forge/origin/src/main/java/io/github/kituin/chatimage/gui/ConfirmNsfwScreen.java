package io.github.kituin.chatimage.gui;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static io.github.kituin.chatimage.tool.SimpleUtil.*;

@OnlyIn(Dist.CLIENT)
public class ConfirmNsfwScreen extends ConfirmScreen {

    public ConfirmNsfwScreen(BooleanConsumer callback, String link) {
        this(callback, createTranslatableComponent("nsfw.chatimage.open"), createLiteralComponent(link));
    }

    public ConfirmNsfwScreen(BooleanConsumer callback, #Component# title, #Component# message) {
        super(callback, title, message);
        this.yesButton = CommonComponents.GUI_YES;
        this.noButton = CommonComponents.GUI_NO;
    }

    @Override
    protected void addButtons(int y) {
        this.#kituin$addRenderableWidget#(createButton(this.width / 2 - 50 - 52, y, 100, 20, this.yesButton,
                (button) -> this.callback.accept(true)));
        this.#kituin$addRenderableWidget#(createButton(this.width / 2 - 50 + 52, y, 100, 20, this.noButton,
                (button) -> this.callback.accept(false)));
    }

    @Override
    public void render(#GuiGraphics# matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
// IF <= forge-1.19
//        renderBackground(matrixStack);
//        drawCenteredString(matrixStack, this.font,
//                title, this.width / 2, this.height / 4 - 16, 16764108);
// ELSE
//        renderBackground(matrixStack, mouseX, mouseY, partialTicks);
//        matrixStack.drawCenteredString(this.font,
//                createTranslatableComponent("nsfw.chatimage.warning"), this.width / 2, 110, 16764108);
// END IF
    }
}