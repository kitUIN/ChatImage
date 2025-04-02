package io.github.kituin.chatimage.mixin;

import io.github.kituin.chatimage.ChatImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import #ChatScreen#;
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
    private void onFilesDropped(long window, List<Path> paths,
// IF >= neoforge-1.21.0
                                int p_350707_,
// END IF
                                CallbackInfo ci) {
        if (this.#kituin$clientCurrentScreen# != null &&
                this.#kituin$clientCurrentScreen# instanceof ChatScreen &&
                ChatImage.CONFIG.dragImage &&
                this.minecraft.player != null) {
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