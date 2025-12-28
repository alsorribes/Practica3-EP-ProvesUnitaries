package test.domain;

import medicalconsultation.Suggestion;
import medicalconsultation.Suggestion.Action;
import data.ProductID;
import exceptions.IncorrectParametersException;

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
    void testConstructorWithGuidelines() throws IncorrectParametersException {
        Action action = Action.INSERT;
        ProductID productID = new ProductID("MED001PRODUCT12"); // 15 chars - valid
        String[] guidelines = {"Take after meals", "Do not exceed dose"};

        Suggestion suggestion = new Suggestion(action, productID, guidelines);

        assertEquals(action, suggestion.getAction());
        assertEquals(productID, suggestion.getProductID());
        assertArrayEquals(guidelines, suggestion.getGuidelines());
    }


    @Test
    @DisplayName("Constructor without guidelines sets guidelines to null")
    void testConstructorWithoutGuidelines() throws IncorrectParametersException {
        Action action = Action.ELIMINATE;
        ProductID productID = new ProductID("MED002PRODUCT12"); // 15 chars - valid

        Suggestion suggestion = new Suggestion(action, productID);

        assertEquals(action, suggestion.getAction());
        assertEquals(productID, suggestion.getProductID());
        assertNull(suggestion.getGuidelines());
    }


    // ---------------- GETTERS ---------------------
    @Test
    @DisplayName("getGuidelines returns the same array reference")
    void testGetGuidelines() throws IncorrectParametersException {
        String[] guidelines = {"Guideline 1"};
        Suggestion suggestion = new Suggestion(
                Action.MODIFY,
                new ProductID("MED003PRODUCT12"), // 15 chars - valid
                guidelines
        );

        assertSame(guidelines, suggestion.getGuidelines());
    }


    // ----------- TOSTRING ---------------
    @Test
    @DisplayName("toString contains action, productID and guidelines")
    void testToStringWithGuidelines() throws IncorrectParametersException {
        ProductID productID = new ProductID("MED004PRODUCT12"); // 15 chars - valid
        String[] guidelines = {"Morning", "Evening"};

        Suggestion suggestion = new Suggestion(
                Action.MODIFY,
                productID,
                guidelines
        );

        String text = suggestion.toString();

        assertTrue(text.contains("Suggestion"));
        assertTrue(text.contains("MODIFY"));
        assertTrue(text.contains("MED004PRODUCT12"));
        assertTrue(text.contains("Morning"));
        assertTrue(text.contains("Evening"));
    }

    @Test
    @DisplayName("toString shows 'none' when guidelines are null")
    void testToStringWithoutGuidelines() throws IncorrectParametersException {
        ProductID productID = new ProductID("MED005PRODUCT12"); // 15 chars - valid

        Suggestion suggestion = new Suggestion(
                Action.ELIMINATE,
                productID
        );

        String text = suggestion.toString();

        assertTrue(text.contains("Suggestion"));
        assertTrue(text.contains("ELIMINATE"));
        assertTrue(text.contains("MED005PRODUCT12"));
        assertTrue(text.contains("none"));
    }
}