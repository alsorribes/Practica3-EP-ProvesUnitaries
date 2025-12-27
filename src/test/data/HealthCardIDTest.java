package test.data;

import data.HealthCardID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for HealthCardID value object.
 */

@DisplayName("HealthCardID - Unit Tests")

public class HealthCardIDTest {
    // ----------- CONSTRUCTOR AND GETTERS --------------
    @Test
    @DisplayName("Constructor - Stores personal ID correctly")
    void testContructorStoresPersonalID(){
        // Arrange
        String code = "1234567890123456";

        // Act
        HealthCardID id = new HealthCardID(code);

        // Assert
        assertEquals(code, id.getPersonalID());
    }
}
