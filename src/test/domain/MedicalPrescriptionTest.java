package domain;

import data.*;
import exceptions.*;
import medicalconsultation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MedicalPrescriptionTest {

    private MedicalPrescription prescription;
    private HealthCardID cip;
    private ProductID productID1;
    private ProductID productID2;

    @BeforeEach
    public void setUp() throws HealthCardIDException {
        cip = new HealthCardID("1234567890ABCDEF");
        prescription = new MedicalPrescription(cip, 12345, "Hypertension");
        productID1 = new ProductID("1234567890123");
        productID2 = new ProductID("9876543210987");
    }

    @Test
    public void testAddLineSuccess() throws ProductAlreadyInPrescriptionException, IncorrectTakingGuidelinesException {
        String[] instruc = {"BEFORELUNCH", "15", "1", "1", "DAY", "Take with water", ""};
        prescription.addLine(productID1, instruc);
        assertTrue(prescription.getLines().containsKey(productID1));
        assertEquals(1, prescription.getLines().size());
    }

    @Test
    public void testAddLineDuplicateProduct() throws ProductAlreadyInPrescriptionException, IncorrectTakingGuidelinesException {
        String[] instruc = {"BEFORELUNCH", "15", "1", "1", "DAY", "Take with water", ""};
        prescription.addLine(productID1, instruc);
        assertThrows(ProductAlreadyInPrescriptionException.class, () -> {
            prescription.addLine(productID1, instruc);
        });
    }

    @Test
    public void testAddLineIncorrectGuidelinesIncomplete() {
        String[] instruc = {"BEFORELUNCH", "15", "1"};
        assertThrows(IncorrectTakingGuidelinesException.class, () -> {
            prescription.addLine(productID1, instruc);
        });
    }

    @Test
    public void testAddLineIncorrectGuidelinesNull() {
        assertThrows(IncorrectTakingGuidelinesException.class, () -> {
            prescription.addLine(productID1, null);
        });
    }

    @Test
    public void testAddLineIncorrectGuidelinesInvalidFormat() {
        String[] instruc = {"INVALID_DAY_MOMENT", "15", "1", "1", "DAY", "Take with water", ""};
        assertThrows(IncorrectTakingGuidelinesException.class, () -> {
            prescription.addLine(productID1, instruc);
        });
    }
}
