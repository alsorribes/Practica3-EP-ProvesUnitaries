package exceptions;

/**
 * Exception thrown when there's a problem with AI system functionality.
 */
public class AIException extends Exception {

    public AIException() {
        super("Error in AI system operation");
    }

    public AIException(String message) {
        super(message);
    }
}