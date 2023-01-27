package github.kituin.chatimage.Exceptions;

public class InvalidChatImageUrlException extends Exception{
    private final String message;
    private final InvalidUrlMode mode;
    public InvalidChatImageUrlException(String message, InvalidUrlMode mode)
    {
        this.message = message;
        this.mode = mode;
    }

    public String getMessage()
    {
        return this.message;
    }
    public InvalidUrlMode getMode()
    {
        return this.mode;
    }
    public enum InvalidUrlMode
    {
        /**
         * 文件不存在
         */
        FileNotFound,
        /**
         * http 连接无法访问
         */
        HttpNotFound,
        /**
         * 其他错误
         */
        NotFound
    }
}