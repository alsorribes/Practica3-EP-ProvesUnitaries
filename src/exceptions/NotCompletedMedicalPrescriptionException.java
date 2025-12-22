package exceptions;

/**
 * Exception thrown when a medical prescription is incomplete and cannot be validated.
 */
public class NotCompletedMedicalPrescriptionException extends Exception {

    public NotCompletedMedicalPrescriptionException() {
        super("Medical prescription is incomplete");
    }

    public NotCompletedMedicalPrescriptionException(String message) {
        super(message);
    }
}