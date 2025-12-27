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
}
