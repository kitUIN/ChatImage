package github.kituin.chatimage.mixin;

import com.github.chatimagecode.ChatImageCode;
import github.kituin.chatimage.gui.ConfirmNsfwScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.github.chatimagecode.ChatImageCode.NSFW_MAP;
import static github.kituin.chatimage.ChatImage.CONFIG;
import static github.kituin.chatimage.tool.ChatImageStyle.SHOW_IMAGE;

@Mixin(Screen.class)
public abstract class ScreenMixin extends AbstractContainerEventHandler implements Renderable {


    @Shadow
    protected Minecraft minecraft;
    @Unique
    private String chatimage$nsfwUrl;

    @Unique
    private void chatimage$confirmNsfw(boolean open) {
        if (open) {
            NSFW_MAP.put(chatimage$nsfwUrl, 1);
        }
        this.chatimage$nsfwUrl = null;
        this.minecraft.setScreen((Screen) (Object) this);
    }

    @Inject(at = @At("RETURN"),
            method = "handleComponentClicked")
    private void handleComponentClicked(Style style, CallbackInfoReturnable<Boolean> cir) {
        if (style != null && style.getHoverEvent() != null) {
            HoverEvent hoverEvent = style.getHoverEvent();
            ChatImageCode view = hoverEvent.getValue(SHOW_IMAGE);
            if (view != null && view.getNsfw() && !NSFW_MAP.containsKey(view.getOriginalUrl()) && !CONFIG.nsfw) {
                this.chatimage$nsfwUrl = view.getOriginalUrl();
                this.minecraft.setScreen(new ConfirmNsfwScreen(this::chatimage$confirmNsfw, chatimage$nsfwUrl));
            }
        }
    }
}
