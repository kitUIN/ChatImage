package github.kituin.chatimage.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import github.kituin.chatimage.gui.ConfirmNsfwScreen;
import github.kituin.chatimage.tool.ChatImageCode;
import github.kituin.chatimage.tool.ChatImageFrame;
import github.kituin.chatimage.tool.HttpUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
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

import static github.kituin.chatimage.Chatimage.CONFIG;
import static github.kituin.chatimage.tool.ChatImageStyle.SHOW_IMAGE;
import static github.kituin.chatimage.tool.ChatImageUrl.CACHE_MAP;

/**
 * 注入修改悬浮显示图片
 *
 * @author kitUIN
 */
@Mixin(Screen.class)
public abstract class ScreenMixin extends AbstractContainerEventHandler implements Renderable {

    @Shadow
    public abstract void renderTooltip(PoseStack p_96618_, List<? extends FormattedCharSequence> p_96619_, int p_96620_, int p_96621_);

    @Shadow
    public int width;

    @Shadow
    @Nullable
    protected Minecraft minecraft;


    @Inject(at = @At("RETURN"),
            method = "renderComponentHoverEffect")
    protected void renderComponentHoverEffect(PoseStack p_96571_, @javax.annotation.Nullable Style p_96572_, int p_96573_, int p_96574_, CallbackInfo ci) {
        if (p_96572_ != null && p_96572_.getHoverEvent() != null) {
            HoverEvent hoverEvent = p_96572_.getHoverEvent();
            ChatImageCode view = hoverEvent.getValue(SHOW_IMAGE);
            if (view != null) {
                if (CONFIG.nsfw || !view.getNsfw() || HttpUtils.NSFW_MAP.containsKey(view.getOriginalUrl())) {
                    ChatImageFrame frame = view.getFrame();
                    if (frame.loadImage(CONFIG.limitWidth, CONFIG.limitHeight)) {
                        int viewWidth = frame.getWidth();
                        int viewHeight = frame.getHeight();
                        DefaultTooltipPositioner positioner = (DefaultTooltipPositioner) DefaultTooltipPositioner.INSTANCE;

                        Vector2ic vector2ic = positioner.positionTooltip((Screen) (Object) this, p_96573_, p_96574_, viewWidth + CONFIG.paddingLeft + CONFIG.paddingRight, viewHeight + CONFIG.paddingTop + CONFIG.paddingBottom);
                        int l = vector2ic.x();
                        int m = vector2ic.y();
                        p_96571_.pushPose();
                        Tesselator tesselator = Tesselator.getInstance();
                        BufferBuilder bufferbuilder = tesselator.getBuilder();
                        RenderSystem.setShader(GameRenderer::getPositionColorShader);
                        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
                        Matrix4f matrix4f = p_96571_.last().pose();
                        TooltipRenderUtil.renderTooltipBackground(GuiComponent::fillGradient, matrix4f, bufferbuilder, l, m, viewWidth + CONFIG.paddingLeft + CONFIG.paddingRight, viewHeight + CONFIG.paddingTop + CONFIG.paddingBottom, 400);
                        RenderSystem.enableDepthTest();
                        RenderSystem.disableTexture();
                        RenderSystem.enableBlend();
                        RenderSystem.defaultBlendFunc();
                        BufferUploader.drawWithShader(bufferbuilder.end());
                        RenderSystem.disableBlend();
                        RenderSystem.enableTexture();
                        p_96571_.translate(0.0F, 0.0F, 400.0F);
                        RenderSystem.setShader(GameRenderer::getPositionColorShader);
                        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                        RenderSystem.setShaderTexture(0, frame.getId());

                        GuiComponent.blit(p_96571_, l + CONFIG.paddingLeft, m + CONFIG.paddingTop, 0, 0, viewWidth, viewHeight, viewWidth, viewHeight);
                        p_96571_.popPose();
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
                        MutableComponent text;
                        switch (frame.getError()) {
                            case FILE_NOT_FOUND:
                                if (view.isSendFromSelf()) {
                                    text = Component.literal(view.getChatImageUrl().getUrl());
                                    text.append(Component.literal("\n↑")).append(Component.translatable("filenotfound.chatimage.exception"));
                                } else {
                                    if (view.isTimeout()) {
                                        text = Component.translatable("error.server.chatimage.message");
                                    } else {
                                        text = Component.translatable("loading.server.chatimage.message");
                                    }
                                }
                                break;
                            case FILE_LOAD_ERROR:
                                text = Component.translatable("error.chatimage.message");
                                break;
                            case SERVER_FILE_LOAD_ERROR:
                                text = Component.translatable("error.server.chatimage.message");
                                break;
                            default:
                                if (view.isTimeout()) {
                                    text = Component.translatable("error.chatimage.message");
                                } else {
                                    text = Component.translatable("loading.chatimage.message");
                                }
                                break;
                        }

                        this.renderTooltip(p_96571_, this.minecraft.font.split(text, Math.max(this.width / 2, 200)), p_96573_, p_96574_);
                    }
                } else {
                    this.renderTooltip(p_96571_, this.minecraft.font.split(Component.translatable("nsfw.chatimage.message"), Math.max(this.width / 2, 200)), p_96573_, p_96574_);
                }

            }

        }

    }

    private String nsfwUrl;

    private void confirmNsfw(boolean open) {
        if (open) {
            HttpUtils.NSFW_MAP.put(nsfwUrl, 1);
        }
        this.nsfwUrl = null;
        this.minecraft.setScreen((Screen) (Object) this);
    }

    @Inject(at = @At("RETURN"),
            method = "handleComponentClicked")
    private void handleComponentClicked(Style style, CallbackInfoReturnable<Boolean> cir) {
        if (style != null && style.getHoverEvent() != null) {
            HoverEvent hoverEvent = style.getHoverEvent();
            ChatImageCode view = hoverEvent.getValue(SHOW_IMAGE);
            if (view != null && view.getNsfw() && !HttpUtils.NSFW_MAP.containsKey(view.getOriginalUrl()) && !CONFIG.nsfw) {
                this.nsfwUrl = view.getOriginalUrl();
                this.minecraft.setScreen(new ConfirmNsfwScreen(this::confirmNsfw, nsfwUrl));
            }
        }
    }
}
