package github.kituin.chatimage.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import github.kituin.chatimage.tools.ChatImageCode;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.AbstractParentElement;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.HoveredTooltipPositioner;
import net.minecraft.client.gui.tooltip.TooltipBackgroundRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
import org.joml.Matrix4f;
import org.joml.Vector2ic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static github.kituin.chatimage.client.ChatImageClient.*;
import static github.kituin.chatimage.tools.ChatImageTool.SHOW_IMAGE;

@Environment(EnvType.CLIENT)
@Mixin(Screen.class)
public abstract  class ScreenMixin extends AbstractParentElement implements Drawable {

    @Inject(at = @At("RETURN") ,
            method = "renderTextHoverEffect" )
    protected void renderTextHoverEffect(MatrixStack matrices, Style style, int x, int y, CallbackInfo ci) {
        if (style != null && style.getHoverEvent() != null) {
            HoverEvent hoverEvent = style.getHoverEvent();
            ChatImageCode view = hoverEvent.getValue(SHOW_IMAGE);

            if(view.loadImage(0,0))
            {

                int viewWidth = view.getWidth();
                int viewHeight = view.getHeight();
                HoveredTooltipPositioner positioner = (HoveredTooltipPositioner) HoveredTooltipPositioner.INSTANCE;
                Vector2ic vector2ic = positioner.getPosition((Screen) (Object) this, x, y, viewWidth+PADDING_LEFT+PADDING_RIGHT, viewHeight+PADDING_BOTTOM+PADDING_TOP);
                int l = vector2ic.x();
                int m = vector2ic.y();
                matrices.push();

                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferBuilder = tessellator.getBuffer();
                RenderSystem.setShader(GameRenderer::getPositionColorProgram);
                bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
                Matrix4f matrix4f = matrices.peek().getPositionMatrix();
                TooltipBackgroundRenderer.render(DrawableHelper::fillGradient, matrix4f, bufferBuilder, l, m, viewWidth+PADDING_LEFT+PADDING_RIGHT, viewHeight+PADDING_BOTTOM+PADDING_TOP, 400);
                RenderSystem.enableDepthTest();
                RenderSystem.disableTexture();
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
                RenderSystem.disableBlend();

                RenderSystem.enableTexture();
                matrices.translate(0.0F, 0.0F, 400.0F);
                RenderSystem.setShader(GameRenderer::getPositionTexProgram);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.setShaderTexture(0, view.getIdentifier());
                drawTexture(matrices, l+PADDING_LEFT,m+PADDING_TOP,0,0,viewWidth, viewHeight,viewWidth,viewHeight);


            }
            matrices.pop();
        }

    }

}
