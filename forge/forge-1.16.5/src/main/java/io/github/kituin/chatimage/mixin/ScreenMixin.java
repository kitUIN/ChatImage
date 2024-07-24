package io.github.kituin.chatimage.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.kituin.chatimage.gui.ConfirmNsfwScreen;
import io.github.kituin.ChatImageCode.ChatImageCode;
import io.github.kituin.ChatImageCode.ChatImageFrame;
import io.github.kituin.ChatImageCode.ClientStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FocusableGui;
import net.minecraft.client.gui.IRenderable;
import net.minecraft.client.gui.screen.IScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.HoverEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

import static io.github.kituin.chatimage.ChatImage.CONFIG;
import static io.github.kituin.chatimage.tool.ChatImageStyle.SHOW_IMAGE;


/**
 * 注入修改悬浮显示图片
 *
 * @author kitUIN
 */
@Mixin(Screen.class)
public abstract class ScreenMixin extends FocusableGui implements IScreen, IRenderable {


    @Shadow
    public int width;

    @Shadow
    @Nullable
    protected Minecraft minecraft;


    @Shadow
    public abstract void renderTooltip(MatrixStack matrixStack, List<? extends IReorderingProcessor> tooltips, int mouseX, int mouseY);


    @Shadow
    public int height;


    @Inject(at = @At("RETURN"),
            method = "renderComponentHoverEffect")
    protected void renderComponentHoverEffect(MatrixStack matrixStack, @Nullable Style style, int mouseX, int mouseY, CallbackInfo ci) {
        if (style != null && style.getHoverEvent() != null) {
            HoverEvent hoverEvent = style.getHoverEvent();
            ChatImageCode code = hoverEvent.getValue(SHOW_IMAGE);
            if (code != null) {
                if (CONFIG.nsfw || !code.isNsfw() || ClientStorage.ContainNsfw(code.getUrl())) {
                    ChatImageFrame frame = code.getFrame();
                    if (frame.loadImage(CONFIG.limitWidth, CONFIG.limitHeight)) {
                        int viewWidth = frame.getWidth();
                        int viewHeight = frame.getHeight();
                        int i = viewWidth + CONFIG.paddingLeft + CONFIG.paddingRight;
                        int j = viewHeight + CONFIG.paddingTop + CONFIG.paddingBottom;
                        int l = mouseX + 12;
                        int m = mouseY - 12;
                        if (l + i + 6 > this.width) {
                            l = this.width - i - 6;
                        }

                        if (m + j + 6 > this.height) {
                            m = this.height - j - 6;
                        }
                        matrixStack.pushPose();
                        Tessellator tessellator = Tessellator.getInstance();
                        BufferBuilder bufferbuilder = tessellator.getBuilder();
                        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
                        Matrix4f matrix4f = matrixStack.last().pose();
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
                        RenderSystem.shadeModel(7425);
                        bufferbuilder.end();
                        WorldVertexBufferUploader.end(bufferbuilder);
                        RenderSystem.shadeModel(7424);
                        RenderSystem.disableBlend();
                        RenderSystem.enableTexture();
                        matrixStack.translate(0.0D, 0.0D, 400.0D);
                        RenderSystem.blendColor(1.0F, 1.0F, 1.0F, 1.0F);
                        RenderSystem.bindTexture(Objects.requireNonNull(Minecraft.getInstance().getTextureManager().getTexture((ResourceLocation) frame.getId())).getId());

                        AbstractGui.blit(matrixStack, l + CONFIG.paddingLeft, m + CONFIG.paddingTop, 0, 0, viewWidth, viewHeight, viewWidth, viewHeight);
                        matrixStack.popPose();
                        frame.gifLoop(CONFIG.gifSpeed);
                    } else {
                        IFormattableTextComponent text = (IFormattableTextComponent) frame.getErrorMessage(
                                (str) -> new StringTextComponent((String) str),
                                (str) -> new TranslationTextComponent((String) str),
                                (obj, s) -> ((IFormattableTextComponent) obj).append((ITextComponent) s), code);
                        this.renderTooltip(matrixStack, this.minecraft.font.split(text, Math.max(this.width / 2, 200)), mouseX, mouseY);
                    }
                } else {
                    this.renderTooltip(matrixStack, this.minecraft.font.split(new TranslationTextComponent("nsfw.chatimage.message"), Math.max(this.width / 2, 200)),  mouseX, mouseY);
                }

            }

        }

    }

    @Unique
    private String chatImage$nsfwUrl;

    private void chatImage$confirmNsfw(boolean open) {
        if (open) {
            ClientStorage.AddNsfw(chatImage$nsfwUrl, 1);
        }
        this.chatImage$nsfwUrl = null;
        if (this.minecraft != null) {
            this.minecraft.setScreen((Screen) (Object) this);
        }
    }

    @Inject(at = @At("RETURN"),
            method = "handleComponentClicked", cancellable = true)
    private void handleComponentClicked(Style style, CallbackInfoReturnable<Boolean> cir) {
        if (style != null && style.getHoverEvent() != null) {
            HoverEvent hoverEvent = style.getHoverEvent();
            ChatImageCode code = hoverEvent.getValue(SHOW_IMAGE);
            if (code != null && code.isNsfw() && !ClientStorage.ContainNsfw(code.getUrl()) && !CONFIG.nsfw) {
                this.chatImage$nsfwUrl = code.getUrl();
                if (this.minecraft != null) {
                    this.minecraft.setScreen(new ConfirmNsfwScreen(this::chatImage$confirmNsfw, chatImage$nsfwUrl));
                    cir.setReturnValue(true);
                }
            }
        }
    }
}
