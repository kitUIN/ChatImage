package io.github.kituin.chatimage.mixin;

import io.github.kituin.chatimage.gui.ConfirmNsfwScreen;
import io.github.kituin.ChatImageCode.ChatImageCode;
import io.github.kituin.ChatImageCode.ClientStorage;
import io.github.kituin.chatimage.tool.ChatImageStyle;
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
        this.client.setScreen((Screen) (Object) this);
    }

    @Inject(at = @At("RETURN"),
            method = "handleTextClick", cancellable = true)
    private void handleTextClick(Style style, CallbackInfoReturnable<Boolean> cir) {
        if (style != null && style.getHoverEvent() != null) {
            HoverEvent hoverEvent = style.getHoverEvent();
            ChatImageCode code = hoverEvent.getValue(ChatImageStyle.SHOW_IMAGE);
            if (code != null && code.isNsfw() && !ClientStorage.ContainNsfw(code.getUrl()) && !CONFIG.nsfw) {
                this.nsfwUrl = code.getUrl();
                this.client.setScreen(new ConfirmNsfwScreen(this::confirmNsfw, nsfwUrl));
                cir.setReturnValue(true);
            }
        }
    }
}
