package github.kituin.chatimage.mixin;

import net.minecraft.client.Keyboard;
import org.apache.commons.codec.digest.DigestUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static github.kituin.chatimage.client.ChatImageClient.CONFIG;

/**
 * 注入修改剪切板,支持粘贴图片
 * @author kitUIN
 */
@Mixin(Keyboard.class)
public class ClipboradMixin {


    @Inject(at = @At("RETURN"), method = "getClipboard", cancellable = true)
    public void getClipboard(CallbackInfoReturnable<String> cir) {
        try {
            Transferable trans = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
            BufferedImage image = null;
            if (trans != null && trans.isDataFlavorSupported(DataFlavor.imageFlavor)) {
                Object objt = trans.getTransferData(DataFlavor.imageFlavor);
                if (objt instanceof BufferedImage) {
                    image = (BufferedImage) objt;
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(image, "png", baos);
                    byte[] byteArr = baos.toByteArray();
                    String fileName = CONFIG.cachePath + "/" + DigestUtils.md5Hex(byteArr) + ".png";
                    File outputfile = new File(fileName);
                    ImageIO.write(image, "png", outputfile);
                    cir.setReturnValue("[CICode,url=file:///" + outputfile.getAbsolutePath() + "]");
                }
            }
        } catch (IOException | UnsupportedFlavorException | IllegalStateException e) {
            // e.printStackTrace();
        }


    }
}
