package test.data;

import data.ProductID;

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
    void testConstructorStoresCode() {
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
    void testEqualsSameObject() {
        ProductID productID = new ProductID("ABC123");

        assertEquals(productID, productID);
    }


    @Test
    @DisplayName("equals - Different objects with same value are equal")
    void testEqualsSameValue() {
        ProductID id1 = new ProductID("ABC123");
        ProductID id2 = new ProductID("ABC123");

        assertEquals(id1, id2);
    }
}
