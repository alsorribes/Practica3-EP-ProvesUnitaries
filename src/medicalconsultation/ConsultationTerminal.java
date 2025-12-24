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

    // ========== INPUT EVENTS ==========

    /**
     * Initiates a patient revision session.
     * Downloads the medical history and prescription from the Health National Service.
     *
     * CONTRACT:
     * - Preconditions: None
     * - Postconditions:
     *   * MedicalHistory instance created and associated
     *   * MedicalPrescription instance created and associated
     *   * Prescription lines created and initialized
     *
     * @param cip the patient's health card ID
     * @param illness the illness being treated
     * @throws ConnectException if network connection fails
     * @throws HealthCardIDException if patient ID not registered in HNS
     * @throws AnyCurrentPrescriptionException if no active prescription for this illness
     */
    public void initRevision(HealthCardID cip, String illness)
            throws ConnectException, HealthCardIDException, AnyCurrentPrescriptionException {

        // Validate input parameters
        if (cip == null || illness == null || illness.trim().isEmpty()) {
            throw new IllegalArgumentException("CIP and illness cannot be null or empty");
        }

        // Download medical history from HNS
        this.currentMedicalHistory = healthNationalService.getMedicalHistory(cip);

        // Download medical prescription for this illness from HNS
        this.currentPrescription = healthNationalService.getMedicalPrescription(cip, illness);

        // Store current illness
        this.currentIllness = illness;

        // Mark revision as initialized
        this.revisionInitialized = true;

        // Reset other state flags (in case of reuse)
        this.prescriptionEditionMode = false;
        this.aiInitialized = false;
        this.lastAIResponse = null;
        this.treatmentDatesSet = false;
        this.signatureStamped = false;
    }

    /**
     * Doctor reports medical assessments observed during the revision visit.
     * Updates the patient's medical history with new annotations.
     *
     * CONTRACT:
     * - Preconditions: Revision must be initialized
     * - Postconditions: MedicalHistory.history attribute updated with new annotations
     *
     * @param assess the medical assessment text to add to history
     * @throws ProceduralException if revision not initialized
     */
    public void enterMedicalAssessmentInHistory(String assess) throws ProceduralException {

        // Check precondition: revision must be initialized
        if (!revisionInitialized) {
            throw new ProceduralException("Cannot enter assessment: revision not initialized");
        }

        // Validate input
        if (assess == null || assess.trim().isEmpty()) {
            throw new IllegalArgumentException("Assessment cannot be null or empty");
        }

        // Add annotations to medical history
        currentMedicalHistory.addMedicalHistoryAnnotations(assess);
    }

    /**
     * Doctor initiates the prescription editing process.
     * Enters prescription edition mode for the current illness.
     *
     * CONTRACT:
     * - Preconditions: Revision must be initialized
     * - Postconditions: Prescription edition mode activated
     *
     * @throws ProceduralException if revision not initialized
     */
    public void initMedicalPrescriptionEdition() throws ProceduralException {

        // Check precondition: revision must be initialized
        if (!revisionInitialized) {
            throw new ProceduralException(
                    "Cannot init prescription edition: revision not initialized");
        }

        // Enter prescription edition mode
        this.prescriptionEditionMode = true;
    }

    // ========== AI-RELATED INPUT EVENTS ==========

    /**
     * Doctor invokes the Decision Making AI for treatment support.
     * Initializes the AI system to be ready for receiving prompts.
     *
     * CONTRACT:
     * - Preconditions: Prescription edition mode must be active
     * - Postconditions: DecisionMakingAI instance ready in memory
     *
     * @throws AIException if there's a problem starting the AI system
     * @throws ProceduralException if prescription edition not active
     */
    public void callDecisionMakingAI() throws AIException, ProceduralException {

        // Check precondition: prescription edition mode must be active
        if (!prescriptionEditionMode) {
            throw new ProceduralException(
                    "Cannot call AI: prescription edition not initialized");
        }

        // Initialize the AI system
        decisionMakingAI.initDecisionMakingAI();

        // Mark AI as initialized and ready
        this.aiInitialized = true;
    }

    /**
     * Doctor sends a prompt to the AI and receives suggestions in text format.
     * The AI's response is stored for later parsing.
     *
     * CONTRACT:
     * - Preconditions:
     *   * Prescription edition mode active
     *   * AI system initialized and ready
     * - Postconditions: AI response displayed and stored for parsing
     *
     * @param prompt the question or request sent to the AI
     * @return the AI's text response to be displayed
     * @throws BadPromptException if the prompt is unclear or inconsistent
     * @throws ProceduralException if preconditions not met
     */
    public String askAIForSuggest(String prompt)
            throws BadPromptException, ProceduralException {

        // Check preconditions
        if (!prescriptionEditionMode) {
            throw new ProceduralException(
                    "Cannot ask AI: prescription edition not initialized");
        }

        if (!aiInitialized) {
            throw new ProceduralException(
                    "Cannot ask AI: AI system not initialized. Call callDecisionMakingAI() first");
        }

        // Validate input
        if (prompt == null || prompt.trim().isEmpty()) {
            throw new IllegalArgumentException("Prompt cannot be null or empty");
        }

        // Send prompt to AI and get response
        String aiResponse = decisionMakingAI.getSuggestions(prompt);

        // Store response for later parsing
        this.lastAIResponse = aiResponse;

        // Return response to be displayed to doctor
        return aiResponse;
    }



}