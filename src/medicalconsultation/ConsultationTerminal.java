package medicalconsultation;

import data.*;
import exceptions.*;
import services.*;
import java.net.ConnectException;
import java.util.Date;
import java.util.List;

/**
 * Facade Controller for the "Supervise Treatment" use case.
 * Acts as intermediary between the user interface and the system.
 * Manages the workflow state and coordinates with external services.
 *
 * This class follows the GRASP Controller pattern (Facade Controller).
 */
public class ConsultationTerminal {

    // External services (injected via setters)
    private HealthNationalService healthNationalService;
    private DecisionMakingAI decisionMakingAI;

    // Current session state
    private MedicalHistory currentMedicalHistory;
    private MedicalPrescription currentPrescription;
    private String currentIllness;

    // Workflow state flags
    private boolean revisionInitialized;
    private boolean prescriptionEditionMode;
    private boolean aiInitialized;
    private String lastAIResponse;
    private boolean treatmentDatesSet;
    private boolean signatureStamped;

    /**
     * Constructor - initializes the terminal in idle state.
     */
    public ConsultationTerminal() {
        this.revisionInitialized = false;
        this.prescriptionEditionMode = false;
        this.aiInitialized = false;
        this.treatmentDatesSet = false;
        this.signatureStamped = false;
    }

    // ========== DEPENDENCY INJECTION SETTERS ==========

    /**
     * Injects the HealthNationalService dependency.
     * @param service the health national service implementation
     */
    public void setHealthNationalService(HealthNationalService service) {
        this.healthNationalService = service;
    }

    /**
     * Injects the DecisionMakingAI dependency.
     * @param ai the decision making AI implementation
     */
    public void setDecisionMakingAI(DecisionMakingAI ai) {
        this.decisionMakingAI = ai;
    }

    // ========== STATE MANAGEMENT METHODS ==========

    /**
     * Resets the terminal state after completing a revision.
     */
    private void resetState() {
        this.currentMedicalHistory = null;
        this.currentPrescription = null;
        this.currentIllness = null;
        this.revisionInitialized = false;
        this.prescriptionEditionMode = false;
        this.aiInitialized = false;
        this.lastAIResponse = null;
        this.treatmentDatesSet = false;
        this.signatureStamped = false;
    }

}