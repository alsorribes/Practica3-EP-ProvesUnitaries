package test.data;

import data.HealthCardID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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


    // ------------ EQUALS CONTRACTS -----------
    @Test
    @DisplayName("equals - Same object returns true")
    void testEqualsSameObject(){
        HealthCardID id = new HealthCardID("ABC123");

        assertEquals(id, id);
    }

    @Test
    @DisplayName("equals - Different object with same value are equal")
    void testEqualsSameValue(){
        HealthCardID id1 = new HealthCardID("ABC123");
        HealthCardID id2 = new HealthCardID("ABC123");

        assertEquals(id1, id2);
    }


    @Test
    @DisplayName("equals - Different values are not equal")
    void testEqualsDifferentValue(){
        HealthCardID id1 = new HealthCardID("ABC123");
        HealthCardID id2 = new HealthCardID("ZYX987");

        assertNotEquals(id1, id2);
    }


    @Test
    @DisplayName("equals - Compared with null returns false")
    void testEqualsNull(){
        HealthCardID id = new HealthCardID("ABC123");

        assertNotEquals(id, null);
    }
}
