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

    public void addMedicalHistoryAnnotations(String annot) {
        this.history += annot + "\n";
    }

    public void setNewDoctor(int mshN) {
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