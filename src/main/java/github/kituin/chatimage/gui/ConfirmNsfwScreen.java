package github.kituin.chatimage.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
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
        this.addRenderableWidget(Button.builder(this.yesButton, (button) -> {
            this.callback.accept(true);
        }).bounds(this.width / 2 - 50 - 52, y, 100, 20).build());
        this.addRenderableWidget(Button.builder(this.noButton, (button) -> {
            this.callback.accept(false);
        }).bounds(this.width / 2 - 50 + 52, y, 100, 20).build());
    }

    @Override
    public void render(PoseStack p_96249_, int p_96250_, int p_96251_, float p_96252_) {
        super.render(p_96249_, p_96250_, p_96251_, p_96252_);
        drawCenteredString(p_96249_, this.font,
                Component.translatable("nsfw.chatimage.warning"), this.width / 2, 110, 16764108);
    }
}
