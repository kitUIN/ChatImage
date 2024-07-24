package io.github.kituin.chatimage.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import io.github.kituin.chatimage.gui.ConfirmNsfwScreen;
import io.github.kituin.ChatImageCode.ChatImageCode;
import io.github.kituin.ChatImageCode.ChatImageFrame;
import io.github.kituin.ChatImageCode.ClientStorage;
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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

import static io.github.kituin.chatimage.ChatImage.CONFIG;
import static io.github.kituin.chatimage.tool.ChatImageStyle.SHOW_IMAGE;


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
            ChatImageCode code = hoverEvent.getValue(SHOW_IMAGE);
            if (code != null) {
                if (CONFIG.nsfw || !code.isNsfw() || ClientStorage.ContainNsfw(code.getUrl())) {
                    ChatImageFrame frame = code.getFrame();
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
                        frame.gifLoop(CONFIG.gifSpeed);
                    } else {
                        MutableComponent text = (MutableComponent) frame.getErrorMessage(
                                (str) -> Component.literal((String) str),
                                (str) -> Component.translatable((String) str),
                                (obj, s) -> ((MutableComponent) obj).append((Component) s), code);
                        this.renderTooltip(p_96571_, this.minecraft.font.split(text, Math.max(this.width / 2, 200)), p_96573_, p_96574_);
                    }
                } else {
                    this.renderTooltip(p_96571_, this.minecraft.font.split(Component.translatable("nsfw.chatimage.message"), Math.max(this.width / 2, 200)), p_96573_, p_96574_);
                }

            }

        }

    }

    @Unique
    private String chatimage$nsfwUrl;

    @Unique
    private void chatimage$confirmNsfw(boolean open) {
        if (open) {
            ClientStorage.AddNsfw(chatimage$nsfwUrl, 1);
        }
        this.chatimage$nsfwUrl = null;
        this.minecraft.setScreen((Screen) (Object) this);
    }

    @Inject(at = @At("RETURN"),
            method = "handleComponentClicked", cancellable = true)
    private void handleComponentClicked(Style style, CallbackInfoReturnable<Boolean> cir) {
        if (style != null && style.getHoverEvent() != null) {
            HoverEvent hoverEvent = style.getHoverEvent();
            ChatImageCode code = hoverEvent.getValue(SHOW_IMAGE);
            if (code != null && code.isNsfw() && !ClientStorage.ContainNsfw(code.getUrl()) && !CONFIG.nsfw) {
                this.chatimage$nsfwUrl = code.getUrl();
                this.minecraft.setScreen(new ConfirmNsfwScreen(this::chatimage$confirmNsfw, chatimage$nsfwUrl));
                cir.setReturnValue(true);
            }
        }
    }
}
