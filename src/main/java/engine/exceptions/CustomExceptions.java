package engine.exceptions;

public class CustomExceptions extends RuntimeException{
    public CustomExceptions(String message, Throwable cause) {
        super(message, cause);
    }
    public CustomExceptions(String message) {
        super(message);
    }
}
