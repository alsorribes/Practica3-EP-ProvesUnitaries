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

}
