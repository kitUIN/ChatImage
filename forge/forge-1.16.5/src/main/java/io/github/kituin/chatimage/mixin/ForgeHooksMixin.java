package io.github.kituin.chatimage.mixin;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.ForgeHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ForgeHooks.class)
public abstract class ForgeHooksMixin {

    /**
     * @author kitUIN
     * @reason 禁止forge的url解析
     */
    @Inject(method = "newChatWithLinks(Ljava/lang/String;Z)Lnet/minecraft/util/text/ITextComponent;",
            at = @At("RETURN"), cancellable = true,remap = false)
    private static void newChatWithLinks(String string, boolean allowMissingHeader, CallbackInfoReturnable<ITextComponent> cir){
        System.out.println(string);
        cir.setReturnValue(new StringTextComponent(string));
    }

}
