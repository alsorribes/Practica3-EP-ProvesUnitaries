package test.domain;

import data.*;
import exceptions.*;
import medicalconsultation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MedicalPrescription - Unit Tests")
public class MedicalPrescriptionTest {

    private MedicalPrescription prescription;
    private HealthCardID cip;
    private ProductID productID1;
    private ProductID productID2;

    @BeforeEach
    public void setUp() throws IncorrectParametersException {
        cip = new HealthCardID("1234567890ABCDEF");
        prescription = new MedicalPrescription(cip, 12345, "Hypertension");
        productID1 = new ProductID("243516578917"); // 12 digits
        productID2 = new ProductID("640557143200"); // 12 digits
    }

    @Test
    @DisplayName("Constructor - Valid construction")
    public void testValidConstructor() throws IncorrectParametersException {
        HealthCardID testCip = new HealthCardID("ABCD1234567890EF");
        MedicalPrescription presc = new MedicalPrescription(testCip, 99999, "Diabetes");

        assertEquals(testCip, presc.getCip());
        assertEquals(99999, presc.getMembShipNumb());
        assertEquals("Diabetes", presc.getIllness());
        assertNotNull(presc.getLines());
        assertEquals(0, presc.getLines().size());
    }

    @Test
    @DisplayName("Constructor - Throws exception with null HealthCardID")
    public void testConstructorNullCip() {
        assertThrows(IncorrectParametersException.class, () -> {
            new MedicalPrescription(null, 12345, "Illness");
        });
    }

    @Test
    @DisplayName("Constructor - Throws exception with null illness")
    public void testConstructorNullIllness() throws IncorrectParametersException {
        HealthCardID testCip = new HealthCardID("1234567890ABCDEF");
        assertThrows(IncorrectParametersException.class, () -> {
            new MedicalPrescription(testCip, 12345, null);
        });
    }

    @Test
    @DisplayName("Constructor - Throws exception with empty illness")
    public void testConstructorEmptyIllness() throws IncorrectParametersException {
        HealthCardID testCip = new HealthCardID("1234567890ABCDEF");
        assertThrows(IncorrectParametersException.class, () -> {
            new MedicalPrescription(testCip, 12345, "   ");
        });
    }

    @Test
    @DisplayName("Constructor - Throws exception with negative membership number")
    public void testConstructorNegativeMembership() throws IncorrectParametersException {
        HealthCardID testCip = new HealthCardID("1234567890ABCDEF");
        assertThrows(IncorrectParametersException.class, () -> {
            new MedicalPrescription(testCip, -1, "Illness");
        });
    }

    @Test
    @DisplayName("addLine - Success with valid 6-element array")
    public void testAddLineSuccess() throws ProductAlreadyInPrescriptionException, IncorrectTakingGuidelinesException {
        // Format: [dayMoment, duration, dose, freq, freqUnit, instructions]
        String[] instruc = {"BEFORELUNCH", "15", "1", "1", "DAY", "Take with water"};

        prescription.addLine(productID1, instruc);

        assertTrue(prescription.getLines().containsKey(productID1));
        assertEquals(1, prescription.getLines().size());
    }

    @Test
    @DisplayName("addLine - Success with valid 7-element array (extra ignored)")
    public void testAddLineSuccessWithExtraElement() throws ProductAlreadyInPrescriptionException, IncorrectTakingGuidelinesException {
        // Format: [dayMoment, duration, dose, freq, freqUnit, instructions, EXTRA]
        String[] instruc = {"BEFORELUNCH", "15", "1", "1", "DAY", "Take with water", "extra"};

        prescription.addLine(productID1, instruc);

        assertTrue(prescription.getLines().containsKey(productID1));
        assertEquals(1, prescription.getLines().size());
    }

    @Test
    @DisplayName("addLine - Throws exception for duplicate product")
    public void testAddLineDuplicateProduct() throws ProductAlreadyInPrescriptionException, IncorrectTakingGuidelinesException {
        String[] instruc = {"BEFORELUNCH", "15", "1", "1", "DAY", "Take with water"};

        prescription.addLine(productID1, instruc);

        // Try to add the same product again
        assertThrows(ProductAlreadyInPrescriptionException.class, () -> {
            prescription.addLine(productID1, instruc);
        });
    }

