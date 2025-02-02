package io.github.kituin.chatimage.paste;

import io.github.kituin.chatimage.ChatImage;
import org.apache.commons.codec.digest.DigestUtils;

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

import static io.github.kituin.ChatImageCode.ChatImageCodeInstance.CLIENT_ADAPTER;
import static io.github.kituin.chatimage.client.ChatImageClient.CONFIG;

public class WindowsPasteCompat implements IPasteCompat {
    @Override
    public String doPaste() {
        try {
            Transferable trans = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
            if (trans == null) return null;
            if (trans.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                StringBuilder sb = new StringBuilder();
                Object object = trans.getTransferData(DataFlavor.javaFileListFlavor);
                if (object instanceof List<?> obj) {
                    for (Object o : obj) {
                        sb.append(getImageCICode(o));
                    }
                    return sb.toString();
                }
            } else if (trans.isDataFlavorSupported(DataFlavor.imageFlavor)) {
                Object object = trans.getTransferData(DataFlavor.imageFlavor);
                return getImageCICode(object);
            }
        } catch (IOException | UnsupportedFlavorException | IllegalStateException e) {
            ChatImage.LOGGER.warn(e.getMessage());
        }
        return null;
    }

    private static final String TempFileType = "png";

    private String getImageCICode(Object object) throws IOException {
        StringBuilder sb = new StringBuilder();
        if (object instanceof File) {
            sb.append("[[CICode,url=file:///").append(((File) object).getPath()).append("]]");
        } else if (object instanceof BufferedImage) {
            BufferedImage image = (BufferedImage) object;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, TempFileType, baos);
            byte[] byteArr = baos.toByteArray();
            CLIENT_ADAPTER.checkCachePath();
            String fileName = CONFIG.cachePath + "/" + DigestUtils.md5Hex(byteArr) + "." + TempFileType;
            File outputfile = new File(fileName);
            ImageIO.write(image, TempFileType, outputfile);
            sb.append("[[CICode,url=file:///").append(outputfile.getAbsolutePath()).append("]]");
        } else {
            return null;
        }
        return sb.toString();
    }
}