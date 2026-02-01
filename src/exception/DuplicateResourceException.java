package exception;

public class DuplicateResourceException extends InvalidInputException{
    public DuplicateResourceException(String message) {
        super(message);
    }

    public DuplicateResourceException(String message, Throwable cause) {
        super(message);
        initCause(cause);
    }
}
