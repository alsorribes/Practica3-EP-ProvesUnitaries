package test.domain;

import medicalconsultation.Posology;
import medicalconsultation.FqUnit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Posology class.
 */
@DisplayName("Posology - Unit Tests")
public class PosologyTest {
    // ------------- CONSTRUCTOR AND GETTERS --------------
    @Test
    @DisplayName("Constructor initializes all fields correctly")
    void testConstructorAndGetters() {
        float dose = 500.0f;
        float freq = 3.0f;
        FqUnit unit = FqUnit.DAY;

        Posology posology = new Posology(dose, freq, unit);

        assertEquals(dose, posology.getDose());
        assertEquals(freq, posology.getFreq());
        assertEquals(unit, posology.getFreqUnit());
    }
}
