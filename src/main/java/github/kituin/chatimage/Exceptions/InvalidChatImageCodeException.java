package github.kituin.chatimage.Exceptions;


/**
 * @author kituin
 */
public class InvalidChatImageCodeException extends Exception
{
    private final String message;
    private final InvalidCodeMode codeMode;
    private InvalidChatImageUrlException.InvalidUrlMode urlMode = InvalidChatImageUrlException.InvalidUrlMode.NotFound;
    public InvalidChatImageCodeException(String message)
    {
        this.message = message;
        this.codeMode = InvalidCodeMode.CodeInvalid;
    }
    public InvalidChatImageCodeException(String message, InvalidChatImageUrlException.InvalidUrlMode urlMode)
    {
        this.message = message;
        this.codeMode = InvalidCodeMode.URLInvalid;
        this.urlMode = urlMode;
    }
    public String getMessage()
    {
        return this.message;
    }
    public InvalidCodeMode getCodeMode()
    {
        return this.codeMode;
    }public InvalidChatImageUrlException.InvalidUrlMode getUrlMode()
    {
        return this.urlMode;
    }
    public enum InvalidCodeMode
    {
        /**
         * URL 出错
         */
        URLInvalid,
        /**
         * Code 出错
         */
        CodeInvalid
    }
}
