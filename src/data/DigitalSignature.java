package data;

import java.util.Arrays;

/**
 * TEMPORARY -
 * The digital signature of the doctor.
 */
public final class DigitalSignature {
    private final byte[] signature;

    public DigitalSignature(byte[] signature) {
        this.signature = signature != null ? signature.clone() : null;
    }

    public byte[] getSignature() {
        return signature != null ? signature.clone() : null;
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
        return "DigitalSignature{" + "signature=" + Arrays.toString(signature) + '}';
    }
}