package test.controller;

import data.*;
import exceptions.*;
import medicalconsultation.*;
import services.*;
import test.doubles.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.net.ConnectException;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ConsultationTerminal class.
 * Tests all input events and workflow scenarios using test doubles.
 */
@DisplayName("ConsultationTerminal - Unit Tests")
class ConsultationTerminalTest {

    // System Under Test
    private ConsultationTerminal terminal;

    // Test doubles
    private HealthNationalService hnsSuccess;
    private HealthNationalService hnsWithErrors;
    private DecisionMakingAI aiSuccess;
    private DecisionMakingAI aiWithErrors;

    // Test data
    private HealthCardID validCip;
    private String validIllness;
    private ProductID validProductID;
    private String[] validGuidelines;

    /**
     * Setup method - initializes test doubles and test data.
     * Called before each test.
     */
    @BeforeEach
    void setUp() {
        // Initialize system under test
        terminal = new ConsultationTerminal();

        // Initialize test doubles
        hnsSuccess = new HealthNationalServiceStubSuccess();
        hnsWithErrors = new HealthNationalServiceStubWithErrors();
        aiSuccess = new DecisionMakingAIStubSuccess();
        aiWithErrors = new DecisionMakingAIStubWithErrors();

        // Initialize test data
        validCip = new HealthCardID("1234567890123456");
        validIllness = "Diabetes";
        validProductID = new ProductID("243516578917");
        validGuidelines = new String[]{
                "BEFORELUNCH",  // dayMoment
                "15",           // duration
                "1",            // dose
                "1",            // frequency
                "DAY",          // frequency unit
                "Tomar con abundante agua" // instructions
        };
    }

    // ========== TESTS FOR initRevision ==========

    @Test
    @DisplayName("initRevision - Success scenario")
    void testInitRevision_Success() throws Exception {
        // Arrange
        terminal.setHealthNationalService(hnsSuccess);

        // Act
        terminal.initRevision(validCip, validIllness);

        // Assert
        assertTrue(terminal.isRevisionInitialized());
        assertNotNull(terminal.getCurrentMedicalHistory());
        assertNotNull(terminal.getCurrentPrescription());
        assertEquals(validIllness, terminal.getCurrentIllness());
    }

