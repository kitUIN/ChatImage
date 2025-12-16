package io.github.kituin.chatimage.mixin;

import io.github.kituin.chatimage.client.ChatImageClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import #ChatScreen#;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.file.Path;
import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(Mouse.class)
public class FileDragMixin {

    @Shadow
    @Final
    private MinecraftClient client;

// IF fabric-1.16.5
//    @Inject(at = @At("RETURN"), method = "method_29616")
// ELSE
//    @Inject(at = @At("RETURN"), method = "onFilesDropped")
// END IF
    private void onFilesDropped(long window, List<Path> paths,
// IF >= fabric-1.21
//                                int invalidFilesCount,
// END IF
                                CallbackInfo ci) {
        if (this.#kituin$clientCurrentScreen# != null &&
                this.#kituin$clientCurrentScreen# instanceof ChatScreen &&
                this.client.world != null && ChatImageClient.CONFIG.dragImage) {
            StringBuilder sb = new StringBuilder();
            for (Path o : paths) {
                if (ChatImageClient.CONFIG.dragUseCicode) {
                    sb.append("[[CICode,url=file:///").append(o).append("]]");
                } else {
                    sb.append("file:///").append(o);
                }
            }
// IF >= fabric-1.21.9
//            this.client.setScreen(new ChatScreen(sb.toString(), true));
// ELSE IF fabric-1.16.5
//            this.client.openScreen(new ChatScreen(sb.toString()));
// ELSE
//             this.client.setScreen(new ChatScreen(sb.toString()));
// END IF
        }
    }
}