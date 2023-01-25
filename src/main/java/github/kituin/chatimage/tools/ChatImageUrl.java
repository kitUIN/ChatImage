package github.kituin.chatimage.tools;

import github.kituin.chatimage.Exceptions.InvalidChatImageUrlException;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import static github.kituin.chatimage.client.ChatImageClient.CACHE_PATH;

public class ChatImageUrl {
    private String originalUrl;
    private String httpUrl;
    private String cachePathUrl;
    private UrlMethod urlMethod;
    private String fileUrl;


    ChatImageUrl(String url) throws InvalidChatImageUrlException {
        this.originalUrl = url;
        init();
    }

    private void init() throws InvalidChatImageUrlException {
        File folder = new File(CACHE_PATH);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        if (this.originalUrl.startsWith("http")) {
            this.urlMethod = UrlMethod.HTTP;
            httpUrl = this.originalUrl.replace("http://", "").replace("https://", "");
            try {
                Path path = FileSystems.getDefault().getPath(httpUrl);
                this.cachePathUrl = CACHE_PATH + "/" + path.getFileName().toString();
            } catch (java.nio.file.InvalidPathException ep) {
                throw new InvalidChatImageUrlException(ep.getMessage());
            }

        } else if (this.originalUrl.startsWith("file:///")) {
            this.urlMethod = UrlMethod.FILE;
            this.fileUrl = this.originalUrl.replace("\\", "\\\\").replace("file:///", "");
        } else {
            throw new InvalidChatImageUrlException(originalUrl + "<-this url is invalid, Please Recheck");
        }
    }

    public static ChatImageUrl of(String url) throws InvalidChatImageUrlException {
        return new ChatImageUrl(url);
    }

    public String getOriginalUrl() {
        return this.originalUrl;
    }

    public UrlMethod getUrlMethod() {
        return this.urlMethod;
    }

    public String getCachePathUrl() {
        return this.cachePathUrl;
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
