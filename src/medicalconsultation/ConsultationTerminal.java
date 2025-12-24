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

    /**
     * Doctor requests to parse the AI's text response into structured suggestions.
     * The AI breaks down its response into specific actions (Insert/Eliminate/Modify)
     * following a defined pattern for easier review by the doctor.
     *
     * Pattern format per suggestion:
     * - INSERT: <I, ProductID, dayMoment, duration, dose, freq, freqUnit, instructions>
     * - ELIMINATE: <E, ProductID>
     * - MODIFY: <M, ProductID, [only changed fields, others empty]>
     *
     * CONTRACT:
     * - Preconditions:
     *   * Prescription edition mode active
     *   * AI has provided a response to a prompt
     * - Postconditions:
     *   * List<Suggestion> created with parsed suggestions
     *   * Each Suggestion instance initialized with AI recommendations
     *
     * @return List of structured Suggestion objects
     * @throws ProceduralException if preconditions not met
     */
    public List<Suggestion> extractGuidelinesFromSugg() throws ProceduralException {

        // Check preconditions
        if (!prescriptionEditionMode) {
            throw new ProceduralException(
                    "Cannot extract guidelines: prescription edition not initialized");
        }

        if (!aiInitialized || lastAIResponse == null) {
            throw new ProceduralException(
                    "Cannot extract guidelines: AI has not provided a response yet");
        }

        // Parse AI response into structured suggestions
        List<Suggestion> suggestions = decisionMakingAI.parseSuggest(lastAIResponse);

        // Return parsed suggestions for doctor review
        return suggestions;
    }

    // ========== PRESCRIPTION EDITING INPUT EVENTS ==========

    /**
     * Doctor adds a new medicine to the prescription with complete administration guidelines.
     * Creates a new prescription line with all medication details.
     *
     * CONTRACT:
     * - Preconditions: Prescription edition mode must be active
     * - Postconditions:
     *   * New MedicalPrescription line component created
     *   * Guideline instance created with dayMoment, duration, instructions
     *   * Posology instance created with dose, freq, freqUnit
     *   * Line associated with MedicalPrescription
     *
     * @param prodID the product identifier of the medicine
     * @param instruc array with guidelines: [dayMoment, duration, dose, freq, freqUnit, instructions]
     * @throws ProductAlreadyInPrescriptionException if product already in prescription
     * @throws IncorrectTakingGuidelinesException if guidelines format is incorrect
     * @throws ProceduralException if prescription edition not active
     */
    public void enterMedicineWithGuidelines(ProductID prodID, String[] instruc)
            throws ProductAlreadyInPrescriptionException,
            IncorrectTakingGuidelinesException,
            ProceduralException {

        // Check precondition: prescription edition mode must be active
        if (!prescriptionEditionMode) {
            throw new ProceduralException(
                    "Cannot add medicine: prescription edition not initialized");
        }

        // Validate input parameters
        if (prodID == null) {
            throw new IllegalArgumentException("ProductID cannot be null");
        }

        if (instruc == null || instruc.length == 0) {
            throw new IncorrectTakingGuidelinesException(
                    "Guidelines cannot be null or empty");
        }

        // Delegate to internal operation
        createMedPrescriptionLine(prodID, instruc);

        // Add line to prescription (will throw exceptions if invalid)
        currentPrescription.addLine(prodID, instruc);
    }

    /**
     * Doctor modifies the dose of an existing medicine in the prescription.
     * Updates the prescription line for the specified product.
     *
     * CONTRACT:
     * - Preconditions: Prescription edition mode must be active
     * - Postconditions: Posology.dose attribute updated to newDose value
     *
     * @param prodID the product identifier of the medicine to modify
     * @param newDose the new dose value
     * @throws ProductNotInPrescriptionException if product not found in prescription
     * @throws ProceduralException if prescription edition not active
     */
    public void modifyDoseInLine(ProductID prodID, float newDose)
            throws ProductNotInPrescriptionException, ProceduralException {

        // Check precondition: prescription edition mode must be active
        if (!prescriptionEditionMode) {
            throw new ProceduralException(
                    "Cannot modify dose: prescription edition not initialized");
        }

        // Validate input parameters
        if (prodID == null) {
            throw new IllegalArgumentException("ProductID cannot be null");
        }

        if (newDose <= 0) {
            throw new IllegalArgumentException("Dose must be greater than 0");
        }

        // Modify dose in prescription line
        currentPrescription.modifyDoseInLine(prodID, newDose);
    }

    /**
     * Doctor removes a medicine from the prescription.
     * Deletes the prescription line for the specified product.
     *
     * CONTRACT:
     * - Preconditions: Prescription edition mode must be active
     * - Postconditions:
     *   * MedicalPrescription line component destroyed
     *   * Association broken
     *
     * @param prodID the product identifier of the medicine to remove
     * @throws ProductNotInPrescriptionException if product not found in prescription
     * @throws ProceduralException if prescription edition not active
     */
    public void removeLine(ProductID prodID)
            throws ProductNotInPrescriptionException, ProceduralException {

        // Check precondition: prescription edition mode must be active
        if (!prescriptionEditionMode) {
            throw new ProceduralException(
                    "Cannot remove line: prescription edition not initialized");
        }

        // Validate input parameter
        if (prodID == null) {
            throw new IllegalArgumentException("ProductID cannot be null");
        }

        // Remove line from prescription
        currentPrescription.removeLine(prodID);
    }

    /**
     * Doctor enters the treatment ending date.
     * Sets both the prescription date (current date) and ending date.
     *
     * CONTRACT:
     * - Preconditions: Prescription edition mode must be active
     * - Postconditions:
     *   * MedicalPrescription.prescDate set to current date
     *   * MedicalPrescription.endDate set to provided date
     *
     * @param date the treatment ending date
     * @throws IncorrectEndingDateException if date is invalid (past, current, or too close)
     * @throws ProceduralException if prescription edition not active
     */
    public void enterTreatmentEndingDate(Date date)
            throws IncorrectEndingDateException, ProceduralException {

        // Check precondition: prescription edition mode must be active
        if (!prescriptionEditionMode) {
            throw new ProceduralException(
                    "Cannot set ending date: prescription edition not initialized");
        }

        // Validate input parameter
        if (date == null) {
            throw new IllegalArgumentException("Ending date cannot be null");
        }

        // Validate that ending date is appropriate
        Date currentDate = new Date();

        // Check if date is in the past or equal to current date
        if (date.before(currentDate) || date.equals(currentDate)) {
            throw new IncorrectEndingDateException(
                    "Ending date cannot be in the past or equal to current date");
        }

        // Check if date is too close (less than 1 day ahead)
        long diffInMillis = date.getTime() - currentDate.getTime();
        long diffInDays = diffInMillis / (1000 * 60 * 60 * 24);

        if (diffInDays < 1) {
            throw new IncorrectEndingDateException(
                    "Ending date is too close to current date (minimum 1 day required)");
        }

        // Delegate to internal operation
        setPrescDateAndEndDate(date);

        // Mark treatment dates as set
        this.treatmentDatesSet = true;
    }

    /**
     * Internal operation: Sets prescription date (current) and ending date.
     * Called after electronic signature is stamped.
     *
     * @param date the ending date for the treatment
     */
    private void setPrescDateAndEndDate(Date date) {
        Date currentDate = new Date();

        // Set prescription date to current date
        currentPrescription.setPrescDate(currentDate);

        // Set ending date to provided date
        currentPrescription.setEndDate(date);
    }

    /**
     * Doctor indicates the end of the prescription editing process.
     * Exits prescription edition mode.
     *
     * CONTRACT:
     * - Preconditions: Prescription edition mode must be active
     * - Postconditions: Prescription edition mode deactivated
     *
     * @throws ProceduralException if prescription edition not active
     */
    public void finishMedicalPrescriptionEdition() throws ProceduralException {

        // Check precondition: prescription edition mode must be active
        if (!prescriptionEditionMode) {
            throw new ProceduralException(
                    "Cannot finish edition: prescription edition not initialized");
        }

        // Exit prescription edition mode
        this.prescriptionEditionMode = false;
    }

    /**
     * Doctor stamps their electronic signature on the prescription.
     * Incorporates the digital signature into the medical prescription.
     *
     * CONTRACT:
     * - Preconditions:
     *   * Prescription edition mode active
     *   * Treatment dates set (prescDate and endDate)
     * - Postconditions: MedicalPrescription.eSign set to doctor's digital signature
     *
     * @throws eSignatureException if there's a problem stamping the signature
     * @throws ProceduralException if preconditions not met
     */
    public void stampeeSignature() throws eSignatureException, ProceduralException {

        // Check preconditions
        if (!prescriptionEditionMode) {
            throw new ProceduralException(
                    "Cannot stamp signature: prescription edition not initialized");
        }

        if (!treatmentDatesSet) {
            throw new ProceduralException(
                    "Cannot stamp signature: treatment ending date not set. " +
                            "Call enterTreatmentEndingDate() first");
        }

        // Simulate obtaining doctor's digital signature
        // In a real system, this would involve:
        // 1. Reading from a smart card or certificate
        // 2. Cryptographic signing of the prescription data
        // 3. Hardware security module interaction

        try {
            // For now, create a simple signature representation
            // This would be replaced with actual cryptographic signature
            String signatureData = "DIGITAL_SIGNATURE_" + System.currentTimeMillis();
            byte[] signature = signatureData.getBytes();

            DigitalSignature digitalSignature = new DigitalSignature(signature);

            // Stamp signature on prescription
            currentPrescription.seteSign(digitalSignature);

            // Mark signature as stamped
            this.signatureStamped = true;

        } catch (Exception e) {
            throw new eSignatureException(
                    "Error generating or stamping digital signature: " + e.getMessage());
        }
    }

    /**
     * Doctor sends the updated medical history and prescription to the Health National Service.
     * If successful, HNS generates a new treatment code for the prescription.
     *
     * CONTRACT:
     * - Preconditions:
     *   * Prescription edition mode active
     *   * Treatment dates set
     *   * Electronic signature stamped
     * - Postconditions:
     *   * MedicalHistory transmitted to HNS for persistent storage
     *   * Old MedicalPrescription destroyed
     *   * New MedicalPrescription instance created with treatment code
     *   * New prescription associated with HNS
     *   * MedicalPrescription.prescCode updated with HNS-generated code
     *   * Prescription officially registered and linked to patient's e-prescription
     *
     * @return the updated MedicalPrescription with the new treatment code
     * @throws ConnectException if network connection fails
     * @throws HealthCardIDException if patient ID not registered
     * @throws AnyCurrentPrescriptionException if no active prescription exists
     * @throws NotCompletedMedicalPrescriptionException if prescription incomplete
     * @throws ProceduralException if preconditions not met
     */
    public MedicalPrescription sendHistoryAndPrescription()
            throws ConnectException, HealthCardIDException,
            AnyCurrentPrescriptionException,
            NotCompletedMedicalPrescriptionException,
            ProceduralException {

        // Check preconditions
        if (!prescriptionEditionMode) {
            throw new ProceduralException(
                    "Cannot send data: prescription edition not initialized");
        }

        if (!treatmentDatesSet) {
            throw new ProceduralException(
                    "Cannot send data: treatment ending date not set");
        }

        if (!signatureStamped) {
            throw new ProceduralException(
                    "Cannot send data: electronic signature not stamped. " +
                            "Call stampeeSignature() first");
        }

        // Send history and prescription to HNS for validation and storage
        // This will generate a new treatment code
        MedicalPrescription updatedPrescription =
                healthNationalService.sendHistoryAndPrescription(
                        currentPrescription.getCip(),
                        currentMedicalHistory,
                        currentIllness,
                        currentPrescription
                );

        // Replace current prescription with the updated one (with new code)
        // This simulates the destruction of old instance and creation of new one
        this.currentPrescription = updatedPrescription;

        // Return the updated prescription with treatment code
        return updatedPrescription;
    }


    // ========== INTERNAL OPERATIONS ==========

    /**
     * Internal operation: Creates a medical prescription line with all details.
     * This method encapsulates the logic for validating and creating prescription lines.
     *
     * @param prodID the product identifier
     * @param instruc the guidelines array
     * @throws IncorrectTakingGuidelinesException if format is incorrect
     */
    private void createMedPrescriptionLine(ProductID prodID, String[] instruc)
            throws IncorrectTakingGuidelinesException {

        // Validate guidelines array has minimum required elements
        // Expected format: [dayMoment, duration, dose, freq, freqUnit, instructions]
        if (instruc.length < 5) {
            throw new IncorrectTakingGuidelinesException(
                    "Insufficient guidelines data. Expected at least 5 elements: " +
                            "[dayMoment, duration, dose, freq, freqUnit]");
        }

        // Validation logic for each field would go here
        // For now, basic validation that fields are not null/empty
        for (int i = 0; i < 5; i++) {
            if (instruc[i] == null || instruc[i].trim().isEmpty()) {
                throw new IncorrectTakingGuidelinesException(
                        "Guideline field at position " + i + " cannot be null or empty");
            }
        }
    }
}