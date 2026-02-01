package exception;

public class DatabaseOperationException extends Exception{
    public DatabaseOperationException(String message) {
        super(message);
    }
    public DatabaseOperationException(String message, Throwable cause) {
        super(message);
        initCause(cause);
    }
    public DatabaseOperationException(Throwable cause) {
        initCause(cause);
    }

}
