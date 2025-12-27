package domain;

import data.HealthCardID;
import exceptions.HealthCardIDException;
import exceptions.IncorrectParametersException;
import medicalconsultation.MedicalHistory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MedicalHistoryTest {

    @Test
    public void testValidMedicalHistoryConstruction() throws HealthCardIDException, IncorrectParametersException {
        HealthCardID cip = new HealthCardID("1234567890ABCDEF");
        MedicalHistory medicalHistory = new MedicalHistory(cip, 12345);

        assertEquals(cip, medicalHistory.getCip());
        assertEquals(12345, medicalHistory.getMembShipNumb());
        assertEquals("", medicalHistory.getHistory());
    }

    @Test
    public void testConstructorWithNullCIPThrowsException() {
        assertThrows(IncorrectParametersException.class, () -> {
            new MedicalHistory(null, 12345);
        });
    }

    @Test
    public void testConstructorWithNegativeMemberShipNumThrowsException() throws HealthCardIDException {
        HealthCardID cip = new HealthCardID("1234567890ABCDEF");
        assertThrows(IncorrectParametersException.class, () -> {
            new MedicalHistory(cip, -1);
        });
    }

    @Test
    public void testAddMedicalHistoryAnnotations() throws HealthCardIDException, IncorrectParametersException {
        HealthCardID cip = new HealthCardID("1234567890ABCDEF");
        MedicalHistory medicalHistory = new MedicalHistory(cip, 12345);

        medicalHistory.addMedicalHistoryAnnotations("Patient has high blood pressure");
        assertTrue(medicalHistory.getHistory().contains("Patient has high blood pressure"));

        medicalHistory.addMedicalHistoryAnnotations("Prescribed medication X");
        assertTrue(medicalHistory.getHistory().contains("Prescribed medication X"));
    }
}
