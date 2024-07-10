package io.github.kituin.chatimage.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.kituin.chatimage.gui.ConfirmNsfwScreen;
import io.github.kituin.ChatImageCode.ChatImageCode;
import io.github.kituin.ChatImageCode.ChatImageFrame;
import io.github.kituin.ChatImageCode.ClientStorage;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.AbstractParentElement;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.HoveredTooltipPositioner;
import net.minecraft.client.gui.tooltip.TooltipBackgroundRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.*;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector2ic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

import static io.github.kituin.chatimage.client.ChatImageClient.CONFIG;
import static io.github.kituin.chatimage.tool.ChatImageStyle.SHOW_IMAGE;

/**
 * 注入修改悬浮显示图片
 *
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


    @Shadow
    public int height;

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
                        RenderSystem.enableBlend();
                        RenderSystem.defaultBlendFunc();
                        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
                        RenderSystem.disableBlend();
                        matrices.translate(0.0F, 0.0F, 400.0F);
                        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
                        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                        RenderSystem.setShaderTexture(0, (Identifier) frame.getId());

                        drawTexture(matrices, l + CONFIG.paddingLeft, m + CONFIG.paddingTop, 0, 0, viewWidth, viewHeight, viewWidth, viewHeight);
                        matrices.pop();
                        frame.gifLoop(CONFIG.gifSpeed);
                    } else {
                        MutableText text = (MutableText) frame.getErrorMessage(
                                (str) -> Text.literal((String) str),
                                (str) -> Text.translatable((String) str),
                                (obj, s) -> ((MutableText) obj).append((Text) s), code);
                        this.renderOrderedTooltip(matrices, this.client.textRenderer.wrapLines(text, Math.max(this.width / 2, 200)), x, y);
                    }
                } else {
                    this.renderOrderedTooltip(matrices, this.client.textRenderer.wrapLines(Text.translatable("nsfw.chatimage.message"), Math.max(this.width / 2, 200)), x, y);
                }

            }

        }

    }

    @Unique
    private String nsfwUrl;

    @Unique
    private void confirmNsfw(boolean open) {
        if (open) {
            ClientStorage.AddNsfw(nsfwUrl, 1);
        }
        this.nsfwUrl = null;
        this.client.setScreen((Screen) (Object) this);
    }

    @Inject(at = @At("RETURN"),
            method = "handleTextClick", cancellable = true)
    private void handleTextClick(Style style, CallbackInfoReturnable<Boolean> cir) {
        if (style != null && style.getHoverEvent() != null) {
            HoverEvent hoverEvent = style.getHoverEvent();
            ChatImageCode code = hoverEvent.getValue(SHOW_IMAGE);
            if (code != null && code.isNsfw() && !ClientStorage.ContainNsfw(code.getUrl()) && !CONFIG.nsfw) {
                this.nsfwUrl = code.getUrl();
                this.client.setScreen(new ConfirmNsfwScreen(this::confirmNsfw, nsfwUrl));
                cir.setReturnValue(true);
            }
        }
    }
}
