package github.kituin.chatimage.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.gui.screen.ChatScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.file.Path;
import java.util.List;

@Mixin(Mouse.class)
public class FileDragMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(at = @At("RETURN"), method = "method_29616")
    private void method_29616(long window, List<Path> paths, CallbackInfo ci) {
        if (this.client.currentScreen != null && this.client.world != null) {
            StringBuilder sb = new StringBuilder();
            for (Path o : paths) {
                   sb.append("[[CICode,url=file:///").append(o).append("]]");
            }
            this.client.openScreen(new ChatScreen(sb.toString()));
        }
    }
}
