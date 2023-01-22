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
    private String base64Data;
    private UrlMethod urlMethod;
    private String fileUrl;

    ChatImageUrl(String url) throws InvalidChatImageUrlException {
        this.originalUrl = url;
        init();
    }
    private void init() throws InvalidChatImageUrlException {
        File folder = new File(CACHE_PATH);
        if(!folder.exists()){ folder.mkdirs(); }

        if(this.originalUrl.startsWith("http"))
        {
            this.urlMethod = UrlMethod.HTTP;
            httpUrl = this.originalUrl.replace("http://","").replace("https://","");
            try
            {
                Path path = FileSystems.getDefault().getPath(httpUrl);
                this.cachePathUrl = CACHE_PATH + "/" + path.getFileName().toString();
            }
            catch (java.nio.file.InvalidPathException ep)
            {
                throw new InvalidChatImageUrlException(ep.getMessage());
            }

        }
        else if (this.originalUrl.startsWith("data:image/"))
        {
            this.urlMethod = UrlMethod.BASE64;
            this.base64Data = this.originalUrl.replace("data:image/jpeg;base64,", "")
                    .replace("data:image/png;base64,", "");
        }
        else if (this.originalUrl.startsWith("file:///"))
        {
            this.fileUrl = this.originalUrl.replace("\\\\", "/").replace("\\","/");
//            URI url2 = URI.create(url);
//            System.out.println(url2);
//            File file = new  File(url2);
//            inputStream = new FileInputStream(file);

        }
        else
        {
            throw new InvalidChatImageUrlException("can not match any url method");
        }
    }
    public static ChatImageUrl of(String url) throws InvalidChatImageUrlException
    {
        return new ChatImageUrl(url);
    }
    public String getOriginalUrl()
    {
        return this.originalUrl;
    }
    public UrlMethod getUrlMethod()
    {
        return this.urlMethod;
    }
    public String getCachePathUrl()
    {
        return this.cachePathUrl;
    }
    public String getBase64Data()
    {
        return this.base64Data;
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
         * base64格式
         */
        BASE64,
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
