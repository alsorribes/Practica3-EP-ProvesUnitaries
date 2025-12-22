package medicalconsultation;

import data.ProductID;

/**
 * TEMPORARY -
 * Represents a suggestion from the AI system for prescription adjustment.
 *
 * Pattern: <ACTION, ProductID, [optional guidelines...]>
 * Actions: I (Insert), E (Eliminate), M (Modify)
 */
public class Suggestion {

    public enum Action {
        INSERT, ELIMINATE, MODIFY
    }

    private Action action;
    private ProductID productID;
    private String[] guidelines; // Optional guidelines for I and M actions

    public Suggestion(Action action, ProductID productID, String[] guidelines) {
        this.action = action;
        this.productID = productID;
        this.guidelines = guidelines;
    }

    public Suggestion(Action action, ProductID productID) {
        this(action, productID, null);
    }

    public Action getAction() {
        return action;
    }

    public ProductID getProductID() {
        return productID;
    }

    public String[] getGuidelines() {
        return guidelines;
    }

    @Override
    public String toString() {
        return "Suggestion{" +
                "action=" + action +
                ", productID=" + productID +
                ", guidelines=" + (guidelines != null ? String.join(",", guidelines) : "none") +
                '}';
    }
}