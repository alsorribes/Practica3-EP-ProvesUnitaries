package medicalconsultation;

import data.ProductID;

/**
 * Represents a line in a medical prescription.
 * Each line contains a product and its taking guidelines.
 */
public class MedicalPrescriptionLine {
    private ProductID productID;
    private TakingGuideline takingGuideline;

    public MedicalPrescriptionLine(ProductID productID, TakingGuideline takingGuideline) {
        this.productID = productID;
        this.takingGuideline = takingGuideline;
    }

    public ProductID getProductID() {
        return productID;
    }

    public void setProductID(ProductID productID) {
        this.productID = productID;
    }

    public TakingGuideline getTakingGuideline() {
        return takingGuideline;
    }

    public void setTakingGuideline(TakingGuideline takingGuideline) {
        this.takingGuideline = takingGuideline;
    }
}
