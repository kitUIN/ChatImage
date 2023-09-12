package github.kituin.chatimage.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHelper;
import net.minecraft.client.gui.screen.ChatScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.file.Path;
import java.util.List;

@Mixin(MouseHelper.class)
public class FileDragMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(at = @At("RETURN"), method = "addPacksToScreen")
    private void onFilesDropped(long window, List<Path> paths, CallbackInfo ci) {
        if (this.minecraft.currentScreen != null && this.minecraft.player != null) {
            StringBuilder sb = new StringBuilder();
            for (Path o : paths) {
                sb.append("[[CICode,url=file:///").append(o).append("]]");
            }
            this.minecraft.displayGuiScreen(new ChatScreen(sb.toString()));
        }
    }
}
