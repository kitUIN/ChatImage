package github.kituin.chatimage.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import github.kituin.chatimage.widget.SettingSliderWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponent;


public abstract class ConfigRawScreen extends Screen {
    protected Screen parent;

    protected ConfigRawScreen(TextComponent title, Screen screen) {
        super(title);
        this.parent = screen;
    }
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack);
        drawCenteredString(matrixStack,  this.font,
                title, this.width / 2, this.height / 4 - 16, 16764108);
        super.render(matrixStack,mouseX,mouseY,partialTicks);
    }
    protected SettingSliderWidget.OnTooltip createSliderTooltip(ITextComponent text) {
        return (p_93753_, p_93754_, p_93755_, p_93756_) -> {
            if (text != null) {
                ConfigRawScreen.this.renderTooltip(p_93754_, ConfigRawScreen.this.font.split(text, Math.max(ConfigRawScreen.this.width / 2 - 43, 170)), p_93755_, p_93756_);
            }
        };
    }
    protected Button.ITooltip createButtonTooltip(ITextComponent text) {
        return (Button p_onTooltip_1_, MatrixStack p_onTooltip_2_, int p_onTooltip_3_, int p_onTooltip_4_) ->{
            if (text != null) {
                ConfigRawScreen.this.renderTooltip(p_onTooltip_2_, ConfigRawScreen.this.font.split(text, Math.max(ConfigRawScreen.this.width / 2 - 43, 170)), p_onTooltip_3_, p_onTooltip_4_);
            }
        };
    }
}
