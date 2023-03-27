package github.kituin.chatimage.tool;


import com.madgag.gif.fmsware.GifDecoder;
import net.sf.image4j.codec.ico.ICODecoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static github.kituin.chatimage.ChatImage.LOGGER;
import static github.kituin.chatimage.tool.ChatImageCode.CACHE_MAP;

public class ChatImageHandler {
    /**
     * 添加进入本地图片序列
     *
     * @param frame 图片
     * @param url   url
     */
    public static void AddChatImage(ChatImageFrame frame, String url) {
        CACHE_MAP.put(url, frame);
        LOGGER.info("[ChatImageHandler][AddChatImage]添加图片" + url + " 成功");
    }

    public static void AddChatImage(BufferedImage image, String url) {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(image, "png", os);
            AddChatImage(new ByteArrayInputStream(os.toByteArray()), url);
        } catch (IOException e) {
            CACHE_MAP.put(url, new ChatImageFrame<>(ChatImageFrame.FrameError.FILE_LOAD_ERROR));
            LOGGER.error("[ChatImageHandler][AddChatImage]添加图片:" + url + " ImageIO.write失败");
        }
    }

    public static void AddChatImage(InputStream image, String url) {
        try {
            AddChatImage(new ChatImageFrame<>(image), url);
        } catch (IOException e) {
            CACHE_MAP.put(url, new ChatImageFrame<>(ChatImageFrame.FrameError.FILE_LOAD_ERROR));
            LOGGER.error("[ChatImageHandler][AddChatImage]添加图片:" + url + " 失败");
        }
    }

    /**
     * 添加进入本地图片序列报错
     *
     * @param url   url
     * @param error 报错
     */
    public static void AddChatImageError(String url, ChatImageFrame.FrameError error) {
        CACHE_MAP.put(url, new ChatImageFrame<>(error));
        LOGGER.error("[ChatImageHandler][AddChatImageError]添加图片:" + url + " 失败:" + error);
    }

    /**
     * 载入Gif
     *
     * @param is  输入流
     * @param url url
     */
    public static void loadGif(InputStream is, String url) {
        CompletableFuture.supplyAsync(() -> {
            try {
                GifDecoder gd = new GifDecoder();
                int status = gd.read(is);
                if (status != GifDecoder.STATUS_OK) {
                    AddChatImageError(url, ChatImageFrame.FrameError.FILE_LOAD_ERROR);
                    return null;
                }
                ChatImageFrame frame = new ChatImageFrame<>(gd.getFrame(0));
                for (int i = 1; i < gd.getFrameCount(); i++) {
                    frame.append(new ChatImageFrame<>(gd.getFrame(i)));
                }
                CACHE_MAP.put(url, frame);
            } catch (IOException ignored) {
                AddChatImageError(url, ChatImageFrame.FrameError.FILE_LOAD_ERROR);
            }
            return null;
        });
    }

    public static void loadGif(byte[] is, String url) {
        loadGif(new ByteArrayInputStream(is), url);
    }


    /**
     * 载入图片
     *
     * @param input InputStream
     * @param url   url
     */
    public static void loadFile(byte[] input, String url) {
        ChatImageCode.ChatImageType t = getPicType(input);
        try {
            switch (t) {
                case GIF -> loadGif(input, url);
                case ICO -> {
                    List<BufferedImage> images;
                    images = ICODecoder.read(new ByteArrayInputStream(input));
                    AddChatImage(images.get(0), url);
                }
                case PNG -> AddChatImage(ImageIO.read(new ByteArrayInputStream(input)), url);
                case WEBP -> {
//                    忽略Webp
//                    ImageReader reader = ImageIO.getImageReadersByMIMEType("image/webp").next();
//                    WebPReadParam readParam = new WebPReadParam();
//                    readParam.setBypassFiltering(true);
//                    reader.setInput(new MemoryCacheImageInputStream(new ByteArrayInputStream(input)));
//                    BufferedImage image = reader.read(0, readParam);
                }
            }
        } catch (IOException e) {
            LOGGER.error("[ChatImageHandler][loadFile]图片" + url + " ImageIO.read失败");
        }
    }

    public static void loadFile(InputStream input, String url) {
        try {
            byte[] is = input.readAllBytes();
            loadFile(is, url);
        } catch (IOException e) {
            LOGGER.error("[ChatImageHandler][loadFile]图片" + url + " InputStream读取失败");
        }
    }

    public static void loadFile(String url) {
        try {
            InputStream inputStream = new FileInputStream(url);
            loadFile(inputStream, url);
        } catch (FileNotFoundException e) {
            LOGGER.error("[ChatImageHandler][loadFile]图片" + url + " FileInputStream读取失败");
        }
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(aByte & 0xFF);
            if (hex.length() < 2) {
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public static ChatImageCode.ChatImageType getPicType(byte[] is) {
        byte[] b = new byte[4];
        System.arraycopy(is, 0, b, 0, b.length);
        String type_ = bytesToHex(b).toUpperCase();
        if (type_.startsWith("47494638")) {
            return ChatImageCode.ChatImageType.GIF;
        } else if (type_.startsWith("00000100")) {
            return ChatImageCode.ChatImageType.ICO;
        } else if (type_.startsWith("52494646")) {
            return ChatImageCode.ChatImageType.WEBP;
        } else {
            return ChatImageCode.ChatImageType.PNG;
        }
    }
}
