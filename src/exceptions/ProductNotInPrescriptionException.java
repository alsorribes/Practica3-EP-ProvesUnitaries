package exceptions;

/**
 * Exception thrown when trying to modify/remove a product that doesn't exist in the prescription.
 */
public class ProductNotInPrescriptionException extends Exception {

    public ProductNotInPrescriptionException() {
        super("Product not found in the prescription");
    }

    public ProductNotInPrescriptionException(String message) {
        super(message);
    }
}