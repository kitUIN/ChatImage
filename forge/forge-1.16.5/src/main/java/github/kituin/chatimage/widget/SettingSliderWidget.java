package github.kituin.chatimage.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.gui.widget.Slider;


@OnlyIn(Dist.CLIENT)
public abstract class SettingSliderWidget extends Slider {

    protected final OnTooltip onTooltip;


    public SettingSliderWidget(int x, int y, int width, int height,ITextComponent prefix, ITextComponent suf, int value, float min, float max, OnTooltip tooltip, ISlider slider) {
        super(x, y, width, height, ((IFormattableTextComponent)prefix).append(":"), suf, min,max,value,false,true,null,slider);
        this.updateSlider();
        this.onTooltip = tooltip;
    }
    public void renderBg(MatrixStack mStack, Minecraft par1Minecraft, int par2, int par3) {
        super.renderBg(mStack, par1Minecraft, par2, par3);
        if (this.isHovered()) {
            this.renderToolTip(mStack, par2, par3);
        }
    }
    public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY) {
        if(!this.dragging){
            this.onTooltip.onTooltip(this, matrixStack, mouseX, mouseY);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public interface OnTooltip {
        void onTooltip(SettingSliderWidget p_93753_, MatrixStack p_93754_, int p_93755_, int p_93756_ );

    }
}