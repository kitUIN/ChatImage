package github.kituin.chatimage.Exceptions;

public class InvalidChatImageUrlException extends Exception{
    private final String message;

    public InvalidChatImageUrlException(String message)
    {
        this.message = message;
    }
    public String getMessage()
    {
        return this.message;
    }
}