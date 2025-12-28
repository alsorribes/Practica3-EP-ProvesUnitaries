package test.domain;

import data.HealthCardID;
import exceptions.IncorrectParametersException;
import medicalconsultation.MedicalHistory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MedicalHistory - Unit Tests")
public class MedicalHistoryTest {

    @Test
    @DisplayName("Constructor - Valid medical history construction")
    public void testValidMedicalHistoryConstruction() throws IncorrectParametersException {
        HealthCardID cip = new HealthCardID("1234567890ABCDEF");
        MedicalHistory medicalHistory = new MedicalHistory(cip, 12345);

        assertEquals(cip, medicalHistory.getCip());
        assertEquals(12345, medicalHistory.getMembShipNumb());
        assertEquals("", medicalHistory.getHistory());
    }

    @Test
    @DisplayName("Constructor - Throws exception with null CIP")
    public void testConstructorWithNullCIPThrowsException() {
        assertThrows(IncorrectParametersException.class, () -> {
            new MedicalHistory(null, 12345);
        });
    }

    @Test
    @DisplayName("Constructor - Throws exception with negative membership number")
    public void testConstructorWithNegativeMemberShipNumThrowsException() throws IncorrectParametersException {
        HealthCardID cip = new HealthCardID("1234567890ABCDEF");
        assertThrows(IncorrectParametersException.class, () -> {
            new MedicalHistory(cip, -1);
        });
    }

    @Test
    @DisplayName("Constructor - Accepts zero membership number")
    public void testConstructorWithZeroMemberShipNum() throws IncorrectParametersException {
        HealthCardID cip = new HealthCardID("1234567890ABCDEF");
        MedicalHistory medicalHistory = new MedicalHistory(cip, 0);

        assertEquals(0, medicalHistory.getMembShipNumb());
    }

    @Test
    @DisplayName("addMedicalHistoryAnnotations - Adds single annotation")
    public void testAddMedicalHistoryAnnotations() throws IncorrectParametersException {
        HealthCardID cip = new HealthCardID("1234567890ABCDEF");
        MedicalHistory medicalHistory = new MedicalHistory(cip, 12345);

        medicalHistory.addMedicalHistoryAnnotations("Patient has high blood pressure");

        assertTrue(medicalHistory.getHistory().contains("Patient has high blood pressure"));
        assertTrue(medicalHistory.getHistory().endsWith("\n"));
    }

    @Test
    @DisplayName("addMedicalHistoryAnnotations - Adds multiple annotations")
    public void testAddMultipleAnnotations() throws IncorrectParametersException {
        HealthCardID cip = new HealthCardID("1234567890ABCDEF");
        MedicalHistory medicalHistory = new MedicalHistory(cip, 12345);

        medicalHistory.addMedicalHistoryAnnotations("Patient has high blood pressure");
        medicalHistory.addMedicalHistoryAnnotations("Prescribed medication X");
        medicalHistory.addMedicalHistoryAnnotations("Follow-up in 2 weeks");

        String history = medicalHistory.getHistory();
        assertTrue(history.contains("Patient has high blood pressure"));
        assertTrue(history.contains("Prescribed medication X"));
        assertTrue(history.contains("Follow-up in 2 weeks"));

        // Verify order is preserved
        int idx1 = history.indexOf("Patient has high blood pressure");
        int idx2 = history.indexOf("Prescribed medication X");
        int idx3 = history.indexOf("Follow-up in 2 weeks");
        assertTrue(idx1 < idx2 && idx2 < idx3);
    }

    @Test
    @DisplayName("addMedicalHistoryAnnotations - Throws exception for null annotation")
    public void testAddNullAnnotationThrowsException() throws IncorrectParametersException {
        HealthCardID cip = new HealthCardID("1234567890ABCDEF");
        MedicalHistory medicalHistory = new MedicalHistory(cip, 12345);

        assertThrows(IllegalArgumentException.class, () -> {
            medicalHistory.addMedicalHistoryAnnotations(null);
        });
    }

    @Test
    @DisplayName("addMedicalHistoryAnnotations - Throws exception for empty annotation")
    public void testAddEmptyAnnotationThrowsException() throws IncorrectParametersException {
        HealthCardID cip = new HealthCardID("1234567890ABCDEF");
        MedicalHistory medicalHistory = new MedicalHistory(cip, 12345);

        assertThrows(IllegalArgumentException.class, () -> {
            medicalHistory.addMedicalHistoryAnnotations("   ");
        });
    }

    @Test
    @DisplayName("setNewDoctor - Updates membership number")
    public void testSetNewDoctor() throws IncorrectParametersException {
        HealthCardID cip = new HealthCardID("1234567890ABCDEF");
        MedicalHistory medicalHistory = new MedicalHistory(cip, 12345);

        assertEquals(12345, medicalHistory.getMembShipNumb());

        medicalHistory.setNewDoctor(67890);

        assertEquals(67890, medicalHistory.getMembShipNumb());
    }

    @Test
    @DisplayName("setNewDoctor - Throws exception for negative membership number")
    public void testSetNewDoctorNegativeThrowsException() throws IncorrectParametersException {
        HealthCardID cip = new HealthCardID("1234567890ABCDEF");
        MedicalHistory medicalHistory = new MedicalHistory(cip, 12345);

        assertThrows(IllegalArgumentException.class, () -> {
            medicalHistory.setNewDoctor(-1);
        });
    }

    @Test
    @DisplayName("setNewDoctor - Accepts zero membership number")
    public void testSetNewDoctorZero() throws IncorrectParametersException {
        HealthCardID cip = new HealthCardID("1234567890ABCDEF");
        MedicalHistory medicalHistory = new MedicalHistory(cip, 12345);

        medicalHistory.setNewDoctor(0);

        assertEquals(0, medicalHistory.getMembShipNumb());
    }

    @Test
    @DisplayName("getHistory - Returns empty string initially")
    public void testGetHistoryInitiallyEmpty() throws IncorrectParametersException {
        HealthCardID cip = new HealthCardID("1234567890ABCDEF");
        MedicalHistory medicalHistory = new MedicalHistory(cip, 12345);

        assertEquals("", medicalHistory.getHistory());
    }

    @Test
    @DisplayName("getCip - Returns correct HealthCardID")
    public void testGetCip() throws IncorrectParametersException {
        HealthCardID cip = new HealthCardID("1234567890ABCDEF");
        MedicalHistory medicalHistory = new MedicalHistory(cip, 12345);

        assertEquals(cip, medicalHistory.getCip());
        assertSame(cip, medicalHistory.getCip()); // Should return same reference
    }

    @Test
    @DisplayName("Complex workflow - Multiple doctors and annotations")
    public void testComplexWorkflow() throws IncorrectParametersException {
        HealthCardID cip = new HealthCardID("1234567890ABCDEF");
        MedicalHistory medicalHistory = new MedicalHistory(cip, 11111);

        // First doctor adds annotations
        medicalHistory.addMedicalHistoryAnnotations("Initial consultation");
        medicalHistory.addMedicalHistoryAnnotations("Prescribed aspirin");

        // Change doctor
        medicalHistory.setNewDoctor(22222);
        medicalHistory.addMedicalHistoryAnnotations("Follow-up visit");

        // Verify state
        assertEquals(22222, medicalHistory.getMembShipNumb());
        String history = medicalHistory.getHistory();
        assertTrue(history.contains("Initial consultation"));
        assertTrue(history.contains("Prescribed aspirin"));
        assertTrue(history.contains("Follow-up visit"));
    }
}