    @Test
    @DisplayName("initRevision - Null CIP throws IllegalArgumentException")
    void testInitRevision_NullCIP() {
        // Arrange
        terminal.setHealthNationalService(hnsSuccess);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            terminal.initRevision(null, validIllness);
        });
    }

    @Test
    @DisplayName("initRevision - Null illness throws IllegalArgumentException")
    void testInitRevision_NullIllness() {
        // Arrange
        terminal.setHealthNationalService(hnsSuccess);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            terminal.initRevision(validCip, null);
        });
    }

    @Test
    @DisplayName("initRevision - Empty illness throws IllegalArgumentException")
    void testInitRevision_EmptyIllness() {
        // Arrange
        terminal.setHealthNationalService(hnsSuccess);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            terminal.initRevision(validCip, "   ");
        });
    }

    @Test
    @DisplayName("initRevision - ConnectException on network failure")
    void testInitRevision_ConnectException() {
        // Arrange
        HealthNationalServiceStubWithErrors hnsError =
                (HealthNationalServiceStubWithErrors) hnsWithErrors;
        hnsError.setThrowConnectException(true);
        terminal.setHealthNationalService(hnsError);

        // Act & Assert
        assertThrows(ConnectException.class, () -> {
            terminal.initRevision(validCip, validIllness);
        });
    }

    @Test
    @DisplayName("initRevision - HealthCardIDException for unregistered patient")
    void testInitRevision_HealthCardIDException() {
        // Arrange
        HealthNationalServiceStubWithErrors hnsError =
                (HealthNationalServiceStubWithErrors) hnsWithErrors;
        hnsError.setThrowHealthCardIDException(true);
        terminal.setHealthNationalService(hnsError);

        // Act & Assert
        assertThrows(HealthCardIDException.class, () -> {
            terminal.initRevision(validCip, validIllness);
        });
    }

    @Test
    @DisplayName("initRevision - AnyCurrentPrescriptionException for missing prescription")
    void testInitRevision_AnyCurrentPrescriptionException() {
        // Arrange
        HealthNationalServiceStubWithErrors hnsError =
                (HealthNationalServiceStubWithErrors) hnsWithErrors;
        hnsError.setThrowAnyCurrentPrescriptionException(true);
        terminal.setHealthNationalService(hnsError);

        // Act & Assert
        assertThrows(AnyCurrentPrescriptionException.class, () -> {
            terminal.initRevision(validCip, validIllness);
        });
    }

    // ========== TESTS FOR enterMedicalAssessmentInHistory ==========

    @Test
    @DisplayName("enterAssessment - Success scenario")
    void testEnterAssessment_Success() throws Exception {
        // Arrange
        terminal.setHealthNationalService(hnsSuccess);
        terminal.initRevision(validCip, validIllness);
        String assessment = "Patient shows improvement in glucose levels";

        // Act
        terminal.enterMedicalAssessmentInHistory(assessment);

        // Assert
        String history = terminal.getCurrentMedicalHistory().getHistory();
        assertTrue(history.contains(assessment));
    }

    @Test
    @DisplayName("enterAssessment - ProceduralException when revision not initialized")
    void testEnterAssessment_NoRevisionInitialized() {
        // Arrange
        String assessment = "Some assessment";

        // Act & Assert
        assertThrows(ProceduralException.class, () -> {
            terminal.enterMedicalAssessmentInHistory(assessment);
        });
    }

    @Test
    @DisplayName("enterAssessment - IllegalArgumentException for null assessment")
    void testEnterAssessment_NullAssessment() throws Exception {
        // Arrange
        terminal.setHealthNationalService(hnsSuccess);
        terminal.initRevision(validCip, validIllness);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            terminal.enterMedicalAssessmentInHistory(null);
        });
    }

    @Test
    @DisplayName("enterAssessment - IllegalArgumentException for empty assessment")
    void testEnterAssessment_EmptyAssessment() throws Exception {
        // Arrange
        terminal.setHealthNationalService(hnsSuccess);
        terminal.initRevision(validCip, validIllness);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            terminal.enterMedicalAssessmentInHistory("   ");
        });
    }

    // ========== TESTS FOR initMedicalPrescriptionEdition ==========

    @Test
    @DisplayName("initPrescriptionEdition - Success scenario")
    void testInitPrescriptionEdition_Success() throws Exception {
        // Arrange
        terminal.setHealthNationalService(hnsSuccess);
        terminal.initRevision(validCip, validIllness);

        // Act
        terminal.initMedicalPrescriptionEdition();

        // Assert
        assertTrue(terminal.isPrescriptionEditionMode());
    }

    @Test
    @DisplayName("initPrescriptionEdition - ProceduralException when revision not initialized")
    void testInitPrescriptionEdition_NoRevisionInitialized() {
        // Act & Assert
        assertThrows(ProceduralException.class, () -> {
            terminal.initMedicalPrescriptionEdition();
        });
    }

    // ========== TESTS FOR callDecisionMakingAI ==========

    @Test
    @DisplayName("callAI - Success scenario")
    void testCallAI_Success() throws Exception {
        // Arrange
        terminal.setHealthNationalService(hnsSuccess);
        terminal.setDecisionMakingAI(aiSuccess);
        terminal.initRevision(validCip, validIllness);
        terminal.initMedicalPrescriptionEdition();

        // Act
        terminal.callDecisionMakingAI();

        // Assert
        assertTrue(terminal.isAiInitialized());
    }

    @Test
    @DisplayName("callAI - ProceduralException when prescription edition not active")
    void testCallAI_NoPrescriptionEdition() throws Exception {
        // Arrange
        terminal.setHealthNationalService(hnsSuccess);
        terminal.setDecisionMakingAI(aiSuccess);
        terminal.initRevision(validCip, validIllness);

        // Act & Assert
        assertThrows(ProceduralException.class, () -> {
            terminal.callDecisionMakingAI();
        });
    }

    @Test
    @DisplayName("callAI - AIException on AI initialization failure")
    void testCallAI_AIException() throws Exception {
        // Arrange
        DecisionMakingAIStubWithErrors aiError =
                (DecisionMakingAIStubWithErrors) aiWithErrors;
        aiError.setThrowAIException(true);

        terminal.setHealthNationalService(hnsSuccess);
        terminal.setDecisionMakingAI(aiError);
        terminal.initRevision(validCip, validIllness);
        terminal.initMedicalPrescriptionEdition();

        // Act & Assert
        assertThrows(AIException.class, () -> {
            terminal.callDecisionMakingAI();
        });
    }

    // ========== TESTS FOR askAIForSuggest ==========

    @Test
    @DisplayName("askAI - Success scenario")
    void testAskAI_Success() throws Exception {
        // Arrange
        terminal.setHealthNationalService(hnsSuccess);
        terminal.setDecisionMakingAI(aiSuccess);
        terminal.initRevision(validCip, validIllness);
        terminal.initMedicalPrescriptionEdition();
        terminal.callDecisionMakingAI();
        String prompt = "What adjustments should I make to this treatment?";

        // Act
        String response = terminal.askAIForSuggest(prompt);

        // Assert
        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertNotNull(terminal.getLastAIResponse());
    }

    @Test
    @DisplayName("askAI - ProceduralException when prescription edition not active")
    void testAskAI_NoPrescriptionEdition() throws Exception {
        // Arrange
        terminal.setHealthNationalService(hnsSuccess);
        terminal.setDecisionMakingAI(aiSuccess);
        terminal.initRevision(validCip, validIllness);

        // Act & Assert
        assertThrows(ProceduralException.class, () -> {
            terminal.askAIForSuggest("Some prompt");
        });
    }

    @Test
    @DisplayName("askAI - ProceduralException when AI not initialized")
    void testAskAI_AINotInitialized() throws Exception {
        // Arrange
        terminal.setHealthNationalService(hnsSuccess);
        terminal.setDecisionMakingAI(aiSuccess);
        terminal.initRevision(validCip, validIllness);
        terminal.initMedicalPrescriptionEdition();

        // Act & Assert
        assertThrows(ProceduralException.class, () -> {
            terminal.askAIForSuggest("Some prompt");
        });
    }

    @Test
    @DisplayName("askAI - IllegalArgumentException for null prompt")
    void testAskAI_NullPrompt() throws Exception {
        // Arrange
        terminal.setHealthNationalService(hnsSuccess);
        terminal.setDecisionMakingAI(aiSuccess);
        terminal.initRevision(validCip, validIllness);
        terminal.initMedicalPrescriptionEdition();
        terminal.callDecisionMakingAI();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            terminal.askAIForSuggest(null);
        });
    }

    @Test
    @DisplayName("askAI - IllegalArgumentException for empty prompt")
    void testAskAI_EmptyPrompt() throws Exception {
        // Arrange
        terminal.setHealthNationalService(hnsSuccess);
        terminal.setDecisionMakingAI(aiSuccess);
        terminal.initRevision(validCip, validIllness);
        terminal.initMedicalPrescriptionEdition();
        terminal.callDecisionMakingAI();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            terminal.askAIForSuggest("   ");
        });
    }

    @Test
    @DisplayName("askAI - BadPromptException for unclear prompt")
    void testAskAI_BadPromptException() throws Exception {
        // Arrange
        DecisionMakingAIStubWithErrors aiError =
                (DecisionMakingAIStubWithErrors) aiWithErrors;
        aiError.setThrowBadPromptException(true);

        terminal.setHealthNationalService(hnsSuccess);
        terminal.setDecisionMakingAI(aiError);
        terminal.initRevision(validCip, validIllness);
        terminal.initMedicalPrescriptionEdition();
        terminal.callDecisionMakingAI();

        // Act & Assert
        assertThrows(BadPromptException.class, () -> {
            terminal.askAIForSuggest("unclear prompt");
        });
    }

    // ========== TESTS FOR extractGuidelinesFromSugg ==========

    @Test
    @DisplayName("extractGuidelines - Success scenario")
    void testExtractGuidelines_Success() throws Exception {
        // Arrange
        terminal.setHealthNationalService(hnsSuccess);
        terminal.setDecisionMakingAI(aiSuccess);
        terminal.initRevision(validCip, validIllness);
        terminal.initMedicalPrescriptionEdition();
        terminal.callDecisionMakingAI();
        terminal.askAIForSuggest("Suggest adjustments");

        // Act
        List<Suggestion> suggestions = terminal.extractGuidelinesFromSugg();

        // Assert
        assertNotNull(suggestions);
        assertFalse(suggestions.isEmpty());
    }

    @Test
    @DisplayName("extractGuidelines - ProceduralException when prescription edition not active")
    void testExtractGuidelines_NoPrescriptionEdition() throws Exception {
        // Arrange
        terminal.setHealthNationalService(hnsSuccess);
        terminal.setDecisionMakingAI(aiSuccess);
        terminal.initRevision(validCip, validIllness);

        // Act & Assert
        assertThrows(ProceduralException.class, () -> {
            terminal.extractGuidelinesFromSugg();
        });
    }

    @Test
    @DisplayName("extractGuidelines - ProceduralException when AI has not responded")
    void testExtractGuidelines_NoAIResponse() throws Exception {
        // Arrange
        terminal.setHealthNationalService(hnsSuccess);
        terminal.setDecisionMakingAI(aiSuccess);
        terminal.initRevision(validCip, validIllness);
        terminal.initMedicalPrescriptionEdition();
        terminal.callDecisionMakingAI();

        // Act & Assert
        assertThrows(ProceduralException.class, () -> {
            terminal.extractGuidelinesFromSugg();
        });
    }

    // ========== TESTS FOR enterMedicineWithGuidelines ==========

    @Test
    @DisplayName("enterMedicine - Success scenario")
    void testEnterMedicine_Success() throws Exception {
        // Arrange
        terminal.setHealthNationalService(hnsSuccess);
        terminal.initRevision(validCip, validIllness);
        terminal.initMedicalPrescriptionEdition();

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> {
            terminal.enterMedicineWithGuidelines(validProductID, validGuidelines);
        });
    }

    @Test
    @DisplayName("enterMedicine - ProceduralException when prescription edition not active")
    void testEnterMedicine_NoPrescriptionEdition() throws Exception {
        // Arrange
        terminal.setHealthNationalService(hnsSuccess);
        terminal.initRevision(validCip, validIllness);

        // Act & Assert
        assertThrows(ProceduralException.class, () -> {
            terminal.enterMedicineWithGuidelines(validProductID, validGuidelines);
        });
    }

    @Test
    @DisplayName("enterMedicine - IllegalArgumentException for null ProductID")
    void testEnterMedicine_NullProductID() throws Exception {
        // Arrange
        terminal.setHealthNationalService(hnsSuccess);
        terminal.initRevision(validCip, validIllness);
        terminal.initMedicalPrescriptionEdition();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            terminal.enterMedicineWithGuidelines(null, validGuidelines);
        });
    }

    @Test
    @DisplayName("enterMedicine - IncorrectTakingGuidelinesException for null guidelines")
    void testEnterMedicine_NullGuidelines() throws Exception {
        // Arrange
        terminal.setHealthNationalService(hnsSuccess);
        terminal.initRevision(validCip, validIllness);
        terminal.initMedicalPrescriptionEdition();

        // Act & Assert
        assertThrows(IncorrectTakingGuidelinesException.class, () -> {
            terminal.enterMedicineWithGuidelines(validProductID, null);
        });
    }

    @Test
    @DisplayName("enterMedicine - IncorrectTakingGuidelinesException for empty guidelines")
    void testEnterMedicine_EmptyGuidelines() throws Exception {
        // Arrange
        terminal.setHealthNationalService(hnsSuccess);
        terminal.initRevision(validCip, validIllness);
        terminal.initMedicalPrescriptionEdition();

        // Act & Assert
        assertThrows(IncorrectTakingGuidelinesException.class, () -> {
            terminal.enterMedicineWithGuidelines(validProductID, new String[]{});
        });
    }

    @Test
    @DisplayName("enterMedicine - IncorrectTakingGuidelinesException for insufficient guidelines")
    void testEnterMedicine_InsufficientGuidelines() throws Exception {
        // Arrange
        terminal.setHealthNationalService(hnsSuccess);
        terminal.initRevision(validCip, validIllness);
        terminal.initMedicalPrescriptionEdition();
        String[] insufficientGuidelines = {"BEFORELUNCH", "15"};

        // Act & Assert
        assertThrows(IncorrectTakingGuidelinesException.class, () -> {
            terminal.enterMedicineWithGuidelines(validProductID, insufficientGuidelines);
        });
    }

    // ========== TESTS FOR modifyDoseInLine ==========

    @Test
    @DisplayName("modifyDose - Success scenario")
    void testModifyDose_Success() throws Exception {
        // Arrange
        terminal.setHealthNationalService(hnsSuccess);
        terminal.initRevision(validCip, validIllness);
        terminal.initMedicalPrescriptionEdition();
        terminal.enterMedicineWithGuidelines(validProductID, validGuidelines);

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> {
            terminal.modifyDoseInLine(validProductID, 3.0f);
        });
    }

    @Test
    @DisplayName("modifyDose - ProceduralException when prescription edition not active")
    void testModifyDose_NoPrescriptionEdition() throws Exception {
        // Arrange
        terminal.setHealthNationalService(hnsSuccess);
        terminal.initRevision(validCip, validIllness);

        // Act & Assert
        assertThrows(ProceduralException.class, () -> {
            terminal.modifyDoseInLine(validProductID, 3.0f);
        });
    }

    @Test
    @DisplayName("modifyDose - IllegalArgumentException for null ProductID")
    void testModifyDose_NullProductID() throws Exception {
        // Arrange
        terminal.setHealthNationalService(hnsSuccess);
        terminal.initRevision(validCip, validIllness);
        terminal.initMedicalPrescriptionEdition();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            terminal.modifyDoseInLine(null, 3.0f);
        });
    }

    @Test
    @DisplayName("modifyDose - IllegalArgumentException for negative dose")
    void testModifyDose_NegativeDose() throws Exception {
        // Arrange
        terminal.setHealthNationalService(hnsSuccess);
        terminal.initRevision(validCip, validIllness);
        terminal.initMedicalPrescriptionEdition();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            terminal.modifyDoseInLine(validProductID, -1.0f);
        });
    }

    @Test
    @DisplayName("modifyDose - IllegalArgumentException for zero dose")
    void testModifyDose_ZeroDose() throws Exception {
        // Arrange
        terminal.setHealthNationalService(hnsSuccess);
        terminal.initRevision(validCip, validIllness);
        terminal.initMedicalPrescriptionEdition();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            terminal.modifyDoseInLine(validProductID, 0.0f);
        });
    }

    // ========== TESTS FOR removeLine ==========

    @Test
    @DisplayName("removeLine - Success scenario")
    void testRemoveLine_Success() throws Exception {
        // Arrange
        terminal.setHealthNationalService(hnsSuccess);
        terminal.initRevision(validCip, validIllness);
        terminal.initMedicalPrescriptionEdition();
        terminal.enterMedicineWithGuidelines(validProductID, validGuidelines);

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> {
            terminal.removeLine(validProductID);
        });
    }

    @Test
    @DisplayName("removeLine - ProceduralException when prescription edition not active")
    void testRemoveLine_NoPrescriptionEdition() throws Exception {
        // Arrange
        terminal.setHealthNationalService(hnsSuccess);
        terminal.initRevision(validCip, validIllness);

        // Act & Assert
        assertThrows(ProceduralException.class, () -> {
            terminal.removeLine(validProductID);
        });
    }

    @Test
    @DisplayName("removeLine - IllegalArgumentException for null ProductID")
    void testRemoveLine_NullProductID() throws Exception {
        // Arrange
        terminal.setHealthNationalService(hnsSuccess);
        terminal.initRevision(validCip, validIllness);
        terminal.initMedicalPrescriptionEdition();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            terminal.removeLine(null);
        });
    }

    // ========== TESTS FOR enterTreatmentEndingDate ==========

    @Test
    @DisplayName("enterEndingDate - Success with valid future date")
    void testEnterEndingDate_Success() throws Exception {
        // Arrange
        terminal.setHealthNationalService(hnsSuccess);
        terminal.initRevision(validCip, validIllness);
        terminal.initMedicalPrescriptionEdition();
        Date futureDate = new Date(System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000));

        // Act
        terminal.enterTreatmentEndingDate(futureDate);

        // Assert
        assertTrue(terminal.isTreatmentDatesSet());
        assertNotNull(terminal.getCurrentPrescription().getPrescDate());
        assertNotNull(terminal.getCurrentPrescription().getEndDate());
    }

    @Test
    @DisplayName("enterEndingDate - ProceduralException when prescription edition not active")
    void testEnterEndingDate_NoPrescriptionEdition() throws Exception {
        // Arrange
        terminal.setHealthNationalService(hnsSuccess);
        terminal.initRevision(validCip, validIllness);
        Date futureDate = new Date(System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000));

        // Act & Assert
        assertThrows(ProceduralException.class, () -> {
            terminal.enterTreatmentEndingDate(futureDate);
        });
    }

    @Test
    @DisplayName("enterEndingDate - IllegalArgumentException for null date")
    void testEnterEndingDate_NullDate() throws Exception {
        // Arrange
        terminal.setHealthNationalService(hnsSuccess);
        terminal.initRevision(validCip, validIllness);
        terminal.initMedicalPrescriptionEdition();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            terminal.enterTreatmentEndingDate(null);
        });
    }

    @Test
    @DisplayName("enterEndingDate - IncorrectEndingDateException for past date")
    void testEnterEndingDate_PastDate() throws Exception {
        // Arrange
        terminal.setHealthNationalService(hnsSuccess);
        terminal.initRevision(validCip, validIllness);
        terminal.initMedicalPrescriptionEdition();
        Date pastDate = new Date(System.currentTimeMillis() - (10L * 24 * 60 * 60 * 1000));

        // Act & Assert
        assertThrows(IncorrectEndingDateException.class, () -> {
            terminal.enterTreatmentEndingDate(pastDate);
        });
    }

    @Test
    @DisplayName("enterEndingDate - IncorrectEndingDateException for current date")
    void testEnterEndingDate_CurrentDate() throws Exception {
        // Arrange
        terminal.setHealthNationalService(hnsSuccess);
        terminal.initRevision(validCip, validIllness);
        terminal.initMedicalPrescriptionEdition();
        Date currentDate = new Date();

        // Act & Assert
        assertThrows(IncorrectEndingDateException.class, () -> {
            terminal.enterTreatmentEndingDate(currentDate);
        });
    }

    @Test
    @DisplayName("enterEndingDate - IncorrectEndingDateException for too close date")
    void testEnterEndingDate_TooCloseDate() throws Exception {
        // Arrange
        terminal.setHealthNationalService(hnsSuccess);
        terminal.initRevision(validCip, validIllness);
        terminal.initMedicalPrescriptionEdition();
        Date tooCloseDate = new Date(System.currentTimeMillis() + (12L * 60 * 60 * 1000));

        // Act & Assert
        assertThrows(IncorrectEndingDateException.class, () -> {
            terminal.enterTreatmentEndingDate(tooCloseDate);
        });
    }

    // ========== TESTS FOR finishMedicalPrescriptionEdition ==========

    @Test
    @DisplayName("finishPrescriptionEdition - Success scenario")
    void testFinishPrescriptionEdition_Success() throws Exception {
        // Arrange
        terminal.setHealthNationalService(hnsSuccess);
        terminal.initRevision(validCip, validIllness);
        terminal.initMedicalPrescriptionEdition();

        // Act
        terminal.finishMedicalPrescriptionEdition();

        // Assert
        assertFalse(terminal.isPrescriptionEditionMode());
    }

    @Test
    @DisplayName("finishPrescriptionEdition - ProceduralException when edition not active")
    void testFinishPrescriptionEdition_NotActive() throws Exception {
        // Arrange
        terminal.setHealthNationalService(hnsSuccess);
        terminal.initRevision(validCip, validIllness);

        // Act & Assert
        assertThrows(ProceduralException.class, () -> {
            terminal.finishMedicalPrescriptionEdition();
        });
    }

    // ========== TESTS FOR stampeeSignature ==========

    @Test
    @DisplayName("stampSignature - Success scenario")
    void testStampSignature_Success() throws Exception {
        // Arrange
        terminal.setHealthNationalService(hnsSuccess);
        terminal.initRevision(validCip, validIllness);
        terminal.initMedicalPrescriptionEdition();
        Date futureDate = new Date(System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000));
        terminal.enterTreatmentEndingDate(futureDate);

        // Act
        terminal.stampeeSignature();

        // Assert
        assertTrue(terminal.isSignatureStamped());
        assertNotNull(terminal.getCurrentPrescription().geteSign());
    }

    @Test
    @DisplayName("stampSignature - ProceduralException when prescription edition not active")
    void testStampSignature_NoPrescriptionEdition() throws Exception {
        // Arrange
        terminal.setHealthNationalService(hnsSuccess);
        terminal.initRevision(validCip, validIllness);

        // Act & Assert
        assertThrows(ProceduralException.class, () -> {
            terminal.stampeeSignature();
        });
    }

    @Test
    @DisplayName("stampSignature - ProceduralException when dates not set")
    void testStampSignature_DatesNotSet() throws Exception {
        // Arrange
        terminal.setHealthNationalService(hnsSuccess);
        terminal.initRevision(validCip, validIllness);
        terminal.initMedicalPrescriptionEdition();

        // Act & Assert
        assertThrows(ProceduralException.class, () -> {
            terminal.stampeeSignature();
        });
    }

    // ========== TESTS FOR sendHistoryAndPrescription ==========

    @Test
    @DisplayName("sendHistoryAndPrescription - Success scenario")
    void testSendHistoryAndPrescription_Success() throws Exception {
        // Arrange
        terminal.setHealthNationalService(hnsSuccess);
        terminal.initRevision(validCip, validIllness);
        terminal.initMedicalPrescriptionEdition();
        Date futureDate = new Date(System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000));
        terminal.enterTreatmentEndingDate(futureDate);
        terminal.stampeeSignature();

        // Act
        MedicalPrescription result = terminal.sendHistoryAndPrescription();

        // Assert
        assertNotNull(result);
        assertNotNull(result.getPrescCode());
    }

    @Test
    @DisplayName("sendHistoryAndPrescription - ProceduralException when edition not active")
    void testSendHistoryAndPrescription_NoPrescriptionEdition() throws Exception {
        // Arrange
        terminal.setHealthNationalService(hnsSuccess);
        terminal.initRevision(validCip, validIllness);

        // Act & Assert
        assertThrows(ProceduralException.class, () -> {
            terminal.sendHistoryAndPrescription();
        });
    }

    @Test
    @DisplayName("sendHistoryAndPrescription - ProceduralException when dates not set")
    void testSendHistoryAndPrescription_DatesNotSet() throws Exception {
        // Arrange
        terminal.setHealthNationalService(hnsSuccess);
        terminal.initRevision(validCip, validIllness);
        terminal.initMedicalPrescriptionEdition();

        // Act & Assert
        assertThrows(ProceduralException.class, () -> {
            terminal.sendHistoryAndPrescription();
        });
    }

    @Test
    @DisplayName("sendHistoryAndPrescription - ProceduralException when signature not stamped")
    void testSendHistoryAndPrescription_SignatureNotStamped() throws Exception {
        // Arrange
        terminal.setHealthNationalService(hnsSuccess);
        terminal.initRevision(validCip, validIllness);
        terminal.initMedicalPrescriptionEdition();
        Date futureDate = new Date(System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000));
        terminal.enterTreatmentEndingDate(futureDate);

        // Act & Assert
        assertThrows(ProceduralException.class, () -> {
            terminal.sendHistoryAndPrescription();
        });
    }

    @Test
    @DisplayName("sendHistoryAndPrescription - ConnectException on network failure")
    void testSendHistoryAndPrescription_ConnectException() throws Exception {
        // Arrange
        HealthNationalServiceStubWithErrors hnsError =
                (HealthNationalServiceStubWithErrors) hnsWithErrors;
        hnsError.setThrowConnectException(true);

        terminal.setHealthNationalService(hnsError);
        terminal.initRevision(validCip, validIllness);
        terminal.initMedicalPrescriptionEdition();
        Date futureDate = new Date(System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000));
        terminal.enterTreatmentEndingDate(futureDate);
        terminal.stampeeSignature();

        // Act & Assert
        assertThrows(ConnectException.class, () -> {
            terminal.sendHistoryAndPrescription();
        });
    }

    @Test
    @DisplayName("sendHistoryAndPrescription - NotCompletedMedicalPrescriptionException")
    void testSendHistoryAndPrescription_NotCompletedException() throws Exception {
        // Arrange
        HealthNationalServiceStubWithErrors hnsError =
                (HealthNationalServiceStubWithErrors) hnsWithErrors;
        hnsError.setThrowNotCompletedPrescriptionException(true);

        terminal.setHealthNationalService(hnsError);
        terminal.initRevision(validCip, validIllness);
        terminal.initMedicalPrescriptionEdition();
        Date futureDate = new Date(System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000));
        terminal.enterTreatmentEndingDate(futureDate);
        terminal.stampeeSignature();

        // Act & Assert
        assertThrows(NotCompletedMedicalPrescriptionException.class, () -> {
            terminal.sendHistoryAndPrescription();
        });
    }

    // ========== COMPLETE USE CASE FLOW TESTS ==========

    @Test
    @DisplayName("COMPLETE FLOW - Success without AI")
    void testCompleteFlow_SuccessWithoutAI() throws Exception {
        // Arrange
        terminal.setHealthNationalService(hnsSuccess);

        // Act - Complete workflow
        terminal.initRevision(validCip, validIllness);
        terminal.enterMedicalAssessmentInHistory("Patient shows improvement");
        terminal.initMedicalPrescriptionEdition();
        terminal.enterMedicineWithGuidelines(validProductID, validGuidelines);
        terminal.modifyDoseInLine(validProductID, 2.0f);

        ProductID secondProduct = new ProductID("640557143200");
        terminal.enterMedicineWithGuidelines(secondProduct, validGuidelines);
        terminal.removeLine(secondProduct);

        Date futureDate = new Date(System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000));
        terminal.enterTreatmentEndingDate(futureDate);
        terminal.stampeeSignature();

        MedicalPrescription result = terminal.sendHistoryAndPrescription();

        // Assert
        assertNotNull(result);
        assertNotNull(result.getPrescCode());
        assertNotNull(result.geteSign());
        assertNotNull(result.getPrescDate());
        assertNotNull(result.getEndDate());
    }

    @Test
    @DisplayName("COMPLETE FLOW - Success with AI consultation")
    void testCompleteFlow_SuccessWithAI() throws Exception {
        // Arrange
        terminal.setHealthNationalService(hnsSuccess);
        terminal.setDecisionMakingAI(aiSuccess);

        // Act - Complete workflow with AI
        terminal.initRevision(validCip, validIllness);
        terminal.enterMedicalAssessmentInHistory("Patient requires treatment adjustment");
        terminal.initMedicalPrescriptionEdition();
        terminal.callDecisionMakingAI();

        String aiResponse = terminal.askAIForSuggest(
                "What adjustments should I make to this diabetes treatment?");
        List<Suggestion> suggestions = terminal.extractGuidelinesFromSugg();

        terminal.enterMedicineWithGuidelines(validProductID, validGuidelines);

        Date futureDate = new Date(System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000));
        terminal.enterTreatmentEndingDate(futureDate);
        terminal.stampeeSignature();

        MedicalPrescription result = terminal.sendHistoryAndPrescription();

        // Assert
        assertNotNull(result);
        assertNotNull(result.getPrescCode());
        assertNotNull(aiResponse);
        assertNotNull(suggestions);
        assertFalse(suggestions.isEmpty());
    }

    @Test
    @DisplayName("COMPLETE FLOW - Fail at initRevision (network error)")
    void testCompleteFlow_FailAtInitRevision() {
        // Arrange
        HealthNationalServiceStubWithErrors hnsError =
                (HealthNationalServiceStubWithErrors) hnsWithErrors;
        hnsError.setThrowConnectException(true);
        terminal.setHealthNationalService(hnsError);

        // Act & Assert
        assertThrows(ConnectException.class, () -> {
            terminal.initRevision(validCip, validIllness);
        });
    }

    @Test
    @DisplayName("COMPLETE FLOW - Fail due to missing signature")
    void testCompleteFlow_FailMissingSignature() throws Exception {
        // Arrange
        terminal.setHealthNationalService(hnsSuccess);

        terminal.initRevision(validCip, validIllness);
        terminal.enterMedicalAssessmentInHistory("Assessment");
        terminal.initMedicalPrescriptionEdition();
        terminal.enterMedicineWithGuidelines(validProductID, validGuidelines);
        Date futureDate = new Date(System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000));
        terminal.enterTreatmentEndingDate(futureDate);
        terminal.finishMedicalPrescriptionEdition();

        // Act & Assert - Missing signature
        assertThrows(ProceduralException.class, () -> {
            terminal.sendHistoryAndPrescription();
        });
    }

    @Test
    @DisplayName("COMPLETE FLOW - Fail due to invalid ending date")
    void testCompleteFlow_FailInvalidDate() throws Exception {
        // Arrange
        terminal.setHealthNationalService(hnsSuccess);

        terminal.initRevision(validCip, validIllness);
        terminal.enterMedicalAssessmentInHistory("Assessment");
        terminal.initMedicalPrescriptionEdition();
        terminal.enterMedicineWithGuidelines(validProductID, validGuidelines);

        Date pastDate = new Date(System.currentTimeMillis() - (10L * 24 * 60 * 60 * 1000));

        // Act & Assert
        assertThrows(IncorrectEndingDateException.class, () -> {
            terminal.enterTreatmentEndingDate(pastDate);
        });
    }

    @Test
    @DisplayName("COMPLETE FLOW - Fail due to bad AI prompt")
    void testCompleteFlow_FailAIBadPrompt() throws Exception {
        // Arrange
        DecisionMakingAIStubWithErrors aiError =
                (DecisionMakingAIStubWithErrors) aiWithErrors;
        aiError.setThrowBadPromptException(true);

        terminal.setHealthNationalService(hnsSuccess);
        terminal.setDecisionMakingAI(aiError);

        terminal.initRevision(validCip, validIllness);
        terminal.initMedicalPrescriptionEdition();
        terminal.callDecisionMakingAI();

        // Act & Assert
        assertThrows(BadPromptException.class, () -> {
            terminal.askAIForSuggest("unclear gibberish prompt");
        });
    }
}