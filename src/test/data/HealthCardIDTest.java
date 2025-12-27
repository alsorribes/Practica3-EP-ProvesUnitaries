package test.data;

import data.HealthCardID;

import exceptions.HealthCardIDException;
import exceptions.IncorrectParametersException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for HealthCardID value object.
 */

@DisplayName("HealthCardID - Unit Tests")

public class HealthCardIDTest {
    // ----------- CONSTRUCTOR AND GETTERS --------------
    @Test
    @DisplayName("Constructor - Stores personal ID correctly")
    void testContructorStoresPersonalID() throws IncorrectParametersException {
        // Arrange
        String code = "abcdeFGHIJ123456";

        // Act
        HealthCardID id = new HealthCardID(code);

        // Assert
        assertEquals(code, id.getPersonalID());
    }


    // ------------ EQUALS CONTRACTS -----------
    @Test
    @DisplayName("equals - Same object returns true")
    void testEqualsSameObject() throws IncorrectParametersException {
        HealthCardID id = new HealthCardID("abcdeFGHIJ123456");

        assertEquals(id, id);
    }

    @Test
    @DisplayName("equals - Different object with same value are equal")
    void testEqualsSameValue() throws IncorrectParametersException {
        HealthCardID id1 = new HealthCardID("abcdeFGHIJ123456");
        HealthCardID id2 = new HealthCardID("abcdeFGHIJ123456");

        assertEquals(id1, id2);
    }


    @Test
    @DisplayName("equals - Different values are not equal")
    void testEqualsDifferentValue() throws IncorrectParametersException {
        HealthCardID id1 = new HealthCardID("abcdeFGHIJ123456");
        HealthCardID id2 = new HealthCardID("654321JIHGFedcba");

        assertNotEquals(id1, id2);
    }


    @Test
    @DisplayName("equals - Compared with null returns false")
    void testEqualsNull() throws IncorrectParametersException {
        HealthCardID id = new HealthCardID("abcdeFGHIJ123456");

        assertNotEquals(id, null);
    }


    @Test
    @DisplayName("equals - Compared with different class returns false")
    void testEqualsDifferentClass() throws IncorrectParametersException {
        HealthCardID id = new HealthCardID("abcdeFGHIJ123456");
        String other = "abcdeFGHIJ123456";

        assertNotEquals(id, other);
    }


    // ------------- HASHCODE CONTRACT --------------------
    @Test
    @DisplayName("hashCode - Same value produces same hash code")
    void testHashCodeSameValue() throws IncorrectParametersException {
        HealthCardID id1 = new HealthCardID("abcdeFGHIJ123456");
        HealthCardID id2 = new HealthCardID("abcdeFGHIJ123456");

        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    @DisplayName("hashCode - Different values produce different hash codes")
    void testHashCodeDifferentValue() throws IncorrectParametersException {
        HealthCardID id1 = new HealthCardID("abcdeFGHIJ123456");
        HealthCardID id2 = new HealthCardID("654321JIHGFedcba");

        assertNotEquals(id1.hashCode(), id2.hashCode());
    }


    // --------- TOSTRING ----------
    @Test
    @DisplayName("toString - Contains class name and personal ID")
    void testToString() throws IncorrectParametersException {
        HealthCardID id = new HealthCardID("abcdeFGHIJ123456");

        String text = id.toString();

        assertTrue(text.contains("HealthCardID"));
        assertTrue(text.contains("abcdeFGHIJ123456"));
    }


    // ----------- EXCEPTION HANDLING ----------------
    @Test
    @DisplayName("Constructor - Throws exception when code is null")
    void testConstructorNullThrowsException() {
        // Assert
        assertThrows(IncorrectParametersException.class, () -> {
            new HealthCardID(null);
        });
    }

    @Test
    @DisplayName("Constructor - Throws exception when code is empty")
    void testConstructorEmptyThrowsException() {
        // Assert
        assertThrows(IncorrectParametersException.class, () -> {
            new HealthCardID("");
        });
    }


    @Test
    @DisplayName("Constructor - Throws exception when code is empty")
    void testConstructorWrongFormatThrowsException() {
        // Assert
        assertThrows(IncorrectParametersException.class, () -> {
            new HealthCardID("12345");
        });
    }
}
