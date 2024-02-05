package github.kituin.chatimage.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.kituin.ChatImageCode.ChatImageCode;
import io.github.kituin.ChatImageCode.ChatImageFrame;
import io.github.kituin.ChatImageCode.ClientStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static github.kituin.chatimage.ChatImage.CONFIG;
import static github.kituin.chatimage.tool.ChatImageStyle.SHOW_IMAGE;


/**
 * 注入修改悬浮显示图片
 *
 * @author kitUIN
 */
@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin implements net.minecraftforge.client.extensions.IForgeGuiGraphics {


    @Shadow
    @Final
    private Minecraft minecraft;


    @Shadow
    public abstract void drawManaged(Runnable p_286277_);

    @Shadow
    public abstract void renderTooltip(Font p_282192_, List<? extends FormattedCharSequence> p_282297_, int p_281680_, int p_283325_);
    @Shadow
    public abstract void blit(ResourceLocation p_283272_, int p_283605_, int p_281879_, float p_282809_, float p_282942_, int p_281922_, int p_282385_, int p_282596_, int p_281699_);
    @Shadow
    public abstract int guiWidth() ;
    @Shadow
    public abstract int guiHeight();
    @Shadow
    @Final
    private PoseStack pose;


    @Inject(at = @At("RETURN"),
            method = "renderComponentHoverEffect")
    protected void renderComponentHoverEffect(Font p_282584_, @Nullable Style p_282156_, int p_283623_, int p_282114_, CallbackInfo ci) {
        if (p_282156_ != null && p_282156_.getHoverEvent() != null) {
            HoverEvent hoverEvent = p_282156_.getHoverEvent();
            ChatImageCode code = hoverEvent.getValue(SHOW_IMAGE);
            if (code != null) {
                if (CONFIG.nsfw || !code.isNsfw() || ClientStorage.ContainNsfw(code.getUrl())) {
                    ChatImageFrame frame = code.getFrame();
                    if (frame.loadImage(CONFIG.limitWidth, CONFIG.limitHeight)) {
                        int viewWidth = frame.getWidth();
                        int viewHeight = frame.getHeight();
                        int i = viewWidth + CONFIG.paddingLeft + CONFIG.paddingRight; // 长度
                        int j = viewHeight + CONFIG.paddingTop + CONFIG.paddingBottom; // 高度
                        int left = p_283623_ + 6;
                        int top = p_282114_ - 6;
                        int width = guiWidth();
                        int height = guiHeight();
                        if (left + i + 6 > width) { // 若超出窗口
                            left = width - i - 6;
                        }
                        if (top + j + 6 > height) { // 若超出窗口
                            top = height - j - 6;
                        }
                        this.pose.pushPose();
                        int finalL = left;
                        int finalM = top;
                        this.drawManaged(() -> {
                            TooltipRenderUtil.renderTooltipBackground(((GuiGraphics)(Object)this), finalL, finalM , i, j, 400);
                        });
                        this.pose.translate(0.0F, 0.0F, 400.0F);

                        blit((ResourceLocation) frame.getId(), left + CONFIG.paddingLeft, top + CONFIG.paddingTop, 0, 0, viewWidth, viewHeight, viewWidth, viewHeight);
                        pose.popPose();

                        frame.gifLoop(CONFIG.gifSpeed);
                    } else {
                        MutableComponent text = (MutableComponent) frame.getErrorMessage(
                                (str) -> Component.literal((String) str),
                                (str) -> Component.translatable((String) str),
                                (obj, s) -> ((MutableComponent) obj).append((Component) s), code);
                        this.renderTooltip(p_282584_, this.minecraft.font.split(text, Math.max(guiWidth() / 2, 200)), p_283623_, p_282114_);
                    }
                } else {
                    this.renderTooltip(p_282584_, this.minecraft.font.split(Component.translatable("nsfw.chatimage.message"), Math.max(guiWidth() / 2, 200)), p_283623_, p_282114_);
                }

            }

        }

    }

}
