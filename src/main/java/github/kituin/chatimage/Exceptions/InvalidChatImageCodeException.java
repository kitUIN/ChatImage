package github.kituin.chatimage.Exceptions;

/**
 * @author kituin
 */
public class InvalidChatImageCodeException extends Exception
{
    private final String message;

    public InvalidChatImageCodeException(String message)
    {
        this.message = message;
    }
    public String getMessage()
    {
        return this.message;
    }
}
