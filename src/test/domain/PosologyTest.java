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


    // ----------- SETTERS ---------------
    @Test
    @DisplayName("setDose updates the dose value")
    void testSetDose() {
        Posology posology = new Posology(250.0f, 2.0f, FqUnit.DAY);

        posology.setDose(500.0f);

        assertEquals(500.0f, posology.getDose());
    }

    @Test
    @DisplayName("setFreq updates the frequency value")
    void testSetFreq() {
        Posology posology = new Posology(250.0f, 2.0f, FqUnit.DAY);

        posology.setFreq(4.0f);

        assertEquals(4.0f, posology.getFreq());
    }

    @Test
    @DisplayName("setFreqUnit updates the frequency unit")
    void testSetFreqUnit() {
        Posology posology = new Posology(250.0f, 2.0f, FqUnit.DAY);

        posology.setFreqUnit(FqUnit.HOUR);

        assertEquals(FqUnit.HOUR, posology.getFreqUnit());
    }
}
