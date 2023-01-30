package github.kituin.chatimage.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import github.kituin.chatimage.tool.ChatImageCode;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.AbstractParentElement;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.HoveredTooltipPositioner;
import net.minecraft.client.gui.tooltip.TooltipBackgroundRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector2ic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static github.kituin.chatimage.client.ChatImageClient.*;
import static github.kituin.chatimage.tool.ChatImageStyle.SHOW_IMAGE;
import static github.kituin.chatimage.tool.HttpUtils.HTTPS_MAP;

/**
 * 注入修改悬浮显示图片
 * @author kitUIN
 */
@Mixin(Screen.class)
public abstract class ScreenMixin extends AbstractParentElement implements Drawable {

    @Shadow
    public abstract void renderOrderedTooltip(MatrixStack matrices, List<? extends OrderedText> lines, int x, int y);

    @Shadow
    public int width;

    @Shadow
    @Nullable
    protected MinecraftClient client;


    @Inject(at = @At("RETURN"),
            method = "renderTextHoverEffect")
    protected void renderTextHoverEffect(MatrixStack matrices, Style style, int x, int y, CallbackInfo ci) {
        if (style != null && style.getHoverEvent() != null) {
            HoverEvent hoverEvent = style.getHoverEvent();
            ChatImageCode view = hoverEvent.getValue(SHOW_IMAGE);
            if (view != null) {
                if (view.loadImage(CONFIG.limitWidth, CONFIG.limitHeight)) {
                    int viewWidth = view.getWidth();
                    int viewHeight = view.getHeight();
                    HoveredTooltipPositioner positioner = (HoveredTooltipPositioner) HoveredTooltipPositioner.INSTANCE;
                    Vector2ic vector2ic = positioner.getPosition((Screen) (Object) this, x, y, viewWidth + CONFIG.paddingLeft + CONFIG.paddingRight, viewHeight + CONFIG.paddingTop + CONFIG.paddingBottom);
                    int l = vector2ic.x();
                    int m = vector2ic.y();
                    matrices.push();

                    Tessellator tessellator = Tessellator.getInstance();
                    BufferBuilder bufferBuilder = tessellator.getBuffer();
                    RenderSystem.setShader(GameRenderer::getPositionColorProgram);
                    bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
                    Matrix4f matrix4f = matrices.peek().getPositionMatrix();
                    TooltipBackgroundRenderer.render(DrawableHelper::fillGradient, matrix4f, bufferBuilder, l, m, viewWidth + CONFIG.paddingLeft + CONFIG.paddingRight, viewHeight + CONFIG.paddingTop + CONFIG.paddingBottom, 400);
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
                    drawTexture(matrices, l + CONFIG.paddingLeft, m + CONFIG.paddingTop, 0, 0, viewWidth, viewHeight, viewWidth, viewHeight);

                    matrices.pop();
                } else {
                    Text text = Text.of("");
                    if(HTTPS_MAP.containsKey(view.getChatImageUrl().getUrl()))
                    {
                        if(HTTPS_MAP.get(view.getChatImageUrl().getUrl()) == 2)
                        {
                            text = Text.translatable("message.chatimage.error");
                        }else{
                            text = Text.translatable("message.chatimage.loading");
                        }
                    }

                    if (view.getFileNotFound()) {
                        text = Text.translatable("filenotfound.chatimage.exception");
                    }

                    this.renderOrderedTooltip(matrices, this.client.textRenderer.wrapLines(text, Math.max(this.width / 2, 200)), x, y);
                }
            }

        }

    }

}
