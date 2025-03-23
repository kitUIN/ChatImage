package io.github.kituin.chatimage.mixin;

import io.github.kituin.chatimage.paste.PasteToolkit;
import net.minecraft.client.main.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Main.class)
public class AWTHackMixin {

    @Inject(method = "main", at = @At("HEAD"), remap = false)
    private static void awtHack(CallbackInfo ci) {
        if (!PasteToolkit.isMac()) {
            System.setProperty("java.awt.headless", "false");
        }
    }

}