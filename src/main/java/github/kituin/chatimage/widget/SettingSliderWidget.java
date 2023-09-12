package github.kituin.chatimage.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.gui.widget.Slider;


@OnlyIn(Dist.CLIENT)
public abstract class SettingSliderWidget extends Slider {

    protected final OnTooltip onTooltip;


    public SettingSliderWidget(int x, int y, int width, int height,ITextComponent prefix, ITextComponent suf, int value, float min, float max, OnTooltip tooltip, ISlider slider) {
        super(x, y, width, height, ((IFormattableTextComponent)prefix).appendString(":"), suf, min,max,value,false,true,null,slider);
        this.updateSlider();
        this.onTooltip = tooltip;
    }
    public void renderWidget(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.renderWidget(matrixStack, mouseX, mouseY, partialTicks);
        if (this.isHovered()) {
            this.renderToolTip(matrixStack, mouseX, mouseY);
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