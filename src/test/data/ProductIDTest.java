package test.data;

import data.ProductID;

import exceptions.IncorrectParametersException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ProductID value object.
 */
@DisplayName("ProductID - Unit Tests")
public class ProductIDTest {
    // --------- CONSTRUCTOR AND GETTER
    @Test
    @DisplayName("Constructor - Stores product code correctly")
    void testConstructorStoresCode() throws IncorrectParametersException {
        // Arrange
        String code = "012345678901";

        // Act
        ProductID productID = new ProductID(code);

        // Assert
        assertEquals(code, productID.getCode());
    }


    // ---------- EQUALS CONTRACT ------------
    @Test
    @DisplayName("equals - Same object returns true")
    void testEqualsSameObject() throws IncorrectParametersException {
        ProductID productID = new ProductID("ABC123");

        assertEquals(productID, productID);
    }


    @Test
    @DisplayName("equals - Different objects with same value are equal")
    void testEqualsSameValue() throws IncorrectParametersException {
        ProductID id1 = new ProductID("ABC123");
        ProductID id2 = new ProductID("ABC123");

        assertEquals(id1, id2);
    }


    @Test
    @DisplayName("equals - Different values are not equal")
    void testEqualsDifferentValue() throws IncorrectParametersException {
        ProductID id1 = new ProductID("ABC123");
        ProductID id2 = new ProductID("ZYX987");

        assertNotEquals(id1, id2);
    }


    @Test
    @DisplayName("equals - Compared with null returns false")
    void testEqualsNull() throws IncorrectParametersException {
        ProductID productID = new ProductID("ABC123");

        assertNotEquals(productID, null);
    }


    @Test
    @DisplayName("equals - Compared with different class returns false")
    void testEqualsDifferentClass() throws IncorrectParametersException {
        ProductID productID = new ProductID("ABC123");
        String other = "ABC123";

        assertNotEquals(productID, other);
    }


    // -------------- HASHCODE CONTRACT ---------------
    @Test
    @DisplayName("hashCode - Same value produces same hash code")
    void testHashCodeSameValue() throws IncorrectParametersException {
        ProductID id1 = new ProductID("ABC123");
        ProductID id2 = new ProductID("ABC123");

        assertEquals(id1.hashCode(), id2.hashCode());
    }


    @Test
    @DisplayName("hashCode - Different values produce different hash codes")
    void testHashCodeDifferentValue() throws IncorrectParametersException {
        ProductID id1 = new ProductID("ABC123");
        ProductID id2 = new ProductID("ZYX987");

        assertNotEquals(id1.hashCode(), id2.hashCode());
    }


    // --------- TOSTRING ------------
    @Test
    @DisplayName("toString - Contains class name and product code")
    void testToString() throws IncorrectParametersException {
        ProductID productID = new ProductID("012345678901");

        String text = productID.toString();

        assertTrue(text.contains("ProductID"));
        assertTrue(text.contains("012345678901"));
    }


    // ----------- EXCEPTION HANDLING ----------------
    @Test
    @DisplayName("Constructor - Throws exception when code is null")
    void testConstructorNullThrowsException() {
        // Assert
        assertThrows(IncorrectParametersException.class, () -> {
            new ProductID(null);
        });
    }

    @Test
    @DisplayName("Constructor - Throws exception when code is empty")
    void testConstructorEmptyThrowsException() {
        // Assert
        assertThrows(IncorrectParametersException.class, () -> {
            new ProductID("");
        });
    }


    @Test
    @DisplayName("Constructor - Throws exception when code is empty")
    void testConstructorWrongFormatThrowsException() {
        // Assert
        assertThrows(IncorrectParametersException.class, () -> {
            new ProductID("12345");
        });
    }
}
