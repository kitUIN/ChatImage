package github.kituin.chatimage.tool;

import github.kituin.chatimage.exception.InvalidChatImageUrlException;

import java.io.File;

import static github.kituin.chatimage.client.ChatImageClient.CONFIG;
import static github.kituin.chatimage.tool.ChatImageCode.CACHE_MAP;
import static github.kituin.chatimage.tool.ChatImageHandler.loadFile;

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
                boolean f = ChatImageHttpHandler.getInputStream(this.httpUrl);
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
                    loadFile(this.fileUrl);
                    networkHelper.send(this.fileUrl, file, true);
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
        };
    }

    @Override
    public String toString() {
        return this.originalUrl;
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
