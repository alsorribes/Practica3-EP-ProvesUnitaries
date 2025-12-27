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


    @Test
    @DisplayName("getSignature returns a defensive copy")
    void testGetterReturnsCopy() {
        byte[] original = {4, 5, 6};
        DigitalSignature signature = new DigitalSignature(original);

        byte[] obtained = signature.getSignature();
        obtained[0] = 9;

        assertArrayEquals(new byte[]{4, 5, 6}, signature.getSignature());
    }


    @Test
    @DisplayName("Constructor accepts null signature")
    void testConstructorWithNull() {
        DigitalSignature signature = new DigitalSignature(null);

        assertNull(signature.getSignature());
    }


    // -------------- EQUALS CONTRACT -----------------
    @Test
    @DisplayName("equals - Same object returns true")
    void testEqualsSameObject() {
        byte[] data = {1, 2, 3};
        DigitalSignature signature = new DigitalSignature(data);

        assertEquals(signature, signature);
    }


    @Test
    @DisplayName("equals - Different objects with same content are equal")
    void testEqualsSameContent() {
        DigitalSignature sig1 = new DigitalSignature(new byte[]{1, 2, 3});
        DigitalSignature sig2 = new DigitalSignature(new byte[]{1, 2, 3});

        assertEquals(sig1, sig2);
    }


    @Test
    @DisplayName("equals - Different content is not equal")
    void testEqualsDifferentContent() {
        DigitalSignature sig1 = new DigitalSignature(new byte[]{1, 2, 3});
        DigitalSignature sig2 = new DigitalSignature(new byte[]{9, 8, 7});

        assertNotEquals(sig1, sig2);
    }


    @Test
    @DisplayName("equals - Compared with null returns false")
    void testEqualsNull() {
        DigitalSignature signature = new DigitalSignature(new byte[]{1});

        assertNotEquals(signature, null);
    }
}
