package io.github.kituin.chatimage.mixin;

import io.github.kituin.chatimage.gui.ConfirmNsfwScreen;
import io.github.kituin.ChatImageCode.ChatImageCode;
import io.github.kituin.ChatImageCode.ClientStorage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static io.github.kituin.chatimage.client.ChatImageClient.CONFIG;
import static io.github.kituin.chatimage.tool.ChatImageStyle.SHOW_IMAGE;
import static io.github.kituin.chatimage.tool.SimpleUtil.setScreen;

@Environment(EnvType.CLIENT)
@Mixin(BookScreen.class)
public abstract class BookScreenMixin extends Screen {

    @Unique
    private String nsfwUrl;

    protected BookScreenMixin(Text title) {
        super(title);
    }

    @Unique
    private void confirmNsfw(boolean open) {
        if (open) {
            ClientStorage.AddNsfw(nsfwUrl, 1);
        }
        this.nsfwUrl = null;
        if (this.client != null) {
            setScreen(this.client, (Screen) (Object) this);
        }
    }

    @Inject(at = @At("RETURN"),
            method = "handleTextClick", cancellable = true)
    private void handleTextClick(Style style, CallbackInfoReturnable<Boolean> cir) {
        if (style != null && style.getHoverEvent() != null) {
            HoverEvent hoverEvent = style.getHoverEvent();
            ChatImageCode code = hoverEvent.getValue(SHOW_IMAGE);
            if (code != null && code.isNsfw() && !ClientStorage.ContainNsfw(code.getUrl()) && !CONFIG.nsfw) {
                this.nsfwUrl = code.getUrl();
                if (this.client != null) {
                    setScreen(this.client, new ConfirmNsfwScreen(this::confirmNsfw, nsfwUrl));
                }
                cir.setReturnValue(true);
            }
        }
    }
}