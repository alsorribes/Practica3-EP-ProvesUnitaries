package medicalconsultation;

import data.HealthCardID;
import exceptions.IncorrectParametersException;

/**
 * Represents a patient's medical history.
 */
public class MedicalHistory {
    private HealthCardID cip;
    private int membShipNumb;
    private String history;

    public MedicalHistory(HealthCardID cip, int memberShipNum) throws IncorrectParametersException {
        if (cip == null) {
            throw new IncorrectParametersException("HealthCardID cannot be null");
        }
        if (memberShipNum < 0) {
            throw new IncorrectParametersException("Membership number cannot be negative");
        }
        this.cip = cip;
        this.membShipNumb = memberShipNum;
        this.history = "";
    }

    /**
     * Adds medical history annotations.
     *
     * @param annot the annotation to add
     * @throws IllegalArgumentException if annotation is null or empty
     */
    public void addMedicalHistoryAnnotations(String annot) {
        if (annot == null || annot.trim().isEmpty()) {
            throw new IllegalArgumentException("Annotation cannot be null or empty");
        }
        this.history += annot + "\n";
    }

    /**
     * Sets a new doctor by updating the membership number.
     *
     * @param mshN the new membership number
     * @throws IllegalArgumentException if membership number is negative
     */
    public void setNewDoctor(int mshN) {
        if (mshN < 0) {
            throw new IllegalArgumentException("Membership number cannot be negative");
        }
        this.membShipNumb = mshN;
    }

    public HealthCardID getCip() {
        return cip;
    }

    public int getMembShipNumb() {
        return membShipNumb;
    }

    public String getHistory() {
        return history;
    }
}