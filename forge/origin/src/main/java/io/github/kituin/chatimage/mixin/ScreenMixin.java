package io.github.kituin.chatimage.mixin;


import io.github.kituin.chatimage.gui.ConfirmNsfwScreen;
import io.github.kituin.ChatImageCode.ChatImageCode;
import io.github.kituin.ChatImageCode.ClientStorage;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
// IF forge-1.16.5
//import java.util.Objects;
// END IF
// IF <= forge-1.19
//import io.github.kituin.ChatImageCode.ChatImageFrame;
//
//import java.util.List;
//
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//import com.mojang.blaze3d.systems.RenderSystem;
// END IF
import static io.github.kituin.chatimage.ChatImage.CONFIG;
import static io.github.kituin.chatimage.tool.ChatImageStyle.SHOW_IMAGE;
import static io.github.kituin.chatimage.tool.SimpleUtil.createLiteralComponent;
import static io.github.kituin.chatimage.tool.SimpleUtil.createTranslatableComponent;



@Mixin(#Screen#.class)
public abstract class ScreenMixin extends #AbstractContainerEventHandler# implements #Renderable# {


    @Shadow
    protected Minecraft minecraft;
    @Unique
    private String chatimage$nsfwUrl;

    // IF <= forge-1.19
//    @Shadow
//    public int width;
//
//    @Shadow
//    public int height;
//
//    @Shadow
//    public abstract void renderTooltip(#GuiGraphics# matrixStack, List<? extends #FormattedCharSequence#> tooltips, int mouseX, int mouseY);
//
//    @Inject(at = @At("RETURN"),
//            method = "renderComponentHoverEffect")
//    protected void renderComponentHoverEffect(#GuiGraphics# pMatrixStack, #Style# pStyle, int pMouseX, int pMouseY, CallbackInfo ci) {
//        if (pStyle != null && pStyle.getHoverEvent() != null) {
//            #HoverEvent# hoverEvent = pStyle.getHoverEvent();
//            ChatImageCode code = hoverEvent.getValue(SHOW_IMAGE);
//            if (code != null) {
//                if (CONFIG.nsfw || !code.isNsfw() || ClientStorage.ContainNsfw(code.getUrl())) {
//                    ChatImageFrame frame = code.getFrame();
//                    if (frame.loadImage(CONFIG.limitWidth, CONFIG.limitHeight)) {
//                        int viewWidth = frame.getWidth();
//                        int viewHeight = frame.getHeight();
//                        int i = viewWidth + CONFIG.paddingLeft + CONFIG.paddingRight;
//                        int j = viewHeight + CONFIG.paddingTop + CONFIG.paddingBottom;
//                        int l = pMouseX + 12;
//                        int m = pMouseY - 12;
//                        if (l + i + 6 > this.width) {
//                            l = this.width - i - 6;
//                        }
//
//                        if (m + j + 6 > this.height) {
//                            m = this.height - j - 6;
//                        }
//                        pMatrixStack.pushPose();
//                        #Tesselator# tessellator = #Tesselator#.getInstance();
//                        #BufferBuilder# bufferbuilder = tessellator.getBuilder();
// IF forge-1.16.5
//                        bufferbuilder.begin(7, #DefaultVertexFormat#.POSITION_COLOR);
// ELSE IF > forge-1.16.5 && <= forge-1.19
////                        RenderSystem.setShader(net.minecraft.client.renderer.GameRenderer::getPositionColorShader);
////                        bufferbuilder.begin(com.mojang.blaze3d.vertex.VertexFormat.Mode.QUADS, #DefaultVertexFormat#.POSITION_COLOR);
// END IF
//                        #Matrix4f# matrix4f = pMatrixStack.last().pose();
//                        fillGradient(matrix4f, bufferbuilder, l - 3, m - 4, l + i + 3, m - 3, 400, -267386864, -267386864);
//                        fillGradient(matrix4f, bufferbuilder, l - 3, m + j + 3, l + i + 3, m + j + 4, 400, -267386864, -267386864);
//                        fillGradient(matrix4f, bufferbuilder, l - 3, m - 3, l + i + 3, m + j + 3, 400, -267386864, -267386864);
//                        fillGradient(matrix4f, bufferbuilder, l - 4, m - 3, l - 3, m + j + 3, 400, -267386864, -267386864);
//                        fillGradient(matrix4f, bufferbuilder, l + i + 3, m - 3, l + i + 4, m + j + 3, 400, -267386864, -267386864);
//                        fillGradient(matrix4f, bufferbuilder, l - 3, m - 3 + 1, l - 3 + 1, m + j + 3 - 1, 400, 1347420415, 1344798847);
//                        fillGradient(matrix4f, bufferbuilder, l + i + 2, m - 3 + 1, l + i + 3, m + j + 3 - 1, 400, 1347420415, 1344798847);
//                        fillGradient(matrix4f, bufferbuilder, l - 3, m - 3, l + i + 3, m - 3 + 1, 400, 1347420415, 1347420415);
//                        fillGradient(matrix4f, bufferbuilder, l - 3, m + j + 2, l + i + 3, m + j + 3, 400, 1344798847, 1344798847);
//
//                        RenderSystem.enableDepthTest();
//                        RenderSystem.disableTexture();
//                        RenderSystem.enableBlend();
//                        RenderSystem.defaultBlendFunc();
// IF forge-1.16.5
//                         RenderSystem.shadeModel(7425);
//                         bufferbuilder.end();
//                         net.minecraft.client.renderer.WorldVertexBufferUploader.end(bufferbuilder);
//                         RenderSystem.shadeModel(7424);
// ELSE IF > forge-1.16.5 && <= forge-1.19
////                        bufferbuilder.end();
////                        com.mojang.blaze3d.vertex.BufferUploader.end(bufferbuilder);
// END IF
//
//                        RenderSystem.disableBlend();
//                        RenderSystem.enableTexture();
// IF forge-1.16.5
//                         pMatrixStack.translate(0.0D, 0.0D, 400.0D);
//                         RenderSystem.blendColor(1.0F, 1.0F, 1.0F, 1.0F);
//                         RenderSystem.bindTexture(Objects.requireNonNull(Minecraft.getInstance().getTextureManager().getTexture((#ResourceLocation#) frame.getId())).getId());
// ELSE IF > forge-1.16.5 && <= forge-1.19
////                        pMatrixStack.translate(0.0F, 0.0F, 400.0F);
////                        //RenderSystem.setShader(net.minecraft.client.renderer.GameRenderer::getPositionColorShader);
////                        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
////                        RenderSystem.setShaderTexture(0, (#ResourceLocation#) frame.getId());
// END IF
//
//                        #GuiComponent#.blit(pMatrixStack, l + CONFIG.paddingLeft, m + CONFIG.paddingTop, 0, 0, viewWidth, viewHeight, viewWidth, viewHeight);
//                        pMatrixStack.popPose();
//                        frame.gifLoop(CONFIG.gifSpeed);
//                    } else {
//                        #MutableComponent# text = (#MutableComponent#) frame.getErrorMessage(
//                                (str) -> createLiteralComponent((String) str),
//                                (str) -> createTranslatableComponent((String) str),
//                                (obj, s) -> ((#MutableComponent#) obj).append((#Component#) s), code);
//                        this.renderTooltip(pMatrixStack, this.minecraft.font.split(text, Math.max(this.width / 2, 200)), pMouseX, pMouseY);
//                    }
//                } else {
//                    this.renderTooltip(pMatrixStack, this.minecraft.font.split(createTranslatableComponent("nsfw.chatimage.message"), Math.max(this.width / 2, 200)), pMouseX, pMouseY);
//                }
//
//            }
//
//        }
//
//    }
//
    // END IF
    @Unique
    private void chatimage$confirmNsfw(boolean open) {
        if (open) {
            ClientStorage.AddNsfw(chatimage$nsfwUrl, 1);
        }
        this.chatimage$nsfwUrl = null;
        this.minecraft.setScreen((#Screen#) (Object) this);
    }

    @Inject(at = @At("RETURN"),
            method = "handleComponentClicked", cancellable = true)
    private void handleComponentClicked(#Style# style, CallbackInfoReturnable<Boolean> cir) {
        if (style != null && style.getHoverEvent() != null) {
            #HoverEvent# hoverEvent = style.getHoverEvent();
            ChatImageCode code = hoverEvent.getValue(SHOW_IMAGE);
            if (code != null && code.isNsfw() && !ClientStorage.ContainNsfw(code.getUrl()) && !CONFIG.nsfw) {
                this.chatimage$nsfwUrl = code.getUrl();
                this.minecraft.setScreen(new ConfirmNsfwScreen(this::chatimage$confirmNsfw, chatimage$nsfwUrl));
                cir.setReturnValue(true);
            }
        }
    }
}