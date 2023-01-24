package github.kituin.chatimage.mixin;

import com.mojang.logging.LogUtils;
import github.kituin.chatimage.Exceptions.InvalidChatImageCodeException;
import github.kituin.chatimage.tools.ChatImageCode;
import github.kituin.chatimage.tools.ChatImageStyle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;

import static github.kituin.chatimage.tools.ChatImageCode.pattern;
import static github.kituin.chatimage.tools.ChatImageTool.codePattern2;
import static github.kituin.chatimage.tools.ChatImageTool.replaceMessage;

/**
 * @author kitUIN
 */
@Mixin(ChatHud.class)
public class ChatHudMixin extends DrawableHelper {



    @ModifyVariable(at = @At("HEAD") ,
            method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V",
            argsOnly = true)
    public Text addMessage(Text message)
    {
        return replaceMessage(message);
    }
}
