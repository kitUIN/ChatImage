package github.kituin.chatimage.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import github.kituin.chatimage.gui.ConfirmNsfwScreen;
import io.github.kituin.ChatImageCode.ChatImageCode;
import io.github.kituin.ChatImageCode.ChatImageFrame;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.AbstractParentElement;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

import static github.kituin.chatimage.client.ChatImageClient.CONFIG;
import static github.kituin.chatimage.tool.ChatImageStyle.SHOW_IMAGE;
import static io.github.kituin.ChatImageCode.ChatImageCode.NSFW_MAP;
import static io.github.kituin.ChatImageCode.ChatImageHandler.AddChatImage;

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
            ChatImageCode view = hoverEvent.getValue(SHOW_IMAGE);
            if (view != null) {
                if (CONFIG.nsfw || !view.getNsfw() || NSFW_MAP.containsKey(view.getOriginalUrl())) {
                    ChatImageFrame frame = view.getFrame();
                    if (frame.loadImage(CONFIG.limitWidth, CONFIG.limitHeight)) {
                        int viewWidth = frame.getWidth();
                        int viewHeight = frame.getHeight();
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
                        BufferRenderer.drawWithShader(bufferBuilder.end());
                        RenderSystem.disableBlend();
                        RenderSystem.enableTexture();
                        matrices.translate(0.0F, 0.0F, 400.0F);
                        RenderSystem.setShader(GameRenderer::getPositionColorShader);
                        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                        RenderSystem.setShaderTexture(0, (Identifier) frame.getId());

                        drawTexture(matrices, l + CONFIG.paddingLeft, m + CONFIG.paddingTop, 0, 0, viewWidth, viewHeight, viewWidth, viewHeight);
                        matrices.pop();
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
                        MutableText text;
                        switch (frame.getError()) {
                            case FILE_NOT_FOUND -> {
                                if (view.isSendFromSelf()) {
                                    text = Text.literal(view.getChatImageUrl().getUrl())
                                            .append("\n↑")
                                            .append(Text.translatable("filenotfound.chatimage.exception"));
                                } else {
                                    text = Text.translatable(view.isTimeout() ? "error.server.chatimage.message" : "loading.server.chatimage.message");
                                }
                            }
                            case FILE_LOAD_ERROR -> text = Text.translatable("error.chatimage.message");
                            case SERVER_FILE_LOAD_ERROR -> text = Text.translatable("error.server.chatimage.message");
                            case ILLEGAL_CICODE_ERROR -> text = Text.translatable("illegalcode.chatimage.exception");
                            default ->
                                    text = Text.translatable(view.isTimeout() ? "error.chatimage.message" : "loading.chatimage.message");
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
            if (view != null && view.getNsfw() && !NSFW_MAP.containsKey(view.getOriginalUrl()) && !CONFIG.nsfw) {
                this.nsfwUrl = view.getOriginalUrl();
                this.client.setScreen(new ConfirmNsfwScreen(this::confirmNsfw, nsfwUrl));
            }
        }
    }
}
