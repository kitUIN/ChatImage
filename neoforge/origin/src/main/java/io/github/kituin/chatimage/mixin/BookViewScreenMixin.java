package io.github.kituin.chatimage.mixin;

import io.github.kituin.chatimage.gui.ConfirmNsfwScreen;
import io.github.kituin.ChatImageCode.ChatImageCode;
import io.github.kituin.ChatImageCode.ClientStorage;
import io.github.kituin.chatimage.tool.ChatImageStyle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import #Component#;
import #HoverEvent#;
import #Style#;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static #kituin$ChatImageConfig#;

@Mixin(BookViewScreen.class)
public abstract class BookViewScreenMixin extends Screen {


    @Unique
    private String chatimage$nsfwUrl;

    protected BookViewScreenMixin(Component title) {
        super(title);
    }

    @Unique
    private void chatimage$confirmNsfw(boolean open) {
        if (open) {
            ClientStorage.AddNsfw(chatimage$nsfwUrl, 1);
        }
        this.chatimage$nsfwUrl = null;
        this.minecraft.setScreen((Screen) (Object) this);
    }

    @Inject(at = @At("RETURN"),
            method = "handleComponentClicked", cancellable = true)
    private void handleTextClick(Style style, CallbackInfoReturnable<Boolean> cir) {
        if (style != null && style.getHoverEvent() != null) {
            HoverEvent hoverEvent = style.getHoverEvent();
// IF >= neoforge-1.21.5
//            if (!(hoverEvent instanceof ChatImageStyle.ShowImage(ChatImageCode code)))return;
// ELSE
//             ChatImageCode code = hoverEvent.getValue(ChatImageStyle.SHOW_IMAGE);
// END IF
            if (code != null && code.isNsfw() && !ClientStorage.ContainNsfw(code.getUrl()) && !CONFIG.nsfw) {
                this.chatimage$nsfwUrl = code.getUrl();
                this.minecraft.setScreen(new ConfirmNsfwScreen(this::chatimage$confirmNsfw, chatimage$nsfwUrl));
                cir.setReturnValue(true);
            }
        }
    }
}