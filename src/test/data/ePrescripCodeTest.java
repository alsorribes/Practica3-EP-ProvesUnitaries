package test.data;

import data.ePrescripCode;

import exceptions.IncorrectParametersException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ePrescripCode value object.
 */
@DisplayName("ePrescripCode - Unit Tests")
public class ePrescripCodeTest {
    // ----------- CONSTRUCTOR AND GETTER
    @Test
    @DisplayName("Constructor - Stores prescription code correctly")
    void testConstructorStoresCode() throws IncorrectParametersException {
        // Arrange - Valid 16-character alphanumeric code
        String code = "EP20250001234567"; // 16 chars, no hyphens

        // Act
        ePrescripCode prescripCode = new ePrescripCode(code);

        // Assert
        assertEquals(code, prescripCode.getCode());
    }


    // ------------ EQUALS CONTRACT -------------
    @Test
    @DisplayName("equals - Same object returns true")
    void testEqualsSameObject() throws IncorrectParametersException {
        ePrescripCode code = new ePrescripCode("EP00000000000123"); // 16 chars

        assertEquals(code, code);
    }


    @Test
    @DisplayName("equals - Different objects with same value are equal")
    void testEqualsSameValue() throws IncorrectParametersException {
        ePrescripCode code1 = new ePrescripCode("EP00000000000123"); // 16 chars
        ePrescripCode code2 = new ePrescripCode("EP00000000000123"); // 16 chars

        assertEquals(code1, code2);
    }


    @Test
    @DisplayName("equals - Different values are not equal")
    void testEqualsDifferentValue() throws IncorrectParametersException {
        ePrescripCode code1 = new ePrescripCode("EP00000000000123"); // 16 chars
        ePrescripCode code2 = new ePrescripCode("EP00000000000987"); // 16 chars

        assertNotEquals(code1, code2);
    }


    @Test
    @DisplayName("equals - Compared with null returns false")
    void testEqualsNull() throws IncorrectParametersException {
        ePrescripCode code = new ePrescripCode("EP00000000000123"); // 16 chars

        assertNotEquals(code, null);
    }


    @Test
    @DisplayName("equals - Compared with different class returns false")
    void testEqualsDifferentClass() throws IncorrectParametersException {
        ePrescripCode code = new ePrescripCode("EP00000000000123"); // 16 chars
        String other = "EP00000000000123";

        assertNotEquals(code, other);
    }


    // --------------- HASHCODE CONTRACT --------------
    @Test
    @DisplayName("hashCode - Same value produces same hash code")
    void testHashCodeSameValue() throws IncorrectParametersException {
        ePrescripCode code1 = new ePrescripCode("EP00000000000123"); // 16 chars
        ePrescripCode code2 = new ePrescripCode("EP00000000000123"); // 16 chars

        assertEquals(code1.hashCode(), code2.hashCode());
    }


    @Test
    @DisplayName("hashCode - Different values produce different hash codes")
    void testHashCodeDifferentValue() throws IncorrectParametersException {
        ePrescripCode code1 = new ePrescripCode("EP00000000000123"); // 16 chars
        ePrescripCode code2 = new ePrescripCode("EP00000000000987"); // 16 chars

        assertNotEquals(code1.hashCode(), code2.hashCode());
    }


    // ------------ TOSTRING -------------------
    @Test
    @DisplayName("toString - Contains class name and prescription code")
    void testToString() throws IncorrectParametersException {
        ePrescripCode prescripCode = new ePrescripCode("EP20250001234567"); // 16 chars

        String text = prescripCode.toString();

        assertTrue(text.contains("ePrescripCode"));
        assertTrue(text.contains("EP20250001234567"));
    }


    // ----------- EXCEPTION HANDLING ----------------
    @Test
    @DisplayName("Constructor - Throws exception when code is null")
    void testConstructorNullThrowsException() {
        // Assert
        assertThrows(IncorrectParametersException.class, () -> {
            new ePrescripCode(null);
        });
    }

    @Test
    @DisplayName("Constructor - Throws exception when code is empty")
    void testConstructorEmptyThrowsException() {
        // Assert
        assertThrows(IncorrectParametersException.class, () -> {
            new ePrescripCode("");
        });
    }


    @Test
    @DisplayName("Constructor - Throws exception when code has wrong format")
    void testConstructorWrongFormatThrowsException() {
        // Assert - Too short (5 characters)
        assertThrows(IncorrectParametersException.class, () -> {
            new ePrescripCode("12345");
        });
    }

    @Test
    @DisplayName("Constructor - Throws exception when code has hyphens")
    void testConstructorWithHyphensThrowsException() {
        // Assert - Contains hyphens (invalid characters)
        assertThrows(IncorrectParametersException.class, () -> {
            new ePrescripCode("EP-2025-00012345"); // 16 chars but has hyphens
        });
    }

    @Test
    @DisplayName("Constructor - Throws exception when code is too short")
    void testConstructorTooShortThrowsException() {
        // Assert - Only 15 characters (need exactly 16)
        assertThrows(IncorrectParametersException.class, () -> {
            new ePrescripCode("EP0000000000012"); // 15 chars - too short
        });
    }

    @Test
    @DisplayName("Constructor - Throws exception when code is too long")
    void testConstructorTooLongThrowsException() {
        // Assert - 17 characters (need exactly 16)
        assertThrows(IncorrectParametersException.class, () -> {
            new ePrescripCode("EP000000000001234"); // 17 chars - too long
        });
    }

    @Test
    @DisplayName("Constructor - Accepts valid 16-character alphanumeric code")
    void testConstructorValidFormats() throws IncorrectParametersException {
        // Test various valid 16-character formats
        assertDoesNotThrow(() -> {
            new ePrescripCode("EP00000000000001"); // All digits after EP
        });

        assertDoesNotThrow(() -> {
            new ePrescripCode("ABCD123456789012"); // Mix of letters and numbers
        });

        assertDoesNotThrow(() -> {
            new ePrescripCode("1234567890ABCDEF"); // Numbers and letters
        });
    }
}