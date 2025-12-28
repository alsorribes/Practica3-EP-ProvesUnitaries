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
    @DisplayName("Constructor - Stores product code correctly with 12 digits")
    void testConstructorStoresCode12Digits() throws IncorrectParametersException {
        // Arrange
        String code = "123456789012"; // 12 digits - UPC format

        // Act
        ProductID productID = new ProductID(code);

        // Assert
        assertEquals(code, productID.getCode());
    }

    @Test
    @DisplayName("Constructor - Stores product code correctly with 13 digits")
    void testConstructorStoresCode13Digits() throws IncorrectParametersException {
        // Arrange
        String code = "1234567890123"; // 13 digits - EAN format

        // Act
        ProductID productID = new ProductID(code);

        // Assert
        assertEquals(code, productID.getCode());
    }

    @Test
    @DisplayName("Constructor - Stores product code correctly with 16 alphanumeric")
    void testConstructorStoresCode16AlphaNum() throws IncorrectParametersException {
        // Arrange
        String code = "abcdeFGHIJ123456"; // 16 alphanumeric

        // Act
        ProductID productID = new ProductID(code);

        // Assert
        assertEquals(code, productID.getCode());
    }


    // ---------- EQUALS CONTRACT ------------
    @Test
    @DisplayName("equals - Same object returns true")
    void testEqualsSameObject() throws IncorrectParametersException {
        ProductID productID = new ProductID("123456789012");

        assertEquals(productID, productID);
    }


    @Test
    @DisplayName("equals - Different objects with same value are equal")
    void testEqualsSameValue() throws IncorrectParametersException {
        ProductID id1 = new ProductID("123456789012");
        ProductID id2 = new ProductID("123456789012");

        assertEquals(id1, id2);
    }


    @Test
    @DisplayName("equals - Different values are not equal")
    void testEqualsDifferentValue() throws IncorrectParametersException {
        ProductID id1 = new ProductID("123456789012");
        ProductID id2 = new ProductID("987654321098");

        assertNotEquals(id1, id2);
    }


    @Test
    @DisplayName("equals - Compared with null returns false")
    void testEqualsNull() throws IncorrectParametersException {
        ProductID productID = new ProductID("123456789012");

        assertNotEquals(productID, null);
    }


    @Test
    @DisplayName("equals - Compared with different class returns false")
    void testEqualsDifferentClass() throws IncorrectParametersException {
        ProductID productID = new ProductID("123456789012");
        String other = "123456789012";

        assertNotEquals(productID, other);
    }


    // -------------- HASHCODE CONTRACT ---------------
    @Test
    @DisplayName("hashCode - Same value produces same hash code")
    void testHashCodeSameValue() throws IncorrectParametersException {
        ProductID id1 = new ProductID("123456789012");
        ProductID id2 = new ProductID("123456789012");

        assertEquals(id1.hashCode(), id2.hashCode());
    }


    @Test
    @DisplayName("hashCode - Different values produce different hash codes")
    void testHashCodeDifferentValue() throws IncorrectParametersException {
        ProductID id1 = new ProductID("123456789012");
        ProductID id2 = new ProductID("987654321098");

        assertNotEquals(id1.hashCode(), id2.hashCode());
    }


    // --------- TOSTRING ------------
    @Test
    @DisplayName("toString - Contains class name and product code")
    void testToString() throws IncorrectParametersException {
        ProductID productID = new ProductID("123456789012");

        String text = productID.toString();

        assertTrue(text.contains("ProductID"));
        assertTrue(text.contains("123456789012"));
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
    @DisplayName("Constructor - Throws exception when code is too short (11 chars)")
    void testConstructorTooShortThrowsException() {
        // Assert
        assertThrows(IncorrectParametersException.class, () -> {
            new ProductID("12345678901"); // 11 chars - too short
        });
    }

    @Test
    @DisplayName("Constructor - Throws exception when code is too long (17 chars)")
    void testConstructorTooLongThrowsException() {
        // Assert
        assertThrows(IncorrectParametersException.class, () -> {
            new ProductID("12345678901234567"); // 17 chars - too long
        });
    }

    @Test
    @DisplayName("Constructor - Throws exception when code contains special characters")
    void testConstructorSpecialCharsThrowsException() {
        // Assert
        assertThrows(IncorrectParametersException.class, () -> {
            new ProductID("123456-78901"); // Contains hyphen
        });
    }

    @Test
    @DisplayName("Constructor - Throws exception when code contains spaces")
    void testConstructorSpacesThrowsException() {
        // Assert
        assertThrows(IncorrectParametersException.class, () -> {
            new ProductID("1234 5678 9012"); // Contains spaces
        });
    }
}