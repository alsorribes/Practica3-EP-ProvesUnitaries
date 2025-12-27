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
        // Arrange
        String code = "EP-2025-000123";

        // Act
        ePrescripCode prescripCode = new ePrescripCode(code);

        // Assert
        assertEquals(code, prescripCode.getCode());
    }


    // ------------ EQUALS CONTRACT -------------
    @Test
    @DisplayName("equals - Same object returns true")
    void testEqualsSameObject() throws IncorrectParametersException {
        ePrescripCode code = new ePrescripCode("EP-123");

        assertEquals(code, code);
    }


    @Test
    @DisplayName("equals - Different objects with same value are equal")
    void testEqualsSameValue() throws IncorrectParametersException {
        ePrescripCode code1 = new ePrescripCode("EP-123");
        ePrescripCode code2 = new ePrescripCode("EP-123");

        assertEquals(code1, code2);
    }


    @Test
    @DisplayName("equals - Different values are not equal")
    void testEqualsDifferentValue() throws IncorrectParametersException {
        ePrescripCode code1 = new ePrescripCode("EP-123");
        ePrescripCode code2 = new ePrescripCode("EP-987");

        assertNotEquals(code1, code2);
    }


    @Test
    @DisplayName("equals - Compared with null returns false")
    void testEqualsNull() throws IncorrectParametersException {
        ePrescripCode code = new ePrescripCode("EP-123");

        assertNotEquals(code, null);
    }


    @Test
    @DisplayName("equals - Compared with different class returns false")
    void testEqualsDifferentClass() throws IncorrectParametersException {
        ePrescripCode code = new ePrescripCode("EP-123");
        String other = "EP-123";

        assertNotEquals(code, other);
    }


    // --------------- HASHCODE CONTRACT --------------
    @Test
    @DisplayName("hashCode - Same value produces same hash code")
    void testHashCodeSameValue() throws IncorrectParametersException {
        ePrescripCode code1 = new ePrescripCode("EP-123");
        ePrescripCode code2 = new ePrescripCode("EP-123");

        assertEquals(code1.hashCode(), code2.hashCode());
    }


    @Test
    @DisplayName("hashCode - Different values produce different hash codes")
    void testHashCodeDifferentValue() throws IncorrectParametersException {
        ePrescripCode code1 = new ePrescripCode("EP-123");
        ePrescripCode code2 = new ePrescripCode("EP-987");

        assertNotEquals(code1.hashCode(), code2.hashCode());
    }


    // ------------ TOSTRING -------------------
    @Test
    @DisplayName("toString - Contains class name and prescription code")
    void testToString() throws IncorrectParametersException {
        ePrescripCode prescripCode = new ePrescripCode("EP-2025-000123");

        String text = prescripCode.toString();

        assertTrue(text.contains("ePrescripCode"));
        assertTrue(text.contains("EP-2025-000123"));
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
    @DisplayName("Constructor - Throws exception when code is empty")
    void testConstructorWrongFormatThrowsException() {
        // Assert
        assertThrows(IncorrectParametersException.class, () -> {
            new ePrescripCode("12345");
        });
    }

}
