package github.kituin.chatimage.mixin;

import io.github.kituin.ChatImageCode.ChatImageCode;
import io.github.kituin.ChatImageCode.ChatImageFrame;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import github.kituin.chatimage.gui.ConfirmNsfwScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FocusableGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IRenderable;
import net.minecraft.client.gui.screen.IScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.HoverEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


import javax.annotation.Nullable;

import java.util.List;

import static io.github.kituin.ChatImageCode.ChatImageCode.NSFW_MAP;
import static io.github.kituin.ChatImageCode.ChatImageHandler.AddChatImage;
import static github.kituin.chatimage.ChatImage.CONFIG;
import static github.kituin.chatimage.tool.ChatImageStyle.SHOW_IMAGE;


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
    protected FontRenderer font;

    @Shadow
    public abstract void renderTooltip(MatrixStack matrixStack, List<? extends IReorderingProcessor> tooltips, int mouseX, int mouseY);


    @Shadow
    public int height;


    @Inject(at = @At("RETURN"),
            method = "renderComponentHoverEffect")
    protected void renderComponentHoverEffect(MatrixStack matrixStack, @Nullable Style style, int mouseX, int mouseY, CallbackInfo ci) {
        if (style != null && style.getHoverEvent() != null) {
            HoverEvent hoverEvent = style.getHoverEvent();
            ChatImageCode view = hoverEvent.getParameter(SHOW_IMAGE);
            if (view != null) {
                if (CONFIG.nsfw || !view.getNsfw() || NSFW_MAP.containsKey(view.getOriginalUrl())) {
                    ChatImageFrame frame = view.getFrame();
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
                        matrixStack.push();
                        Tessellator tessellator = Tessellator.getInstance();
                        BufferBuilder bufferbuilder = tessellator.getBuffer();
                        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
                        Matrix4f matrix4f = matrixStack.getLast().getMatrix();
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
                        bufferbuilder.finishDrawing();
                        WorldVertexBufferUploader.draw(bufferbuilder);
                        RenderSystem.shadeModel(7424);
                        RenderSystem.disableBlend();
                        RenderSystem.enableTexture();
                        IRenderTypeBuffer.Impl irendertypebuffer$impl = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
                        matrixStack.translate(0.0D, 0.0D, 400.0D);
                        // RenderSystem.blendColor(1.0F, 1.0F, 1.0F, 1.0F);
                        RenderSystem.bindTexture(Minecraft.getInstance().getTextureManager().getTexture((ResourceLocation) frame.getId()).getGlTextureId());

                        AbstractGui.blit(matrixStack, l + CONFIG.paddingLeft, m + CONFIG.paddingTop, 0, 0, viewWidth, viewHeight, viewWidth, viewHeight);
                        matrixStack.pop();
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
                        ITextComponent text;
                        switch (frame.getError()) {
                            case FILE_NOT_FOUND:
                                if (view.isSendFromSelf()) {
                                    text = new StringTextComponent(view.getChatImageUrl().getUrl())
                                            .appendString("\n↑")
                                            .appendSibling(new TranslationTextComponent("filenotfound.chatimage.exception"));
                                } else {
                                    text = new TranslationTextComponent(view.isTimeout() ? "error.server.chatimage.message" : "loading.server.chatimage.message");
                                }
                                break;
                            case FILE_LOAD_ERROR:
                                text = new TranslationTextComponent("error.chatimage.message");
                                break;
                            case SERVER_FILE_LOAD_ERROR:
                                text = new TranslationTextComponent("error.server.chatimage.message");
                                break;
                            case ILLEGAL_CICODE_ERROR:
                                text = new TranslationTextComponent("illegalcode.chatimage.exception");
                                break;
                            default:
                                text = new TranslationTextComponent(view.isTimeout() ? "error.chatimage.message" : "loading.chatimage.message");
                                break;
                        }
                        this.renderTooltip(matrixStack, this.minecraft.fontRenderer.trimStringToWidth(text, Math.max(this.width / 2, 200)), mouseX, mouseY);
                    }
                } else {
                    this.renderTooltip(matrixStack, this.minecraft.fontRenderer.trimStringToWidth(new TranslationTextComponent("nsfw.chatimage.message"), Math.max(this.width / 2, 200)),  mouseX, mouseY);
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
        if (this.minecraft != null) {
            this.minecraft.displayGuiScreen((Screen) (Object) this);
        }
    }

    @Inject(at = @At("RETURN"),
            method = "handleComponentClicked")
    private void handleComponentClicked(Style style, CallbackInfoReturnable<Boolean> cir) {
        if (style != null && style.getHoverEvent() != null) {
            HoverEvent hoverEvent = style.getHoverEvent();
            ChatImageCode view = hoverEvent.getParameter(SHOW_IMAGE);
            if (view != null && view.getNsfw() && !NSFW_MAP.containsKey(view.getOriginalUrl()) && !CONFIG.nsfw) {
                this.nsfwUrl = view.getOriginalUrl();
                if (this.minecraft != null) {
                    this.minecraft.displayGuiScreen(new ConfirmNsfwScreen(this::confirmNsfw, nsfwUrl));
                }
            }
        }
    }
}
