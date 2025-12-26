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
}
