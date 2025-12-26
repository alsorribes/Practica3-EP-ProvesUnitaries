package exceptions;

/**
 * Exception thrown when incorrect parameters are provided to a method or constructor.
 */
public class IncorrectParametersException extends Exception {
    public IncorrectParametersException(String message) {
        super(message);
    }
}
