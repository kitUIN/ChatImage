package github.kituin.chatimage.mixin;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import static github.kituin.chatimage.tools.ChatImageTool.replaceMessage;

/**
 * @author kitUIN
 */
@Mixin(ChatHud.class)
public class ChatHudMixin extends DrawableHelper {


    @ModifyVariable(at = @At("HEAD"),
            method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V",
            argsOnly = true)
    public Text addMessage(Text message) {
        return replaceMessage(message);
    }
}
