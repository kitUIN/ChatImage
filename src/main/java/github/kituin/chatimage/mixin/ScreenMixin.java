package github.kituin.chatimage.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import github.kituin.chatimage.gui.ConfirmNsfwScreen;
import github.kituin.chatimage.tool.ChatImageCode;
import github.kituin.chatimage.tool.ChatImageFrame;
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
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector2ic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

import static github.kituin.chatimage.client.ChatImageClient.CONFIG;
import static github.kituin.chatimage.tool.ChatImageStyle.SHOW_IMAGE;
import static github.kituin.chatimage.tool.HttpUtils.CACHE_MAP;
import static github.kituin.chatimage.tool.HttpUtils.NSFW_MAP;

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


    @Inject(at = @At("RETURN"),
            method = "renderTextHoverEffect")
    protected void renderTextHoverEffect(MatrixStack matrices, Style style, int x, int y, CallbackInfo ci) {
        if (style != null && style.getHoverEvent() != null) {
            HoverEvent hoverEvent = style.getHoverEvent();
            ChatImageCode view = hoverEvent.getValue(SHOW_IMAGE);
            if (view != null) {
                if (CONFIG.nsfw || !view.getNsfw() || NSFW_MAP.containsKey(view.getOriginalUrl())) {
                    ChatImageFrame frame = view.getFrame();
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
                        RenderSystem.disableTexture();
                        RenderSystem.enableBlend();
                        RenderSystem.defaultBlendFunc();
                        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
                        RenderSystem.disableBlend();
                        RenderSystem.enableTexture();
                        matrices.translate(0.0F, 0.0F, 400.0F);
                        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
                        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                        RenderSystem.setShaderTexture(0, frame.getId());

                        drawTexture(matrices, l + CONFIG.paddingLeft, m + CONFIG.paddingTop, 0, 0, viewWidth, viewHeight, viewWidth, viewHeight);
                        matrices.pop();
                        if (frame.getSiblings().size() != 0) {
                            if (frame.getButter() == CONFIG.gifSpeed) {
                                frame.setIndex((frame.getIndex() + 1) % (frame.getSiblings().size() + 1));
                                CACHE_MAP.put(view.getChatImageUrl().getUrl(), frame);
                                frame.setButter(0);
                            } else {
                                frame.setButter((frame.getButter() + 1) % (CONFIG.gifSpeed + 1));
                            }
                        }
                    } else {
                        MutableText text;
                        switch (frame.getError()) {
                            case FILE_NOT_FOUND:
                                if (view.isSendFromSelf()) {
                                    text = (MutableText) Text.of(view.getChatImageUrl().getUrl());
                                    text.append(Text.of("\n↑")).append(Text.translatable("filenotfound.chatimage.exception"));
                                } else {
                                    if (view.isTimeout()) {
                                        text = Text.translatable("error.server.chatimage.message");
                                    } else {
                                        text = Text.translatable("loading.server.chatimage.message");
                                    }
                                }
                                break;
                            case FILE_LOAD_ERROR:
                                text = Text.translatable("error.chatimage.message");
                                break;
                            case SERVER_FILE_LOAD_ERROR:
                                text = Text.translatable("error.server.chatimage.message");
                                break;
                            default:
                                if (view.isTimeout()) {
                                    text = Text.translatable("error.chatimage.message");
                                }else{
                                    text = Text.translatable("loading.chatimage.message");
                                }
                                break;
                        }

                        this.renderOrderedTooltip(matrices, this.client.textRenderer.wrapLines(text, Math.max(this.width / 2, 200)), x, y);
                    }
                } else {
                    this.renderOrderedTooltip(matrices, this.client.textRenderer.wrapLines(Text.translatable("nsfw.chatimage.message"), Math.max(this.width / 2, 200)), x, y);
                }

            }

        }

    }

    private String nsfwUrl;

    private void confirmNsfw(boolean open) {
        if (open) {
            NSFW_MAP.put(nsfwUrl, 1);
        }
        this.nsfwUrl = null;
        this.client.setScreen((Screen) (Object) this);
    }

    @Inject(at = @At("RETURN"),
            method = "handleTextClick")
    private void handleTextClick(Style style, CallbackInfoReturnable<Boolean> cir) {
        if (style != null && style.getHoverEvent() != null) {
            HoverEvent hoverEvent = style.getHoverEvent();
            ChatImageCode view = hoverEvent.getValue(SHOW_IMAGE);
            if (view != null && view.getNsfw() && !NSFW_MAP.containsKey(view.getOriginalUrl())) {
                this.nsfwUrl = view.getOriginalUrl();
                this.client.setScreen(new ConfirmNsfwScreen(this::confirmNsfw, nsfwUrl));
            }
        }
    }
}
