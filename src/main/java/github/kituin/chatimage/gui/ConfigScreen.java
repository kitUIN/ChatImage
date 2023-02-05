package github.kituin.chatimage.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import github.kituin.chatimage.config.ChatImageConfig;
import github.kituin.chatimage.widget.GifSlider;
import github.kituin.chatimage.widget.TimeOutSlider;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.FrameWidget;
import net.minecraft.client.gui.components.GridWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static github.kituin.chatimage.Chatimage.CONFIG;

@OnlyIn(Dist.CLIENT)
public class ConfigScreen extends Screen {
    private Screen parent;
    public ConfigScreen() {
        super(Component.translatable("config.chatimage.category"));

    }

    public ConfigScreen(Screen screen) {
        super(Component.translatable("config.chatimage.category"));
        this.parent = screen;
    }


    protected void init() {
        super.init();
        GridWidget gridWidget = new GridWidget();
        GridWidget gridwidget = new GridWidget();
        gridwidget.defaultCellSetting().paddingHorizontal(5).paddingBottom(4).alignHorizontallyCenter();
        GridWidget.RowHelper adder = gridwidget.createRowHelper(2);
        adder.addChild(Button.builder(getNsfw(CONFIG.nsfw), (button) -> {
            CONFIG.nsfw = !CONFIG.nsfw;
            button.setMessage(getNsfw(CONFIG.nsfw));
            ChatImageConfig.saveConfig(CONFIG);
        }).tooltip(Tooltip.create(Component.translatable("nsfw.chatimage.tooltip"))).build());
        adder.addChild(new GifSlider());
        adder.addChild(new TimeOutSlider());
        adder.addChild(Button.builder(Component.translatable("padding.chatimage.gui"), (button) -> {
            this.minecraft.setScreen(new LimitPaddingScreen(this));
        }).tooltip(Tooltip.create(Component.translatable("padding.chatimage.tooltip"))).build());
        adder.addChild(Button.builder(Component.translatable("gui.back"), (button) -> {
            this.minecraft.setScreen(this.parent);
        }).build(), 2);
        gridwidget.pack();
        FrameWidget.alignInRectangle(gridWidget, 0, this.height / 3 - 12, this.width, this.height, 0.5F, 0.0F);
        this.addRenderableWidget(gridWidget);
    }

    public void render(PoseStack p_96249_, int p_96250_, int p_96251_, float p_96252_) {
        renderBackground(p_96249_);
        drawCenteredString(p_96249_, this.font,
                title, this.width / 2, this.height / 3 - 32, 16764108);
        super.render(p_96249_, p_96250_, p_96251_, p_96252_);

    }

    private MutableComponent getNsfw(boolean enable) {
        return Component.translatable(enable ? "close.nsfw.chatimage.gui" : "open.nsfw.chatimage.gui");
    }


}
