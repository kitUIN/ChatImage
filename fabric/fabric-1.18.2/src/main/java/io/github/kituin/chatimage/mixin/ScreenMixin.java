package io.github.kituin.chatimage.mixin;


import io.github.kituin.chatimage.gui.ConfirmNsfwScreen;
import io.github.kituin.ChatImageCode.ChatImageCode;
import io.github.kituin.ChatImageCode.ClientStorage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.AbstractParentElement;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.*;
import net.minecraft.text.*;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
// IF < fabric-1.20 && > fabric-1.19.2
//import java.util.List;
//import com.mojang.blaze3d.systems.RenderSystem;
//import io.github.kituin.ChatImageCode.ChatImageFrame;
//import net.minecraft.client.util.math.MatrixStack;
//import org.joml.Matrix4f;
//import net.minecraft.client.gui.DrawableHelper;
//import net.minecraft.client.gui.tooltip.HoveredTooltipPositioner;
//import net.minecraft.client.gui.tooltip.TooltipBackgroundRenderer;
//import org.joml.Vector2ic;
//import net.minecraft.util.Identifier;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
// ELSE IF <= fabric-1.19.2
import java.util.List;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.kituin.ChatImageCode.ChatImageFrame;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
// END IF

import static io.github.kituin.chatimage.client.ChatImageClient.CONFIG;
import static io.github.kituin.chatimage.tool.ChatImageStyle.SHOW_IMAGE;
import static io.github.kituin.chatimage.tool.SimpleUtil.*;


/**
 * 注入修改悬浮显示图片
 *
 * @author kitUIN
 */
@Environment(EnvType.CLIENT)
@Mixin(Screen.class)
public abstract class ScreenMixin extends AbstractParentElement implements Drawable {
    
    @Shadow
    public int width;

    @Shadow
    @Nullable
    protected MinecraftClient client;

// IF < fabric-1.20
    @Shadow
    public int height;
    @Shadow
    public abstract void renderOrderedTooltip(MatrixStack matrices, List<? extends OrderedText> lines, int x, int y);

