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

    public MedicalPrescription(HealthCardID cip, int membShipNumb, String illness)
            throws IncorrectParametersException {
        if (cip == null) {
            throw new IncorrectParametersException("HealthCardID cannot be null");
        }
        if (illness == null || illness.trim().isEmpty()) {
            throw new IncorrectParametersException("Illness cannot be null or empty");
        }
        if (membShipNumb < 0) {
            throw new IncorrectParametersException("Membership number cannot be negative");
        }

        this.cip = cip;
        this.membShipNumb = membShipNumb;
        this.illness = illness;
        this.lines = new HashMap<>();
    }

    /**
     * Adds a new line to the prescription.
     *
     * @param prodID the product identifier
     * @param instruc array with 6 elements: [dayMoment, duration, dose, freq, freqUnit, instructions]
     * @throws ProductAlreadyInPrescriptionException if product already exists
     * @throws IncorrectTakingGuidelinesException if guidelines format is incorrect
     */
    public void addLine(ProductID prodID, String[] instruc)
            throws ProductAlreadyInPrescriptionException, IncorrectTakingGuidelinesException {
        if (lines.containsKey(prodID)) {
            throw new ProductAlreadyInPrescriptionException();
        }

        // Validate that instruc has the correct format (6 elements minimum)
        // Format: [dayMoment, duration, dose, freq, freqUnit, instructions]
        if (instruc == null || instruc.length < 6) {
            throw new IncorrectTakingGuidelinesException(
                    "Incomplete taking guidelines. Expected at least 6 elements: " +
                            "[dayMoment, duration, dose, freq, freqUnit, instructions]");
        }

        try {
            // Parse the instructions array to create TakingGuideline
            dayMoment dM = dayMoment.valueOf(instruc[0]);
            float duration = Float.parseFloat(instruc[1]);
            float dose = Float.parseFloat(instruc[2]);
            float freq = Float.parseFloat(instruc[3]);
            FqUnit freqUnit = FqUnit.valueOf(instruc[4]);
            String instructions = instruc[5];

            // Validate parsed values
            if (duration <= 0) {
                throw new IncorrectTakingGuidelinesException("Duration must be positive");
            }
            if (dose <= 0) {
                throw new IncorrectTakingGuidelinesException("Dose must be positive");
            }
            if (freq <= 0) {
                throw new IncorrectTakingGuidelinesException("Frequency must be positive");
            }
            if (instructions == null || instructions.trim().isEmpty()) {
                throw new IncorrectTakingGuidelinesException("Instructions cannot be empty");
            }

            TakingGuideline guideline = new TakingGuideline(dM, duration, dose, freq, freqUnit, instructions);
            MedicalPrescriptionLine line = new MedicalPrescriptionLine(prodID, guideline);
            lines.put(prodID, line);
        } catch (IllegalArgumentException e) {
            throw new IncorrectTakingGuidelinesException(
                    "Invalid taking guidelines format: " + e.getMessage());
        }
    }

    public void modifyDoseInLine(ProductID prodID, float newDose)
            throws ProductNotInPrescriptionException {
        if (!lines.containsKey(prodID)) {
            throw new ProductNotInPrescriptionException();
        }
        if (newDose <= 0) {
            throw new IllegalArgumentException("Dose must be positive");
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