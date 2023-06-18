package github.kituin.chatimage.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import github.kituin.chatimage.gui.ConfirmNsfwScreen;
import github.kituin.chatimage.tool.ChatImageCode;
import github.kituin.chatimage.tool.ChatImageFrame;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.AbstractParentElement;
import net.minecraft.client.gui.Drawable;
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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

import static github.kituin.chatimage.client.ChatImageClient.CONFIG;
import static github.kituin.chatimage.tool.ChatImageCode.NSFW_MAP;
import static github.kituin.chatimage.tool.ChatImageHandler.AddChatImage;
import static github.kituin.chatimage.tool.ChatImageStyle.SHOW_IMAGE;

/**
 * 注入点击nsfw
 *
 * @author kitUIN
 */
@Mixin(Screen.class)
public abstract class ScreenMixin extends AbstractParentElement implements Drawable {

    @Shadow
    public int width;

    @Shadow
    @Nullable
    protected MinecraftClient client;

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
