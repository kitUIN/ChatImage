package io.github.kituin.chatimage.gui;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.gui.screen.ConfirmScreen;
// IF fabric-1.16.5
//import net.minecraft.client.gui.widget.ClickableWidget;
// ELSE
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
// END IF
// IF fabric-1.20 || fabric-1.20.3 || fabric-1.20.5 || fabric-1.21
//import net.minecraft.client.gui.DrawContext;
// ELSE
import net.minecraft.client.util.math.MatrixStack;
// END IF
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

import static io.github.kituin.chatimage.tool.SimpleUtil.*;

@Environment(EnvType.CLIENT)
public class ConfirmNsfwScreen extends ConfirmScreen {

    public ConfirmNsfwScreen(BooleanConsumer callback, String link) {
        this(callback, createTranslatableText("nsfw.chatimage.open"), createLiteralText(link));
    }

    public ConfirmNsfwScreen(BooleanConsumer callback, Text title, Text message) {
        super(callback, title, message);
// IF fabric-1.16.5 || fabric-1.18.2
//        this.yesTranslated = ScreenTexts.YES;
//        this.noTranslated = ScreenTexts.NO;
// ELSE
        this.yesText = ScreenTexts.YES;
        this.noText = ScreenTexts.NO;
// END IF
    }

    protected void addButtons(int y) {
// IF fabric-1.16.5 || fabric-1.18.2
//        Text yesT =  this.yesTranslated;
//        Text noT =  this.noTranslated;
// ELSE
        Text yesT =  this.yesText;
        Text noT =  this.noText;
// END IF

        addDrawableWeight(createButtonWidget(this.width / 2 - 50 - 52, y, 100, 20, yesT, (button) -> {
            this.callback.accept(true);
        }));
        addDrawableWeight(createButtonWidget(this.width / 2 - 50 + 52, y, 100, 20, noT, (button) -> {
            this.callback.accept(false);
        }));
    }
// IF fabric-1.16.5 || fabric-1.18.2 || fabric-1.19.1 || fabric-1.19.2 || fabric-1.19.3
//    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
//        super.render(matrices, mouseX, mouseY, delta);
//        drawCenteredText(matrices, this.textRenderer, createTranslatableText("nsfw.chatimage.warning"), this.width / 2, 110, 16764108);
//    }
// ELSE IF fabric-1.19.4
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        drawCenteredTextWithShadow(matrices, this.textRenderer, createTranslatableText("nsfw.chatimage.warning"), this.width / 2, 110, 16764108);
    }
// ELSE
//    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
//        super.render(context, mouseX, mouseY, delta);
//        context.drawCenteredTextWithShadow(this.textRenderer, createTranslatableText("nsfw.chatimage.warning"), this.width / 2, 110, 16764108);
//    }
// END IF
// IF fabric-1.16.5
//    public <T extends ClickableWidget> T addDrawableWeight(T element)
//    {
//        return this.addButton(element);
// ELSE
    public <T extends Element & Drawable & Selectable> T addDrawableWeight(T element)
    {
        return this.addDrawableChild(element);
// END IF
}
}