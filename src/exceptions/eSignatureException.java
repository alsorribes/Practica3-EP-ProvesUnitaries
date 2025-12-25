package exceptions;

/**
 * Exception thrown when there's a problem stamping the electronic signature.
 */
public class eSignatureException extends Exception {

    public eSignatureException() {
        super("Error stamping electronic signature");
    }

    public eSignatureException(String message) {
        super(message);
    }
}