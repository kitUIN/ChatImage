package github.kituin.chatimage.tool;

import com.madgag.gif.fmsware.GifDecoder;
import github.kituin.chatimage.exception.InvalidChatImageUrlException;
import net.sf.image4j.codec.ico.ICODecoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static github.kituin.chatimage.client.ChatImageClient.CONFIG;
import static github.kituin.chatimage.tool.ChatImageCode.CACHE_MAP;

public class ChatImageUrl {
    private final String originalUrl;
    private String httpUrl;
    private final UrlMethod urlMethod;
    private String fileUrl;
    public static NetworkHelper networkHelper;

    public ChatImageUrl(String url) throws InvalidChatImageUrlException {
        this.originalUrl = url;
        File folder = new File(CONFIG.cachePath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        if (this.originalUrl.startsWith("http://") || this.originalUrl.startsWith("https://")) {
            this.urlMethod = UrlMethod.HTTP;
            this.httpUrl = this.originalUrl;
            if (!CACHE_MAP.containsKey(this.httpUrl)) {
                boolean f = HttpUtils.getInputStream(this.httpUrl);
                if (!f) {
                    throw new InvalidChatImageUrlException("Invalid HTTP URL",
                            InvalidChatImageUrlException.InvalidUrlMode.HttpNotFound);
                }
            }

        } else if (this.originalUrl.startsWith("file:///")) {
            this.urlMethod = UrlMethod.FILE;
            this.fileUrl = this.originalUrl
                    .replace("\\", "\\\\")
                    .replace("file:///", "");
            File file = new File(this.fileUrl);
            if (!CACHE_MAP.containsKey(this.fileUrl)) {
                if (file.exists()) {
                    try {
                        loadLocalFile(this.fileUrl);
                        networkHelper.send(this.fileUrl, file, true);
                    } catch (IOException e) {
                        CACHE_MAP.put(this.fileUrl, new ChatImageFrame<>(ChatImageFrame.FrameError.FILE_LOAD_ERROR));
                    }
                } else {
                    networkHelper.send(this.fileUrl, file, false);
                }
            }
        } else {
            throw new InvalidChatImageUrlException(originalUrl + "<- this url is invalid, Please Recheck",
                    InvalidChatImageUrlException.InvalidUrlMode.NotFound);
        }
    }

    @FunctionalInterface
    public interface NetworkHelper {
        void send(String url, File file, boolean isServer);
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

    public static String getPicType(byte[] is) {
        byte[] b = new byte[4];
        for (int i = 0; i < b.length; i++) {
            b[i] = is[i];
        }
        String type_ = bytesToHex(b).toUpperCase();
        System.out.println(type_.substring(0, 6));
        if (type_.startsWith("47494638")) {
            return "gif";
//        } else if (type_.startsWith("89504E47")) {
//            return "png";
//        } else if (type_.startsWith("FFD8FF")) {
//            return "jpg";
//        } else if (type_.startsWith("424D")) {
//            return "bmp";
        } else if (type_.startsWith("00000100")) {
            return "ico";
        } else {
            return "png";
        }
    }

    /**
     * 从url直接载入图片
     *
     * @param url url
     * @throws IOException IOException
     */
    public static void loadLocalFile(String url) throws IOException {
        InputStream inputStream = new FileInputStream(url);
        loadLocalFile(inputStream, url);
    }

    /**
     * 从InputStream直接载入图片
     *
     * @param input InputStream
     * @param url   url
     * @throws IOException IOException
     */
    public static void loadLocalFile(InputStream input, String url) throws IOException {
        byte[] is = input.readAllBytes();
        String t = getPicType(is);
        if ("gif".equals(t)) {
            loadGif(new ByteArrayInputStream(is), url);
        } else {
            try {
                BufferedImage image;
                if ("ico".equals(t)) {
                    List<BufferedImage> images = ICODecoder.read(new File(url));
                    image = images.get(0);
                } else {
                    image = ImageIO.read(new File(url));
                }
                CACHE_MAP.put(url, new ChatImageFrame<>(image));
            } catch (java.io.IOException e) {
                ChatImageCode.CACHE_MAP.put(url, new ChatImageFrame<>(ChatImageFrame.FrameError.FILE_LOAD_ERROR));
            }
        }
    }

    public String getOriginalUrl() {
        return this.originalUrl;
    }

    public UrlMethod getUrlMethod() {
        return this.urlMethod;
    }

    public String getUrl() {
        return switch (this.urlMethod) {
            case FILE -> this.fileUrl;
            case HTTP -> this.httpUrl;
            default -> this.originalUrl;
        };
    }

    @Override
    public String toString() {
        return this.originalUrl;
    }


    public static void loadGif(InputStream is, String url) {
        CompletableFuture.supplyAsync(() -> {
            try {
                GifDecoder gd = new GifDecoder();
                int status = gd.read(is);
                if (status != GifDecoder.STATUS_OK) {
                    CACHE_MAP.put(url, new ChatImageFrame(ChatImageFrame.FrameError.FILE_LOAD_ERROR));
                    return null;
                }
                ChatImageFrame frame = new ChatImageFrame<>(gd.getFrame(0));
                for (int i = 1; i < gd.getFrameCount(); i++) {
                    frame.append(new ChatImageFrame<>(gd.getFrame(i)));
                }
                CACHE_MAP.put(url, frame);
            } catch (IOException ignored) {
                CACHE_MAP.put(url, new ChatImageFrame(ChatImageFrame.FrameError.FILE_LOAD_ERROR));
            }
            return null;
        });
    }

    /**
     * Url的类型
     */
    public enum UrlMethod {
        /**
         * 本地文件 格式
         */
        FILE,
        /**
         * http(s)格式
         */
        HTTP,
    }

}
