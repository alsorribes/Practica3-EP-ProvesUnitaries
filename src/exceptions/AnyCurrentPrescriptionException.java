package exceptions;

/**
 * Exception thrown when no active prescription exists for a patient's specific illness.
 */
public class AnyCurrentPrescriptionException extends Exception {

    public AnyCurrentPrescriptionException() {
        super("No active prescription found for this illness");
    }

    public AnyCurrentPrescriptionException(String message) {
        super(message);
    }
}