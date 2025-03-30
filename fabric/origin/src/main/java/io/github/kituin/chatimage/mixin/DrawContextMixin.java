// ONLY >= fabric-1.20
package io.github.kituin.chatimage.mixin;

import io.github.kituin.ChatImageCode.ChatImageCode;
import io.github.kituin.ChatImageCode.ChatImageFrame;
import io.github.kituin.ChatImageCode.ClientStorage;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.HoveredTooltipPositioner;
import net.minecraft.client.gui.tooltip.TooltipBackgroundRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.*;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2ic;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

// IF >= neoforge-1.21.2
import java.util.function.Function;
import net.minecraft.client.render.RenderLayer;
// END IF
// IF >=fabric-1.21.5
//import static io.github.kituin.chatimage.tool.ChatImageStyle.ShowImage;
// ELSE
// import static io.github.kituin.chatimage.tool.ChatImageStyle.SHOW_IMAGE;
// END IF
import static io.github.kituin.chatimage.tool.SimpleUtil.createLiteralComponent;
import static io.github.kituin.chatimage.tool.SimpleUtil.createTranslatableComponent;
import static #kituin$ChatImageConfig#;
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
    public abstract void drawTexture(
// IF >= fabric-1.21.2
//            Function<Identifier, RenderLayer> renderLayers,
// END IF
            Identifier texture, int x, int y,  float u, float v, int width, int height, int textureWidth, int textureHeight);
// IF < fabric-1.21.2
//     @Shadow
//     public abstract void draw(Runnable drawCallback);
// END IF
    @SuppressWarnings("t")
    @Inject(at = @At("RETURN"), method = "drawHoverEvent")
    public void drawHoverEvent(TextRenderer textRenderer, Style style, int x, int y, CallbackInfo ci) {
        if (style != null && style.getHoverEvent() != null) {
            HoverEvent hoverEvent = style.getHoverEvent();
// IF >=fabric-1.21.5
//            if (!(hoverEvent instanceof ShowImage(ChatImageCode code)))return;
// ELSE
//             ChatImageCode code = hoverEvent.getValue(SHOW_IMAGE);
// END IF
            if (code != null) {
                if (CONFIG.nsfw || !code.isNsfw() || ClientStorage.ContainNsfw(code.getUrl())) {
                    ChatImageFrame frame = code.getFrame();
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
// IF >= fabric-1.21.2
//                        TooltipBackgroundRenderer.render((DrawContext) (Object)this, l, m, allWidth,allHeight, 400,null);
// ELSE
//                         this.draw(() -> {
//                             TooltipBackgroundRenderer.render((DrawContext) (Object)this, l, m, allWidth,allHeight, 400);
//                         });
// END IF
                        this.matrices.translate(0.0F, 0.0F, 400.0F);

                        // 图片
                        this.drawTexture(
// IF >= fabric-1.21.2
//                                RenderLayer::getGuiTextured,
// END IF
                                (Identifier) frame.getId(), l + CONFIG.paddingLeft, m + CONFIG.paddingTop, 0, 0, viewWidth, viewHeight, viewWidth, viewHeight);

                        this.matrices.pop();
                        frame.gifLoop(CONFIG.gifSpeed);
                    } else {
                        MutableText text = (MutableText) frame.getErrorMessage(
                                (str) -> createLiteralComponent((String) str),
                                (str) -> createTranslatableComponent((String) str),
                                (obj, s) -> ((MutableText) obj).append((Text) s), code);
                        this.drawOrderedTooltip(textRenderer, textRenderer.wrapLines(text, Math.max(this.getScaledWindowWidth() / 2, 200)), x, y);
                    }
                } else {
                    this.drawOrderedTooltip(textRenderer, textRenderer.wrapLines(createTranslatableComponent("nsfw.chatimage.message"), Math.max(this.getScaledWindowWidth() / 2, 200)), x, y);
                }

            }

        }
    }
}