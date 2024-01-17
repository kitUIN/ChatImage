package github.kituin.chatimage.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import github.kituin.chatimage.gui.ConfirmNsfwScreen;
import io.github.kituin.ChatImageCode.ChatImageCode;
import io.github.kituin.ChatImageCode.ChatImageFrame;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

import static github.kituin.chatimage.ChatImage.CONFIG;
import static github.kituin.chatimage.tool.ChatImageStyle.SHOW_IMAGE;
import static io.github.kituin.ChatImageCode.ChatImageCode.NSFW_MAP;
import static io.github.kituin.ChatImageCode.ChatImageHandler.AddChatImage;


/**
 * 注入修改悬浮显示图片
 *
 * @author kitUIN
 */
@Mixin(Screen.class)
public abstract class ScreenMixin extends AbstractContainerEventHandler implements Widget {


    @Shadow
    public int width;

    @Shadow
    @Nullable
    protected Minecraft minecraft;


    @Shadow
    protected Font font;

    @Shadow
    public abstract void renderTooltip(PoseStack p_96618_, List<? extends FormattedCharSequence> p_96619_, int p_96620_, int p_96621_);


    @Shadow
    public int height;

    @Shadow
    protected ItemRenderer itemRenderer;


    @Inject(at = @At("RETURN"),
            method = "renderComponentHoverEffect")
    protected void renderComponentHoverEffect(PoseStack p_96571_, @javax.annotation.Nullable Style p_96572_, int p_96573_, int p_96574_, CallbackInfo ci) {
        if (p_96572_ != null && p_96572_.getHoverEvent() != null) {
            HoverEvent hoverEvent = p_96572_.getHoverEvent();
            ChatImageCode view = hoverEvent.getValue(SHOW_IMAGE);
            if (view != null) {
                if (CONFIG.nsfw || !view.getNsfw() || NSFW_MAP.containsKey(view.getOriginalUrl())) {
                    ChatImageFrame frame = view.getFrame();
                    if (frame.loadImage(CONFIG.limitWidth, CONFIG.limitHeight)) {
                        int viewWidth = frame.getWidth();
                        int viewHeight = frame.getHeight();
                        int i = viewWidth + CONFIG.paddingLeft + CONFIG.paddingRight;
                        int j = viewHeight + CONFIG.paddingTop + CONFIG.paddingBottom;
                        int l = p_96573_ + 12;
                        int m = p_96574_ - 12;
                        if (l + i + 6 > this.width) {
                            l = this.width - i - 6;
                        }

                        if (m + j + 6 > this.height) {
                            m = this.height - j - 6;
                        }
                        p_96571_.pushPose();
                        float f = this.itemRenderer.blitOffset;
                        this.itemRenderer.blitOffset = 400.0F;
                        Tesselator tesselator = Tesselator.getInstance();
                        BufferBuilder bufferbuilder = tesselator.getBuilder();
                        RenderSystem.setShader(GameRenderer::getPositionColorShader);
                        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
                        Matrix4f matrix4f = p_96571_.last().pose();
                        fillGradient(matrix4f, bufferbuilder, l - 3, m - 4, l + i + 3, m - 3, 400, -267386864, -267386864);
                        fillGradient(matrix4f, bufferbuilder, l - 3, m + j + 3, l + i + 3, m + j + 4, 400, -267386864, -267386864);
                        fillGradient(matrix4f, bufferbuilder, l - 3, m - 3, l + i + 3, m + j + 3, 400, -267386864, -267386864);
                        fillGradient(matrix4f, bufferbuilder, l - 4, m - 3, l - 3, m + j + 3, 400, -267386864, -267386864);
                        fillGradient(matrix4f, bufferbuilder, l + i + 3, m - 3, l + i + 4, m + j + 3, 400, -267386864, -267386864);
                        fillGradient(matrix4f, bufferbuilder, l - 3, m - 3 + 1, l - 3 + 1, m + j + 3 - 1, 400, 1347420415, 1344798847);
                        fillGradient(matrix4f, bufferbuilder, l + i + 2, m - 3 + 1, l + i + 3, m + j + 3 - 1, 400, 1347420415, 1344798847);
                        fillGradient(matrix4f, bufferbuilder, l - 3, m - 3, l + i + 3, m - 3 + 1, 400, 1347420415, 1347420415);
                        fillGradient(matrix4f, bufferbuilder, l - 3, m + j + 2, l + i + 3, m + j + 3, 400, 1344798847, 1344798847);

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
                        RenderSystem.setShaderTexture(0, (ResourceLocation) frame.getId());

                        GuiComponent.blit(p_96571_, l + CONFIG.paddingLeft, m + CONFIG.paddingTop, 0, 0, viewWidth, viewHeight, viewWidth, viewHeight);
                        p_96571_.popPose();
                        if (frame.getSiblings().size() != 0) {
                            if (frame.getButter() == CONFIG.gifSpeed) {
                                frame.setIndex((frame.getIndex() + 1) % (frame.getSiblings().size() + 1));
                                AddChatImage(frame, view.getChatImageUrl().getUrl());
                                frame.setButter(0);
                            } else {
                                frame.setButter((frame.getButter() + 1) % (CONFIG.gifSpeed + 1));
                            }
                        }
                    } else {
                        MutableComponent text;
                        switch (frame.getError()) {
                            case FILE_NOT_FOUND -> {
                                if (view.isSendFromSelf()) {
                                    text = Component.literal(view.getChatImageUrl().getUrl())
                                            .append("\n↑")
                                            .append(Component.translatable("filenotfound.chatimage.exception"));
                                } else {
                                    text = Component.translatable(view.isTimeout() ? "error.server.chatimage.message" : "loading.server.chatimage.message");
                                }
                            }
                            case FILE_LOAD_ERROR -> text = Component.translatable("error.chatimage.message");
                            case SERVER_FILE_LOAD_ERROR ->
                                    text = Component.translatable("error.server.chatimage.message");
                            case ILLEGAL_CICODE_ERROR -> text = Component.translatable("illegalcode.chatimage.exception");
                            default ->
                                    text = Component.translatable(view.isTimeout() ? "error.chatimage.message" : "loading.chatimage.message");
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
            NSFW_MAP.put(nsfwUrl, 1);
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
            if (view != null && view.getNsfw() && !NSFW_MAP.containsKey(view.getOriginalUrl()) && !CONFIG.nsfw) {
                this.nsfwUrl = view.getOriginalUrl();
                this.minecraft.setScreen(new ConfirmNsfwScreen(this::confirmNsfw, nsfwUrl));
            }
        }
    }
}
