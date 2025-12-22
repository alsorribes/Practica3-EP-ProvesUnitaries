package medicalconsultation;

import data.*;
import exceptions.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * TEMPORARY -
 * Represents a medical prescription for a patient.
 */
public class MedicalPrescription {
    private HealthCardID cip;
    private int membShipNumb;
    private String illness;
    private ePrescripCode prescCode;
    private Date prescDate;
    private Date endDate;
    private DigitalSignature eSign;
    private Map<ProductID, Object> lines; // Simplified

    public MedicalPrescription(HealthCardID cip, int membShipNumb, String illness) {
        this.cip = cip;
        this.membShipNumb = membShipNumb;
        this.illness = illness;
        this.lines = new HashMap<>();
    }

    public void addLine(ProductID prodID, String[] instruc)
            throws ProductAlreadyInPrescriptionException, IncorrectTakingGuidelinesException {
        if (lines.containsKey(prodID)) {
            throw new ProductAlreadyInPrescriptionException();
        }
        lines.put(prodID, instruc); // Simplified
    }

    public void modifyDoseInLine(ProductID prodID, float newDose)
            throws ProductNotInPrescriptionException {
        if (!lines.containsKey(prodID)) {
            throw new ProductNotInPrescriptionException();
        }
        // Simplified
    }

    public void removeLine(ProductID prodID) throws ProductNotInPrescriptionException {
        if (!lines.containsKey(prodID)) {
            throw new ProductNotInPrescriptionException();
        }
        lines.remove(prodID);
    }

    // Getters and setters
    public HealthCardID getCip() { return cip; }
    public int getMembShipNumb() { return membShipNumb; }
    public String getIllness() { return illness; }
    public ePrescripCode getPrescCode() { return prescCode; }
    public Date getPrescDate() { return prescDate; }
    public Date getEndDate() { return endDate; }
    public DigitalSignature geteSign() { return eSign; }

    public void setPrescCode(ePrescripCode prescCode) { this.prescCode = prescCode; }
    public void setPrescDate(Date prescDate) { this.prescDate = prescDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    public void seteSign(DigitalSignature eSign) { this.eSign = eSign; }
}