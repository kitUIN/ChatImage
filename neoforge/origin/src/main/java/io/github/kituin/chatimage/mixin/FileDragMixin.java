package io.github.kituin.chatimage.mixin;

import io.github.kituin.chatimage.ChatImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.screens.ChatScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.file.Path;
import java.util.List;

@Mixin(MouseHandler.class)
public class FileDragMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(at = @At("RETURN"), method = "onDrop")
    private void onFilesDropped(long window, List<Path> paths, CallbackInfo ci) {
        if (this.minecraft.screen != null && this.minecraft.player != null) {
            StringBuilder sb = new StringBuilder();
            for (Path o : paths) {
                if (ChatImage.CONFIG.dragUseCicode) {
                    sb.append("[[CICode,url=file:///").append(o).append("]]");
                } else {
                    sb.append("file:///").append(o);
                }            }
            this.minecraft.setScreen(new ChatScreen(sb.toString()));
        }
    }
}
