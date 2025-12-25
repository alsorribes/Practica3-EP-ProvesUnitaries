package exceptions;

/**
 * Exception thrown when a prompt sent to the AI is unclear or inconsistent.
 */
public class BadPromptException extends Exception {

    public BadPromptException() {
        super("The prompt is unclear or inconsistent for the AI");
    }

    public BadPromptException(String message) {
        super(message);
    }
}