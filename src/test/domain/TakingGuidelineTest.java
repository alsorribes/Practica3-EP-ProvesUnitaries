package test.domain;

import medicalconsultation.TakingGuideline;
import medicalconsultation.Posology;
import medicalconsultation.FqUnit;
import medicalconsultation.dayMoment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TakingGuideline class.
 */
@DisplayName("TakingGuideline - Unit Tests")
public class TakingGuidelineTest {
    // ------------- CONSTRUCTOR AND GETTERS ---------------
    @Test
    @DisplayName("Constructor initializes all fields correctly")
    void testConstructorAndGetters() {
        dayMoment moment = dayMoment.BEFOREBREAKFAST;
        float duration = 7.0f;
        float dose = 500.0f;
        float freq = 3.0f;
        FqUnit unit = FqUnit.DAY;
        String instructions = "Take after meals";

        TakingGuideline guideline = new TakingGuideline(
                moment, duration, dose, freq, unit, instructions
        );

        assertEquals(moment, guideline.getdMoment());
        assertEquals(duration, guideline.getDuration());
        assertEquals(instructions, guideline.getInstructions());

        Posology posology = guideline.getPosology();
        assertNotNull(posology);
        assertEquals(dose, posology.getDose());
        assertEquals(freq, posology.getFreq());
        assertEquals(unit, posology.getFreqUnit());
    }

}
