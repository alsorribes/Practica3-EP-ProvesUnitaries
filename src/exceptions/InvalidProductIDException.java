package exceptions;

/**
 * Exception thrown when a ProductID is invalid.
 */
public class InvalidProductIDException extends Exception{
    public InvalidProductIDException() {
        super("Invalid ProductID code.");
    }

    public InvalidProductIDException(String message) {
        super(message);
    }
}
