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
import java.util.List;

import static github.kituin.chatimage.client.ChatImageClient.CONFIG;

/**
 * 注入修改剪切板,支持粘贴图片
 *
 * @author kitUIN
 */
@Mixin(Keyboard.class)
public class ClipboardMixin {

    private static boolean isWindows() {
        return System.getProperty("os.name").toUpperCase().contains("WINDOWS");
    }

    @Inject(at = @At("RETURN"), method = "getClipboard", cancellable = true)
    public void getClipboard(CallbackInfoReturnable<String> cir) {
        if (isWindows()) {
            try {
                Transferable trans = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
                if (trans != null && trans.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    StringBuilder sb = new StringBuilder();
                    Object object = trans.getTransferData(DataFlavor.javaFileListFlavor);
                    if (object instanceof List<?> obj) {
                        for (Object o : obj) {
                            sb.append("[[CICode,url=file:///").append(((File) o).getPath()).append("]]");
                        }
                        cir.setReturnValue(sb.toString());
                    }
                } else if (trans != null && trans.isDataFlavorSupported(DataFlavor.imageFlavor)) {
                    Image image = (Image) trans.getTransferData(DataFlavor.imageFlavor);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    String fileType = "png";
                    ImageIO.write((BufferedImage) image, fileType, baos);
                    byte[] byteArr = baos.toByteArray();
                    String fileName = CONFIG.cachePath + "/" + DigestUtils.md5Hex(byteArr) + "." + fileType;
                    File outputfile = new File(fileName);
                    ImageIO.write((BufferedImage) image, fileType, outputfile);
                    cir.setReturnValue("[[CICode,url=file:///" + outputfile.getAbsolutePath() + "]]");
                }
            } catch (IOException | UnsupportedFlavorException | IllegalStateException e) {
                // e.printStackTrace();
            }
        }
    }
}
