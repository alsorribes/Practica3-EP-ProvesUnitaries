package medicalconsultation;

import data.HealthCardID;

/**
 * TEMPORARY -
 * Represents a patient's medical history.
 */
public class MedicalHistory {
    private HealthCardID cip;
    private int membShipNumb;
    private String history;

    public MedicalHistory(HealthCardID cip, int memberShipNum) {
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