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


    // --------------- SETTERS ----------------
    @Test
    @DisplayName("setdMoment updates day moment")
    void testSetDayMoment() {
        TakingGuideline guideline = new TakingGuideline(
                dayMoment.BEFOREBREAKFAST, 5.0f, 250.0f, 2.0f, FqUnit.DAY, "Initial"
        );

        guideline.setdMoment(dayMoment.AFTERDINNER);

        assertEquals(dayMoment.AFTERDINNER, guideline.getdMoment());
    }

    @Test
    @DisplayName("setDuration updates duration")
    void testSetDuration() {
        TakingGuideline guideline = new TakingGuideline(
                dayMoment.BEFOREBREAKFAST, 5.0f, 250.0f, 2.0f, FqUnit.DAY, "Initial"
        );

        guideline.setDuration(10.0f);

        assertEquals(10.0f, guideline.getDuration());
    }

    @Test
    @DisplayName("setPosology updates posology")
    void testSetPosology() {
        TakingGuideline guideline = new TakingGuideline(
                dayMoment.BEFOREBREAKFAST, 5.0f, 250.0f, 2.0f, FqUnit.DAY, "Initial"
        );

        Posology newPosology = new Posology(100.0f, 1.0f, FqUnit.HOUR);

        guideline.setPosology(newPosology);

        assertEquals(newPosology, guideline.getPosology());
    }

    @Test
    @DisplayName("setInstructions updates instructions")
    void testSetInstructions() {
        TakingGuideline guideline = new TakingGuideline(
                dayMoment.BEFOREBREAKFAST, 5.0f, 250.0f, 2.0f, FqUnit.DAY, "Initial"
        );

        guideline.setInstructions("Take before sleeping");

        assertEquals("Take before sleeping", guideline.getInstructions());
    }


    // --------------- MUTABILITY CHECK --------------------
    @Test
    @DisplayName("Object reflects all changes after multiple setters")
    void testMultipleSetters() {
        TakingGuideline guideline = new TakingGuideline(
                dayMoment.BEFOREBREAKFAST, 5.0f, 250.0f, 2.0f, FqUnit.DAY, "Initial"
        );

        Posology posology = new Posology(500.0f, 3.0f, FqUnit.WEEK);

        guideline.setdMoment(dayMoment.AFTERLUNCH);
        guideline.setDuration(14.0f);
        guideline.setPosology(posology);
        guideline.setInstructions("Updated instructions");

        assertEquals(dayMoment.AFTERLUNCH, guideline.getdMoment());
        assertEquals(14.0f, guideline.getDuration());
        assertEquals(posology, guideline.getPosology());
        assertEquals("Updated instructions", guideline.getInstructions());
    }
}
