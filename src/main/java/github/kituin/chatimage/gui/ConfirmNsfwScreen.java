package github.kituin.chatimage.gui;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ConfirmNsfwScreen extends ConfirmScreen {

    public ConfirmNsfwScreen(BooleanConsumer callback, String link) {
        this(callback, Component.translatable("nsfw.chatimage.open"), Component.literal(link));
    }

    public ConfirmNsfwScreen(BooleanConsumer callback, Component title, Component message) {
        super(callback, title, message);
        this.yesButton = CommonComponents.GUI_YES;
        this.noButton = CommonComponents.GUI_NO;
    }

    @Override
    protected void addButtons(int y) {
        this.addRenderableWidget(Button.builder(
                this.yesButton,(button) -> this.callback.accept(true)
        ).bounds(this.width / 2 - 50 - 52, y, 100, 20).build());

        this.addRenderableWidget(Button.builder(
                this.noButton,(button) -> this.callback.accept(false)
        ).bounds(this.width / 2 - 50 + 52, y, 100, 20).build());
    }


    @Override
    public void render(GuiGraphics p_281549_, int p_281550_, int p_282878_, float p_282465_) {
        super.render(p_281549_,p_281550_,p_282878_,p_282465_);
        p_281549_.drawCenteredString(this.font,
                Component.translatable("nsfw.chatimage.warning"), this.width / 2, 110, 16764108);
    }
}
