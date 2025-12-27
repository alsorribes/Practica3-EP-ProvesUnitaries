package test.data;

import data.ePrescripCode;

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
    void testConstructorStoresCode() {
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
    void testEqualsSameObject() {
        ePrescripCode code = new ePrescripCode("EP-123");

        assertEquals(code, code);
    }


    @Test
    @DisplayName("equals - Different objects with same value are equal")
    void testEqualsSameValue() {
        ePrescripCode code1 = new ePrescripCode("EP-123");
        ePrescripCode code2 = new ePrescripCode("EP-123");

        assertEquals(code1, code2);
        assertEquals(code2, code1);
    }

}
