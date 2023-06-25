package github.kituin.chatimage.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import github.kituin.chatimage.tool.ChatImageCode;
import github.kituin.chatimage.tool.ChatImageFrame;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
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
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static github.kituin.chatimage.client.ChatImageClient.CONFIG;
import static github.kituin.chatimage.tool.ChatImageCode.NSFW_MAP;
import static github.kituin.chatimage.tool.ChatImageHandler.AddChatImage;
import static github.kituin.chatimage.tool.ChatImageStyle.SHOW_IMAGE;

/**
 * 注入修改悬浮显示图片
 *
 * @author kitUIN
 */
@Mixin(DrawContext.class)
public abstract class DrawContextMixin {
    @Shadow
    @Nullable
    private MinecraftClient client;
    @Shadow
    @Final
    private MatrixStack matrices;
    @Shadow
    public abstract  int getScaledWindowWidth();
    @Shadow
    public abstract int getScaledWindowHeight();
    @Shadow
    public abstract void drawOrderedTooltip(TextRenderer textRenderer, List<? extends OrderedText> text, int x, int y);
    @Shadow
    public abstract void setShaderColor(float red, float green, float blue, float alpha);
    @Shadow
    public abstract void drawTexture(Identifier texture, int x, int y, int z, float u, float v, int width, int height, int textureWidth, int textureHeight);
    @Shadow
    public abstract void draw(Runnable drawCallback);
    @Inject(at = @At("RETURN"), method = "drawHoverEvent")
    public void drawHoverEvent(TextRenderer textRenderer, Style style, int x, int y, CallbackInfo ci) {
        if (style != null && style.getHoverEvent() != null) {
            HoverEvent hoverEvent = style.getHoverEvent();
            ChatImageCode view = hoverEvent.getValue(SHOW_IMAGE);
            if (view != null) {
                if (CONFIG.nsfw || !view.getNsfw() || NSFW_MAP.containsKey(view.getOriginalUrl())) {
                    ChatImageFrame frame = view.getFrame();
                    if (frame.loadImage(CONFIG.limitWidth, CONFIG.limitHeight)) {
                        int viewWidth = frame.getWidth();
                        int viewHeight = frame.getHeight();
                        int allWidth = viewWidth + CONFIG.paddingLeft + CONFIG.paddingRight;
                        int allHeight = viewHeight + CONFIG.paddingTop + CONFIG.paddingBottom;
                        HoveredTooltipPositioner positioner = (HoveredTooltipPositioner) HoveredTooltipPositioner.INSTANCE;
                        Vector2ic vector2ic = positioner.getPosition(this.getScaledWindowWidth(),this.getScaledWindowHeight(),x, y,allWidth,allHeight );
                        int l = vector2ic.x();
                        int m = vector2ic.y();
                        // 背景
                        this.matrices.push();
                        this.draw(() -> {
                            TooltipBackgroundRenderer.render((DrawContext) (Object)this, l, m, allWidth,allHeight, 400);
                        });
                        this.matrices.translate(0.0F, 0.0F, 400.0F);
                        this.matrices.pop();

                        // 图片
                        this.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                        this.drawTexture((Identifier) frame.getId(), l + CONFIG.paddingLeft, m + CONFIG.paddingTop, 400,0, 0, viewWidth, viewHeight, viewWidth, viewHeight);

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
                            default ->
                                    text = Text.translatable(view.isTimeout() ? "error.chatimage.message" : "loading.chatimage.message");
                        }
                        this.drawOrderedTooltip(textRenderer, textRenderer.wrapLines(text, Math.max(this.getScaledWindowWidth() / 2, 200)), x, y);
                    }
                } else {
                    this.drawOrderedTooltip(textRenderer, textRenderer.wrapLines(Text.translatable("nsfw.chatimage.message"), Math.max(this.getScaledWindowWidth() / 2, 200)), x, y);
                }

            }

        }
    }
}