    @Test
    @DisplayName("addLine - Throws exception for incomplete guidelines (5 elements)")
    public void testAddLineIncorrectGuidelinesIncomplete() {
        String[] instruc = {"BEFORELUNCH", "15", "1", "1", "DAY"}; // Only 5 elements

        assertThrows(IncorrectTakingGuidelinesException.class, () -> {
            prescription.addLine(productID1, instruc);
        });
    }

    @Test
    @DisplayName("addLine - Throws exception for null guidelines")
    public void testAddLineIncorrectGuidelinesNull() {
        assertThrows(IncorrectTakingGuidelinesException.class, () -> {
            prescription.addLine(productID1, null);
        });
    }

    @Test
    @DisplayName("addLine - Throws exception for invalid dayMoment")
    public void testAddLineIncorrectGuidelinesInvalidDayMoment() {
        String[] instruc = {"INVALID_DAY_MOMENT", "15", "1", "1", "DAY", "Take with water"};

        assertThrows(IncorrectTakingGuidelinesException.class, () -> {
            prescription.addLine(productID1, instruc);
        });
    }

    @Test
    @DisplayName("addLine - Throws exception for invalid FqUnit")
    public void testAddLineInvalidFqUnit() {
        String[] instruc = {"BEFORELUNCH", "15", "1", "1", "INVALID_UNIT", "Take with water"};

        assertThrows(IncorrectTakingGuidelinesException.class, () -> {
            prescription.addLine(productID1, instruc);
        });
    }

    @Test
    @DisplayName("addLine - Throws exception for invalid duration (non-numeric)")
    public void testAddLineInvalidDuration() {
        String[] instruc = {"BEFORELUNCH", "ABC", "1", "1", "DAY", "Take with water"};

        assertThrows(IncorrectTakingGuidelinesException.class, () -> {
            prescription.addLine(productID1, instruc);
        });
    }

    @Test
    @DisplayName("addLine - Throws exception for negative duration")
    public void testAddLineNegativeDuration() {
        String[] instruc = {"BEFORELUNCH", "-5", "1", "1", "DAY", "Take with water"};

        assertThrows(IncorrectTakingGuidelinesException.class, () -> {
            prescription.addLine(productID1, instruc);
        });
    }

    @Test
    @DisplayName("addLine - Throws exception for zero dose")
    public void testAddLineZeroDose() {
        String[] instruc = {"BEFORELUNCH", "15", "0", "1", "DAY", "Take with water"};

        assertThrows(IncorrectTakingGuidelinesException.class, () -> {
            prescription.addLine(productID1, instruc);
        });
    }

    @Test
    @DisplayName("addLine - Throws exception for empty instructions")
    public void testAddLineEmptyInstructions() {
        String[] instruc = {"BEFORELUNCH", "15", "1", "1", "DAY", "   "};

        assertThrows(IncorrectTakingGuidelinesException.class, () -> {
            prescription.addLine(productID1, instruc);
        });
    }

    @Test
    @DisplayName("modifyDoseInLine - Success")
    public void testModifyDoseInLineSuccess() throws ProductAlreadyInPrescriptionException, IncorrectTakingGuidelinesException, ProductNotInPrescriptionException {
        String[] instruc = {"BEFORELUNCH", "15", "1", "1", "DAY", "Take with water"};
        prescription.addLine(productID1, instruc);

        float originalDose = prescription.getLines().get(productID1).getTakingGuideline().getPosology().getDose();
        assertEquals(1.0f, originalDose);

        prescription.modifyDoseInLine(productID1, 3.0f);

        float modifiedDose = prescription.getLines().get(productID1).getTakingGuideline().getPosology().getDose();
        assertEquals(3.0f, modifiedDose);
    }

