package exceptions;

/**
 * Exception thrown when the treatment ending date is incorrect.
 * The date may be in the past, equal to current date, or too close to current date.
 */
public class IncorrectEndingDateException extends Exception {

    public IncorrectEndingDateException() {
        super("The ending date is incorrect (past, current, or too close)");
    }

    public IncorrectEndingDateException(String message) {
        super(message);
    }
}