package github.kituin.chatimage.exception;

public class ImageInitException extends Exception{
    private final String message;

    public ImageInitException(String message)
    {
        this.message = message;
    }
    public String getMessage()
    {
        return this.message;
    }
}
