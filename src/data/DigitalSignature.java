package data;

import java.util.Arrays;

/**
 * The digital signature of the doctor.
 * Represents a cryptographic signature for medical prescriptions.
 */
public final class DigitalSignature {
    private final byte[] signature;

    /**
     * Constructor for DigitalSignature.
     *
     * @param signature the byte array representing the signature (can be null for empty signatures)
     */
    public DigitalSignature(byte[] signature) {
        this.signature = signature != null ? signature.clone() : null;
    }

    /**
     * Returns a defensive copy of the signature.
     *
     * @return a copy of the signature byte array, or null if not set
     */
    public byte[] getSignature() {
        return signature != null ? signature.clone() : null;
    }

    /**
     * Checks if the signature is valid (not null and not empty).
     *
     * @return true if signature exists and has content, false otherwise
     */
    public boolean isValid() {
        return signature != null && signature.length > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DigitalSignature that = (DigitalSignature) o;
        return Arrays.equals(signature, that.signature);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(signature);
    }

    @Override
    public String toString() {
        if (signature == null) {
            return "DigitalSignature{signature=null}";
        }
        return "DigitalSignature{signature=" + Arrays.toString(signature) + '}';
    }
}