package exceptions;

/**
 * Exception thrown when a Health Card ID is not registered in the Health National Service.
 */
public class HealthCardIDException extends Exception {

    public HealthCardIDException() {
        super("Health Card ID not registered in the system");
    }

    public HealthCardIDException(String message) {
        super(message);
    }
}