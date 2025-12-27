package medicalconsultation;

import data.*;
import exceptions.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
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
    private Map<ProductID, MedicalPrescriptionLine> lines;

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

        // Validate that instruc has the correct format (7 elements)
        // Format: [dayMoment, duration, dose, freq, freqUnit, instructions]
        if (instruc == null || instruc.length < 7) {
            throw new IncorrectTakingGuidelinesException("Incomplete taking guidelines");
        }

        try {
            // Parse the instructions array to create TakingGuideline
            dayMoment dM = dayMoment.valueOf(instruc[0]);
            float duration = Float.parseFloat(instruc[1]);
            float dose = Float.parseFloat(instruc[2]);
            float freq = Float.parseFloat(instruc[3]);
            FqUnit freqUnit = FqUnit.valueOf(instruc[4]);
            String instructions = instruc[5];

            TakingGuideline guideline = new TakingGuideline(dM, duration, dose, freq, freqUnit, instructions);
            MedicalPrescriptionLine line = new MedicalPrescriptionLine(prodID, guideline);
            lines.put(prodID, line);
        } catch (IllegalArgumentException e) {
            throw new IncorrectTakingGuidelinesException("Invalid taking guidelines format");
        }
    }

    public void modifyDoseInLine(ProductID prodID, float newDose)
            throws ProductNotInPrescriptionException {
        if (!lines.containsKey(prodID)) {
            throw new ProductNotInPrescriptionException();
        }
        MedicalPrescriptionLine line = lines.get(prodID);
        line.getTakingGuideline().getPosology().setDose(newDose);
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
    public Map<ProductID, MedicalPrescriptionLine> getLines() { return lines; }

    public void setPrescCode(ePrescripCode prescCode) { this.prescCode = prescCode; }
    public void setPrescDate(Date prescDate) { this.prescDate = prescDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    public void seteSign(DigitalSignature eSign) { this.eSign = eSign; }
}