package test.domain;

import medicalconsultation.Suggestion;
import medicalconsultation.Suggestion.Action;
import data.ProductID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Suggestion class.
 */
@DisplayName("Suggestion - Unit Tests")
public class SuggestionTest {
    // --------------- CONSTRUCTOR AND GETTERS ---------------------
    @Test
    @DisplayName("Constructor with guidelines initializes all fields correctly")
    void testConstructorWithGuidelines() {
        Action action = Action.INSERT;
        ProductID productID = new ProductID("MED-001");
        String[] guidelines = {"Take after meals", "Do not exceed dose"};

        Suggestion suggestion = new Suggestion(action, productID, guidelines);

        assertEquals(action, suggestion.getAction());
        assertEquals(productID, suggestion.getProductID());
        assertArrayEquals(guidelines, suggestion.getGuidelines());
    }


    @Test
    @DisplayName("Constructor without guidelines sets guidelines to null")
    void testConstructorWithoutGuidelines() {
        Action action = Action.ELIMINATE;
        ProductID productID = new ProductID("MED-002");

        Suggestion suggestion = new Suggestion(action, productID);

        assertEquals(action, suggestion.getAction());
        assertEquals(productID, suggestion.getProductID());
        assertNull(suggestion.getGuidelines());
    }


    // ---------------- GETTERS ---------------------
    @Test
    @DisplayName("getGuidelines returns the same array reference")
    void testGetGuidelines() {
        String[] guidelines = {"Guideline 1"};
        Suggestion suggestion = new Suggestion(
                Action.MODIFY,
                new ProductID("MED-003"),
                guidelines
        );

        assertSame(guidelines, suggestion.getGuidelines());
    }


    // ----------- TOSTRING ---------------
    @Test
    @DisplayName("toString contains action, productID and guidelines")
    void testToStringWithGuidelines() {
        ProductID productID = new ProductID("MED-004");
        String[] guidelines = {"Morning", "Evening"};

        Suggestion suggestion = new Suggestion(
                Action.MODIFY,
                productID,
                guidelines
        );

        String text = suggestion.toString();

        assertTrue(text.contains("Suggestion"));
        assertTrue(text.contains("MODIFY"));
        assertTrue(text.contains("MED-004"));
        assertTrue(text.contains("Morning"));
        assertTrue(text.contains("Evening"));
    }

    @Test
    @DisplayName("toString shows 'none' when guidelines are null")
    void testToStringWithoutGuidelines() {
        ProductID productID = new ProductID("MED-005");

        Suggestion suggestion = new Suggestion(
                Action.ELIMINATE,
                productID
        );

        String text = suggestion.toString();

        assertTrue(text.contains("Suggestion"));
        assertTrue(text.contains("ELIMINATE"));
        assertTrue(text.contains("MED-005"));
        assertTrue(text.contains("none"));
    }
}