    @Test
    @DisplayName("modifyDoseInLine - Throws exception for product not found")
    public void testModifyDoseInLineProductNotFound() {
        assertThrows(ProductNotInPrescriptionException.class, () -> {
            prescription.modifyDoseInLine(productID1, 3.0f);
        });
    }

    @Test
    @DisplayName("modifyDoseInLine - Throws exception for negative dose")
    public void testModifyDoseInLineNegativeDose() throws ProductAlreadyInPrescriptionException, IncorrectTakingGuidelinesException {
        String[] instruc = {"BEFORELUNCH", "15", "1", "1", "DAY", "Take with water"};
        prescription.addLine(productID1, instruc);

        assertThrows(IllegalArgumentException.class, () -> {
            prescription.modifyDoseInLine(productID1, -5.0f);
        });
    }

    @Test
    @DisplayName("modifyDoseInLine - Throws exception for zero dose")
    public void testModifyDoseInLineZeroDose() throws ProductAlreadyInPrescriptionException, IncorrectTakingGuidelinesException {
        String[] instruc = {"BEFORELUNCH", "15", "1", "1", "DAY", "Take with water"};
        prescription.addLine(productID1, instruc);

        assertThrows(IllegalArgumentException.class, () -> {
            prescription.modifyDoseInLine(productID1, 0.0f);
        });
    }

    @Test
    @DisplayName("removeLine - Success")
    public void testRemoveLineSuccess() throws ProductAlreadyInPrescriptionException, IncorrectTakingGuidelinesException, ProductNotInPrescriptionException {
        String[] instruc = {"BEFORELUNCH", "15", "1", "1", "DAY", "Take with water"};
        prescription.addLine(productID1, instruc);

        assertEquals(1, prescription.getLines().size());
        assertTrue(prescription.getLines().containsKey(productID1));

        prescription.removeLine(productID1);

        assertEquals(0, prescription.getLines().size());
        assertFalse(prescription.getLines().containsKey(productID1));
    }

    @Test
    @DisplayName("removeLine - Throws exception for product not found")
    public void testRemoveLineProductNotFound() {
        assertThrows(ProductNotInPrescriptionException.class, () -> {
            prescription.removeLine(productID1);
        });
    }

    @Test
    @DisplayName("Multiple operations - Add, modify, remove")
    public void testMultipleOperations() throws ProductAlreadyInPrescriptionException,
            IncorrectTakingGuidelinesException, ProductNotInPrescriptionException {
        String[] instruc1 = {"BEFORELUNCH", "15", "1", "2", "DAY", "Take with water"};
        String[] instruc2 = {"AFTERDINNER", "30", "2", "1", "DAY", "Take before sleep"};

        // Add two products
        prescription.addLine(productID1, instruc1);
        prescription.addLine(productID2, instruc2);
        assertEquals(2, prescription.getLines().size());

        // Modify one
        prescription.modifyDoseInLine(productID1, 5.0f);
        assertEquals(5.0f, prescription.getLines().get(productID1)
                .getTakingGuideline().getPosology().getDose());

        // Remove one
        prescription.removeLine(productID2);
        assertEquals(1, prescription.getLines().size());
        assertFalse(prescription.getLines().containsKey(productID2));
    }

    @Test
    @DisplayName("Getters and setters")
    public void testGettersAndSetters() throws IncorrectParametersException {
        // Test prescription code
        ePrescripCode code = new ePrescripCode("CODE123456789ABC");
        prescription.setPrescCode(code);
        assertEquals(code, prescription.getPrescCode());

        // Test dates
        Date prescDate = new Date();
        prescription.setPrescDate(prescDate);
        assertEquals(prescDate, prescription.getPrescDate());

        Date endDate = new Date(System.currentTimeMillis() + 86400000);
        prescription.setEndDate(endDate);
        assertEquals(endDate, prescription.getEndDate());

        // Test digital signature
        DigitalSignature signature = new DigitalSignature(new byte[]{1, 2, 3});
        prescription.seteSign(signature);
        assertEquals(signature, prescription.geteSign());
    }
}