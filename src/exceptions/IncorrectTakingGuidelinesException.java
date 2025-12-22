package exceptions;

/**
 * Exception thrown when taking guidelines format is incorrect or incomplete.
 */
public class IncorrectTakingGuidelinesException extends Exception {

    public IncorrectTakingGuidelinesException() {
        super("Taking guidelines are incorrect or incomplete");
    }

    public IncorrectTakingGuidelinesException(String message) {
        super(message);
    }
}