package test.data;

import data.DigitalSignature;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DigitalSignature value object.
 */
@DisplayName("DigitalSignature - Unit Tests")
public class DigitalSignatureTest {
    // ---------- CONSTRUCTOR AND GETTER ----------------
    @Test
    @DisplayName("Constructor stores a copy of the signature")
    void testConstructorStoresCopy() {
        byte[] original = {1, 2, 3};

        DigitalSignature signature = new DigitalSignature(original);

        // Modify original array
        original[0] = 9;

        assertArrayEquals(new byte[]{1, 2, 3}, signature.getSignature());
    }
}
