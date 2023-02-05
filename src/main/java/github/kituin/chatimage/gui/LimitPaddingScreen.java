package github.kituin.chatimage.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import github.kituin.chatimage.widget.LimitSlider;
import github.kituin.chatimage.widget.PaddingSlider;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static github.kituin.chatimage.Chatimage.CONFIG;
import static github.kituin.chatimage.widget.LimitSlider.LimitType.WIDTH;

@OnlyIn(Dist.CLIENT)
public class LimitPaddingScreen extends Screen {
    private Screen parent;

    public LimitPaddingScreen(Screen screen) {
        super(Component.translatable("padding.chatimage.gui"));
        this.parent = screen;
    }

    protected void init() {
        super.init();

        addRenderableWidget(new PaddingSlider(this.width / 2 - 154, this.height / 4 + 24 + -16, 150, 20, Component.translatable("left.padding.chatimage.gui"),
                CONFIG.paddingLeft, this.width / 2, PaddingSlider.PaddingType.LEFT));
        addRenderableWidget(new PaddingSlider(this.width / 2 + 4, this.height / 4 + 24 + -16, 150, 20, Component.translatable("right.padding.chatimage.gui"),
                CONFIG.paddingRight, this.width / 2, PaddingSlider.PaddingType.RIGHT));
        addRenderableWidget(new PaddingSlider(this.width / 2 - 154, this.height / 4 + 48 + -16, 150, 20, Component.translatable("top.padding.chatimage.gui"),
                CONFIG.paddingTop, this.height / 2, PaddingSlider.PaddingType.TOP));
        addRenderableWidget(new PaddingSlider(this.width / 2 + 4, this.height / 4 + 48 + -16, 150, 20, Component.translatable("bottom.padding.chatimage.gui"),
                CONFIG.paddingBottom, this.height / 2, PaddingSlider.PaddingType.BOTTOM));
        addRenderableWidget(new LimitSlider(this.width / 2 - 154, this.height / 4 + 72 + -16, 150, 20,Component.translatable("width.limit.chatimage.gui"),
                CONFIG.limitWidth,  this.width, LimitSlider.LimitType.WIDTH));
        addRenderableWidget(new LimitSlider(this.width / 2 + 4, this.height / 4 + 72 + -16, 150, 20,Component.translatable("height.limit.chatimage.gui"),
                CONFIG.limitHeight,   this.height, LimitSlider.LimitType.HEIGHT));
        addRenderableWidget(Button.builder(Component.translatable("gui.back"), (button) -> {
            this.minecraft.setScreen(this.parent);
        }).bounds(this.width / 2 - 77, this.height / 4 + 96 + -16, 150, 20).build());

    }

    public void render(PoseStack p_96249_, int p_96250_, int p_96251_, float p_96252_) {
        renderBackground(p_96249_);
        super.render(p_96249_, p_96250_, p_96251_, p_96252_);
        drawCenteredString(p_96249_, this.font,
                title, this.width / 2, this.height / 3 - 32, 16764108);
    }
}
