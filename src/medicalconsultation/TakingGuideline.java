package medicalconsultation;

/**
 * Represents the taking guidelines for a medicine.
 */
public class TakingGuideline {
    private dayMoment dMoment;
    private float duration;
    private Posology posology;
    private String instructions;

    public TakingGuideline(dayMoment dM, float du, float d, float f, FqUnit fu, String i) {
        this.dMoment = dM;
        this.duration = du;
        this.posology = new Posology(d, f, fu);
        this.instructions = i;
    }

    public dayMoment getdMoment() {
        return dMoment;
    }

    public void setdMoment(dayMoment dMoment) {
        this.dMoment = dMoment;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public Posology getPosology() {
        return posology;
    }

    public void setPosology(Posology posology) {
        this.posology = posology;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}