    @SuppressWarnings("t")
    @Inject(at = @At("RETURN"),
            method = "renderTextHoverEffect")
    protected void renderTextHoverEffect(MatrixStack matrices, Style style, int x, int y, CallbackInfo ci) {
        if (style != null && style.getHoverEvent() != null) {
            HoverEvent hoverEvent = style.getHoverEvent();
            ChatImageCode code = hoverEvent.getValue(SHOW_IMAGE);
            if (code != null) {
                if (CONFIG.nsfw || !code.isNsfw() || ClientStorage.ContainNsfw(code.getUrl())) {
                    ChatImageFrame frame = code.getFrame();
                    if (frame.loadImage(CONFIG.limitWidth, CONFIG.limitHeight)) {
                        int viewWidth = frame.getWidth();
                        int viewHeight = frame.getHeight();
// END IF
// IF <= fabric-1.19.2
                        int i = viewWidth + CONFIG.paddingLeft + CONFIG.paddingRight;
                        int j = viewHeight + CONFIG.paddingTop + CONFIG.paddingBottom;
                        int l = x + 12;
                        int m = y - 12;
                        if (l + i + 6 > this.width) {
                            l = this.width - i - 6;
                        }
                        if (m + j + 6 > this.height) {
                            m = this.height - j - 6;
                        }
                        matrices.push();
// END IF
// IF fabric-1.16.5
//                        Tessellator tessellator = Tessellator.getInstance();
//                        BufferBuilder bufferBuilder = tessellator.getBuffer();
//                        bufferBuilder.begin(7, VertexFormats.POSITION_COLOR);
//                        Matrix4f matrix4f = matrices.peek().getModel();
//                        fillGradient(matrix4f, bufferBuilder, l - 3, m - 4, l + i + 3, m - 3, 400, -267386864, -267386864);
//                        fillGradient(matrix4f, bufferBuilder, l - 3, m + j + 3, l + i + 3, m + j + 4, 400, -267386864, -267386864);
//                        fillGradient(matrix4f, bufferBuilder, l - 3, m - 3, l + i + 3, m + j + 3, 400, -267386864, -267386864);
//                        fillGradient(matrix4f, bufferBuilder, l - 4, m - 3, l - 3, m + j + 3, 400, -267386864, -267386864);
//                        fillGradient(matrix4f, bufferBuilder, l + i + 3, m - 3, l + i + 4, m + j + 3, 400, -267386864, -267386864);
//                        fillGradient(matrix4f, bufferBuilder, l - 3, m - 3 + 1, l - 3 + 1, m + j + 3 - 1, 400, 1347420415, 1344798847);
//                        fillGradient(matrix4f, bufferBuilder, l + i + 2, m - 3 + 1, l + i + 3, m + j + 3 - 1, 400, 1347420415, 1344798847);
//                        fillGradient(matrix4f, bufferBuilder, l - 3, m - 3, l + i + 3, m - 3 + 1, 400, 1347420415, 1347420415);
//                        fillGradient(matrix4f, bufferBuilder, l - 3, m + j + 2, l + i + 3, m + j + 3, 400, 1344798847, 1344798847);
//
//                        RenderSystem.enableDepthTest();
//                        RenderSystem.disableTexture();
//                        RenderSystem.enableBlend();
//                        RenderSystem.defaultBlendFunc();
//                        bufferBuilder.end();
//                        BufferRenderer.draw(bufferBuilder);
//
//                        RenderSystem.disableBlend();
//                        RenderSystem.enableTexture();
//                        matrices.translate(0.0F, 0.0F, 400.0F);
//                        RenderSystem.blendColor(1.0F, 1.0F, 1.0F, 1.0F);
//                        this.client.getTextureManager().bindTexture((Identifier) frame.getId());
// ELSE IF fabric-1.18.2
                        Tessellator tessellator = Tessellator.getInstance();
                        BufferBuilder bufferBuilder = tessellator.getBuffer();
                        RenderSystem.setShader(GameRenderer::getPositionColorShader);
                        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
                        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
                        fillGradient(matrix4f, bufferBuilder, l - 3, m - 4, l + i + 3, m - 3, 400, -267386864, -267386864);
                        fillGradient(matrix4f, bufferBuilder, l - 3, m + j + 3, l + i + 3, m + j + 4, 400, -267386864, -267386864);
                        fillGradient(matrix4f, bufferBuilder, l - 3, m - 3, l + i + 3, m + j + 3, 400, -267386864, -267386864);
                        fillGradient(matrix4f, bufferBuilder, l - 4, m - 3, l - 3, m + j + 3, 400, -267386864, -267386864);
                        fillGradient(matrix4f, bufferBuilder, l + i + 3, m - 3, l + i + 4, m + j + 3, 400, -267386864, -267386864);
                        fillGradient(matrix4f, bufferBuilder, l - 3, m - 3 + 1, l - 3 + 1, m + j + 3 - 1, 400, 1347420415, 1344798847);
                        fillGradient(matrix4f, bufferBuilder, l + i + 2, m - 3 + 1, l + i + 3, m + j + 3 - 1, 400, 1347420415, 1344798847);
                        fillGradient(matrix4f, bufferBuilder, l - 3, m - 3, l + i + 3, m - 3 + 1, 400, 1347420415, 1347420415);
                        fillGradient(matrix4f, bufferBuilder, l - 3, m + j + 2, l + i + 3, m + j + 3, 400, 1344798847, 1344798847);

                        RenderSystem.enableDepthTest();
                        RenderSystem.disableTexture();
                        RenderSystem.enableBlend();
                        RenderSystem.defaultBlendFunc();
                        bufferBuilder.end();
                        BufferRenderer.draw(bufferBuilder);
                        RenderSystem.disableBlend();
                        RenderSystem.enableTexture();
                        matrices.translate(0.0F, 0.0F, 400.0F);
                        RenderSystem.setShader(GameRenderer::getPositionColorShader);
                        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                        RenderSystem.setShaderTexture(0, (Identifier) frame.getId());
// ELSE IF fabric-1.19.1 ||  fabric-1.19.2
//                        Tessellator tessellator = Tessellator.getInstance();
//                        BufferBuilder bufferBuilder = tessellator.getBuffer();
//                        RenderSystem.setShader(GameRenderer::getPositionColorShader);
//                        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
//                        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
//                        fillGradient(matrix4f, bufferBuilder, l - 3, m - 4, l + i + 3, m - 3, 400, -267386864, -267386864);
//                        fillGradient(matrix4f, bufferBuilder, l - 3, m + j + 3, l + i + 3, m + j + 4, 400, -267386864, -267386864);
//                        fillGradient(matrix4f, bufferBuilder, l - 3, m - 3, l + i + 3, m + j + 3, 400, -267386864, -267386864);
//                        fillGradient(matrix4f, bufferBuilder, l - 4, m - 3, l - 3, m + j + 3, 400, -267386864, -267386864);
//                        fillGradient(matrix4f, bufferBuilder, l + i + 3, m - 3, l + i + 4, m + j + 3, 400, -267386864, -267386864);
//                        fillGradient(matrix4f, bufferBuilder, l - 3, m - 3 + 1, l - 3 + 1, m + j + 3 - 1, 400, 1347420415, 1344798847);
//                        fillGradient(matrix4f, bufferBuilder, l + i + 2, m - 3 + 1, l + i + 3, m + j + 3 - 1, 400, 1347420415, 1344798847);
//                        fillGradient(matrix4f, bufferBuilder, l - 3, m - 3, l + i + 3, m - 3 + 1, 400, 1347420415, 1347420415);
//                        fillGradient(matrix4f, bufferBuilder, l - 3, m + j + 2, l + i + 3, m + j + 3, 400, 1344798847, 1344798847);
//
//                        RenderSystem.enableDepthTest();
//                        RenderSystem.disableTexture();
//                        RenderSystem.enableBlend();
//                        RenderSystem.defaultBlendFunc();
//                        BufferRenderer.drawWithShader(bufferBuilder.end());
//                        RenderSystem.disableBlend();
//                        RenderSystem.enableTexture();
//                        matrices.translate(0.0F, 0.0F, 400.0F);
//                        RenderSystem.setShader(GameRenderer::getPositionColorShader);
//                        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//                        RenderSystem.setShaderTexture(0, (Identifier) frame.getId());
// ELSE IF > fabric-1.19.2
//                        HoveredTooltipPositioner positioner = (HoveredTooltipPositioner) HoveredTooltipPositioner.INSTANCE;
//                        Vector2ic vector2ic = positioner.getPosition((Screen) (Object) this, x, y, viewWidth + CONFIG.paddingLeft + CONFIG.paddingRight, viewHeight + CONFIG.paddingTop + CONFIG.paddingBottom);
//                        int l = vector2ic.x();
//                        int m = vector2ic.y();
//                        matrices.push();
//                        Tessellator tessellator = Tessellator.getInstance();
//                        BufferBuilder bufferBuilder = tessellator.getBuffer();
//                        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
//                        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
//                        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
//                        TooltipBackgroundRenderer.render(DrawableHelper::fillGradient, matrix4f, bufferBuilder, l, m, viewWidth + CONFIG.paddingLeft + CONFIG.paddingRight, viewHeight + CONFIG.paddingTop + CONFIG.paddingBottom, 400);
//                        RenderSystem.enableDepthTest();
//                        RenderSystem.enableBlend();
//                        RenderSystem.defaultBlendFunc();
//                        BufferRenderer.draw(bufferBuilder.end());
//                        RenderSystem.disableBlend();
//                        matrices.translate(0.0F, 0.0F, 400.0F);
//                        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
//                        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//                        RenderSystem.setShaderTexture(0, (Identifier) frame.getId());
// END IF
// IF < fabric-1.20
                        drawTexture(matrices, l + CONFIG.paddingLeft, m + CONFIG.paddingTop, 0, 0, viewWidth, viewHeight, viewWidth, viewHeight);
                        matrices.pop();
                        frame.gifLoop(CONFIG.gifSpeed);
                    } else {
                        MutableText text = (MutableText) frame.getErrorMessage(
                                (str) -> createLiteralText((String) str),
                                (str) -> createTranslatableText((String) str),
                                (obj, s)-> ((MutableText)obj).append((Text)s) , code);
                        this.renderOrderedTooltip(matrices, this.client.textRenderer.wrapLines(text, Math.max(this.width / 2, 200)), x, y);
                    }
                } else {
                    this.renderOrderedTooltip(matrices, this.client.textRenderer.wrapLines(createTranslatableText("nsfw.chatimage.message"), Math.max(this.width / 2, 200)), x, y);
                }

            }

        }

    }
// END IF
    @Unique
    private String nsfwUrl;

    @Unique
    private void confirmNsfw(boolean open) {
        if (open) {
            ClientStorage.AddNsfw(nsfwUrl, 1);
        }
        this.nsfwUrl = null;
        setScreen(this.client, (Screen) (Object) this);
    }

    @Inject(at = @At("RETURN"),
            method = "handleTextClick", cancellable = true)
    private void handleTextClick(Style style, CallbackInfoReturnable<Boolean> cir) {
        if (style != null && style.getHoverEvent() != null) {
            HoverEvent hoverEvent = style.getHoverEvent();
            ChatImageCode code = hoverEvent.getValue(SHOW_IMAGE);
            if (code != null && code.isNsfw() && !ClientStorage.ContainNsfw(code.getUrl()) && !CONFIG.nsfw) {
                this.nsfwUrl = code.getUrl();
                setScreen(this.client, new ConfirmNsfwScreen(this::confirmNsfw, nsfwUrl));
                cir.setReturnValue(true);
            }
        }
    }
}