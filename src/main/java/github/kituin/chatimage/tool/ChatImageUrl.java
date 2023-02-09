package github.kituin.chatimage.tool;

import com.luciad.imageio.webp.WebPReadParam;
import com.madgag.gif.fmsware.GifDecoder;
import github.kituin.chatimage.exception.InvalidChatImageUrlException;
import net.sf.image4j.codec.ico.ICODecoder;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static github.kituin.chatimage.Chatimage.CONFIG;
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




    /**
     * 从InputStream直接载入图片
     *
     * @param input InputStream
     * @param url   url
     * @throws IOException IOException
     */
    public static void putLocalFile(InputStream input, String url) throws IOException {
        if (url.endsWith(".gif")) {
            loadGif(input, url);
        } else {
            CACHE_MAP.put(url, new ChatImageFrame<>(input));
        }
    }

    /**
     * 从url直接载入图片
     *
     * @param url url
     * @throws IOException IOException
     */
    public static void loadLocalFile(String url) throws IOException {
        if (url.endsWith(".gif")) {
            loadGif(new FileInputStream(url), url);
        } else {
            BufferedImage input;
            if (url.endsWith(".ico")) {
                List<BufferedImage> image = ICODecoder.read(new File(url));
                input = image.get(0);
            }else if(url.endsWith(".webp")){
                ImageReader reader = ImageIO.getImageReadersByMIMEType("image/webp").next();
                WebPReadParam readParam = new WebPReadParam();
                readParam.setBypassFiltering(true);
                reader.setInput(new FileImageInputStream(new File(url)));
                input = reader.read(0, readParam);
            }else {
                input = ImageIO.read(new File(url));
            }
            CACHE_MAP.put(url, new ChatImageFrame<>(input));
        }
    }

    public String getOriginalUrl() {
        return this.originalUrl;
    }

    public UrlMethod getUrlMethod() {
        return this.urlMethod;
    }

    public String getUrl() {
        switch (this.urlMethod) {

            case FILE:
                return this.fileUrl;
            case HTTP:
                return this.httpUrl;
            default:
                return this.originalUrl;
        }

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
