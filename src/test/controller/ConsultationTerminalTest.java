package test.controller;

import data.*;
import exceptions.*;
import medicalconsultation.*;
import services.*;
import test.doubles.*;

import java.net.ConnectException;
import java.util.Date;
import java.util.List;

/**
 * Unit tests for ConsultationTerminal class.
 * Tests all input events and workflow scenarios using test doubles.
 */
public class ConsultationTerminalTest {

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
    public void setUp() {
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

    /**
     * Utility method to print test results.
     */
    private void printTestResult(String testName, boolean passed) {
        String result = passed ? "✓ PASSED" : "✗ FAILED";
        System.out.println(result + " - " + testName);
    }

    /**
     * Utility method to print test separator.
     */
    private void printTestSeparator() {
        System.out.println("\n" + "=".repeat(70) + "\n");
    }


    // ========== TESTS FOR initRevision ==========

    /**
     * Test: initRevision with valid parameters should succeed.
     * Should download medical history and prescription from HNS.
     */
    public void testInitRevision_Success() {
        setUp();
        boolean passed = false;

        try {
            // Arrange
            terminal.setHealthNationalService(hnsSuccess);

            // Act
            terminal.initRevision(validCip, validIllness);

            // Assert
            if (terminal.isRevisionInitialized() &&
                    terminal.getCurrentMedicalHistory() != null &&
                    terminal.getCurrentPrescription() != null &&
                    terminal.getCurrentIllness().equals(validIllness)) {
                passed = true;
            }

        } catch (Exception e) {
            System.out.println("Unexpected exception: " + e.getMessage());
        }

        printTestResult("initRevision - Success scenario", passed);
    }

    /**
     * Test: initRevision with null CIP should throw IllegalArgumentException.
     */
    public void testInitRevision_NullCIP() {
        setUp();
        boolean passed = false;

        try {
            // Arrange
            terminal.setHealthNationalService(hnsSuccess);

            // Act
            terminal.initRevision(null, validIllness);

            // Should not reach here

        } catch (IllegalArgumentException e) {
            passed = true; // Expected exception
        } catch (Exception e) {
            System.out.println("Wrong exception type: " + e.getClass().getName());
        }

        printTestResult("initRevision - Null CIP throws IllegalArgumentException", passed);
    }

    /**
     * Test: initRevision with null illness should throw IllegalArgumentException.
     */
    public void testInitRevision_NullIllness() {
        setUp();
        boolean passed = false;

        try {
            // Arrange
            terminal.setHealthNationalService(hnsSuccess);

            // Act
            terminal.initRevision(validCip, null);

        } catch (IllegalArgumentException e) {
            passed = true; // Expected exception
        } catch (Exception e) {
            System.out.println("Wrong exception type: " + e.getClass().getName());
        }

        printTestResult("initRevision - Null illness throws IllegalArgumentException", passed);
    }

    /**
     * Test: initRevision with empty illness should throw IllegalArgumentException.
     */
    public void testInitRevision_EmptyIllness() {
        setUp();
        boolean passed = false;

        try {
            // Arrange
            terminal.setHealthNationalService(hnsSuccess);

            // Act
            terminal.initRevision(validCip, "   ");

        } catch (IllegalArgumentException e) {
            passed = true; // Expected exception
        } catch (Exception e) {
            System.out.println("Wrong exception type: " + e.getClass().getName());
        }

        printTestResult("initRevision - Empty illness throws IllegalArgumentException", passed);
    }

    /**
     * Test: initRevision with network error should throw ConnectException.
     */
    public void testInitRevision_ConnectException() {
        setUp();
        boolean passed = false;

        try {
            // Arrange
            HealthNationalServiceStubWithErrors hnsError =
                    (HealthNationalServiceStubWithErrors) hnsWithErrors;
            hnsError.setThrowConnectException(true);
            terminal.setHealthNationalService(hnsError);

            // Act
            terminal.initRevision(validCip, validIllness);

        } catch (ConnectException e) {
            passed = true; // Expected exception
        } catch (Exception e) {
            System.out.println("Wrong exception type: " + e.getClass().getName());
        }

        printTestResult("initRevision - ConnectException on network failure", passed);
    }

    /**
     * Test: initRevision with unregistered patient should throw HealthCardIDException.
     */
    public void testInitRevision_HealthCardIDException() {
        setUp();
        boolean passed = false;

        try {
            // Arrange
            HealthNationalServiceStubWithErrors hnsError =
                    (HealthNationalServiceStubWithErrors) hnsWithErrors;
            hnsError.setThrowHealthCardIDException(true);
            terminal.setHealthNationalService(hnsError);

            // Act
            terminal.initRevision(validCip, validIllness);

        } catch (HealthCardIDException e) {
            passed = true; // Expected exception
        } catch (Exception e) {
            System.out.println("Wrong exception type: " + e.getClass().getName());
        }

        printTestResult("initRevision - HealthCardIDException for unregistered patient", passed);
    }

    /**
     * Test: initRevision with no prescription should throw AnyCurrentPrescriptionException.
     */
    public void testInitRevision_AnyCurrentPrescriptionException() {
        setUp();
        boolean passed = false;

        try {
            // Arrange
            HealthNationalServiceStubWithErrors hnsError =
                    (HealthNationalServiceStubWithErrors) hnsWithErrors;
            hnsError.setThrowAnyCurrentPrescriptionException(true);
            terminal.setHealthNationalService(hnsError);

            // Act
            terminal.initRevision(validCip, validIllness);

        } catch (AnyCurrentPrescriptionException e) {
            passed = true; // Expected exception
        } catch (Exception e) {
            System.out.println("Wrong exception type: " + e.getClass().getName());
        }

        printTestResult("initRevision - AnyCurrentPrescriptionException for missing prescription", passed);
    }

    // ========== TESTS FOR enterMedicalAssessmentInHistory ==========

    /**
     * Test: enterMedicalAssessmentInHistory with valid assessment should succeed.
     */
    public void testEnterAssessment_Success() {
        setUp();
        boolean passed = false;

        try {
            // Arrange
            terminal.setHealthNationalService(hnsSuccess);
            terminal.initRevision(validCip, validIllness);
            String assessment = "Patient shows improvement in glucose levels";

            // Act
            terminal.enterMedicalAssessmentInHistory(assessment);

            // Assert
            String history = terminal.getCurrentMedicalHistory().getHistory();
            if (history.contains(assessment)) {
                passed = true;
            }

        } catch (Exception e) {
            System.out.println("Unexpected exception: " + e.getMessage());
        }

        printTestResult("enterAssessment - Success scenario", passed);
    }

    /**
     * Test: enterAssessment without initialized revision should throw ProceduralException.
     */
    public void testEnterAssessment_NoRevisionInitialized() {
        setUp();
        boolean passed = false;

        try {
            // Arrange - NO initRevision called
            String assessment = "Some assessment";

            // Act
            terminal.enterMedicalAssessmentInHistory(assessment);

        } catch (ProceduralException e) {
            passed = true; // Expected exception
        } catch (Exception e) {
            System.out.println("Wrong exception type: " + e.getClass().getName());
        }

        printTestResult("enterAssessment - ProceduralException when revision not initialized", passed);
    }

    /**
     * Test: enterAssessment with null should throw IllegalArgumentException.
     */
    public void testEnterAssessment_NullAssessment() {
        setUp();
        boolean passed = false;

        try {
            // Arrange
            terminal.setHealthNationalService(hnsSuccess);
            terminal.initRevision(validCip, validIllness);

            // Act
            terminal.enterMedicalAssessmentInHistory(null);

        } catch (IllegalArgumentException e) {
            passed = true; // Expected exception
        } catch (Exception e) {
            System.out.println("Wrong exception type: " + e.getClass().getName());
        }

        printTestResult("enterAssessment - IllegalArgumentException for null assessment", passed);
    }

    /**
     * Test: enterAssessment with empty string should throw IllegalArgumentException.
     */
    public void testEnterAssessment_EmptyAssessment() {
        setUp();
        boolean passed = false;

        try {
            // Arrange
            terminal.setHealthNationalService(hnsSuccess);
            terminal.initRevision(validCip, validIllness);

            // Act
            terminal.enterMedicalAssessmentInHistory("   ");

        } catch (IllegalArgumentException e) {
            passed = true; // Expected exception
        } catch (Exception e) {
            System.out.println("Wrong exception type: " + e.getClass().getName());
        }

        printTestResult("enterAssessment - IllegalArgumentException for empty assessment", passed);
    }

    // ========== TESTS FOR initMedicalPrescriptionEdition ==========

    /**
     * Test: initMedicalPrescriptionEdition should activate edition mode.
     */
    public void testInitPrescriptionEdition_Success() {
        setUp();
        boolean passed = false;

        try {
            // Arrange
            terminal.setHealthNationalService(hnsSuccess);
            terminal.initRevision(validCip, validIllness);

            // Act
            terminal.initMedicalPrescriptionEdition();

            // Assert
            if (terminal.isPrescriptionEditionMode()) {
                passed = true;
            }

        } catch (Exception e) {
            System.out.println("Unexpected exception: " + e.getMessage());
        }

        printTestResult("initPrescriptionEdition - Success scenario", passed);
    }

    /**
     * Test: initPrescriptionEdition without revision should throw ProceduralException.
     */
    public void testInitPrescriptionEdition_NoRevisionInitialized() {
        setUp();
        boolean passed = false;

        try {
            // Arrange - NO initRevision called

            // Act
            terminal.initMedicalPrescriptionEdition();

        } catch (ProceduralException e) {
            passed = true; // Expected exception
        } catch (Exception e) {
            System.out.println("Wrong exception type: " + e.getClass().getName());
        }

        printTestResult("initPrescriptionEdition - ProceduralException when revision not initialized", passed);
    }


    // ========== TESTS FOR callDecisionMakingAI ==========

    /**
     * Test: callDecisionMakingAI should successfully initialize AI.
     */
    public void testCallAI_Success() {
        setUp();
        boolean passed = false;

        try {
            // Arrange
            terminal.setHealthNationalService(hnsSuccess);
            terminal.setDecisionMakingAI(aiSuccess);
            terminal.initRevision(validCip, validIllness);
            terminal.initMedicalPrescriptionEdition();

            // Act
            terminal.callDecisionMakingAI();

            // Assert
            if (terminal.isAiInitialized()) {
                passed = true;
            }

        } catch (Exception e) {
            System.out.println("Unexpected exception: " + e.getMessage());
        }

        printTestResult("callAI - Success scenario", passed);
    }

    /**
     * Test: callAI without prescription edition should throw ProceduralException.
     */
    public void testCallAI_NoPrescriptionEdition() {
        setUp();
        boolean passed = false;

        try {
            // Arrange
            terminal.setHealthNationalService(hnsSuccess);
            terminal.setDecisionMakingAI(aiSuccess);
            terminal.initRevision(validCip, validIllness);
            // NO initMedicalPrescriptionEdition called

            // Act
            terminal.callDecisionMakingAI();

        } catch (ProceduralException e) {
            passed = true; // Expected exception
        } catch (Exception e) {
            System.out.println("Wrong exception type: " + e.getClass().getName());
        }

        printTestResult("callAI - ProceduralException when prescription edition not active", passed);
    }

    /**
     * Test: callAI with AI error should throw AIException.
     */
    public void testCallAI_AIException() {
        setUp();
        boolean passed = false;

        try {
            // Arrange
            DecisionMakingAIStubWithErrors aiError =
                    (DecisionMakingAIStubWithErrors) aiWithErrors;
            aiError.setThrowAIException(true);

            terminal.setHealthNationalService(hnsSuccess);
            terminal.setDecisionMakingAI(aiError);
            terminal.initRevision(validCip, validIllness);
            terminal.initMedicalPrescriptionEdition();

            // Act
            terminal.callDecisionMakingAI();

        } catch (AIException e) {
            passed = true; // Expected exception
        } catch (Exception e) {
            System.out.println("Wrong exception type: " + e.getClass().getName());
        }

        printTestResult("callAI - AIException on AI initialization failure", passed);
    }


    // ========== TESTS FOR askAIForSuggest ==========

    /**
     * Test: askAIForSuggest should return AI response.
     */
    public void testAskAI_Success() {
        setUp();
        boolean passed = false;

        try {
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
            if (response != null && !response.isEmpty() &&
                    terminal.getLastAIResponse() != null) {
                passed = true;
            }

        } catch (Exception e) {
            System.out.println("Unexpected exception: " + e.getMessage());
        }

        printTestResult("askAI - Success scenario", passed);
    }

    /**
     * Test: askAI without prescription edition should throw ProceduralException.
     */
    public void testAskAI_NoPrescriptionEdition() {
        setUp();
        boolean passed = false;

        try {
            // Arrange
            terminal.setHealthNationalService(hnsSuccess);
            terminal.setDecisionMakingAI(aiSuccess);
            terminal.initRevision(validCip, validIllness);
            // NO initMedicalPrescriptionEdition

            // Act
            terminal.askAIForSuggest("Some prompt");

        } catch (ProceduralException e) {
            passed = true; // Expected exception
        } catch (Exception e) {
            System.out.println("Wrong exception type: " + e.getClass().getName());
        }

        printTestResult("askAI - ProceduralException when prescription edition not active", passed);
    }

    /**
     * Test: askAI without AI initialized should throw ProceduralException.
     */
    public void testAskAI_AINotInitialized() {
        setUp();
        boolean passed = false;

        try {
            // Arrange
            terminal.setHealthNationalService(hnsSuccess);
            terminal.setDecisionMakingAI(aiSuccess);
            terminal.initRevision(validCip, validIllness);
            terminal.initMedicalPrescriptionEdition();
            // NO callDecisionMakingAI

            // Act
            terminal.askAIForSuggest("Some prompt");

        } catch (ProceduralException e) {
            passed = true; // Expected exception
        } catch (Exception e) {
            System.out.println("Wrong exception type: " + e.getClass().getName());
        }

        printTestResult("askAI - ProceduralException when AI not initialized", passed);
    }

    /**
     * Test: askAI with null prompt should throw IllegalArgumentException.
     */
    public void testAskAI_NullPrompt() {
        setUp();
        boolean passed = false;

        try {
            // Arrange
            terminal.setHealthNationalService(hnsSuccess);
            terminal.setDecisionMakingAI(aiSuccess);
            terminal.initRevision(validCip, validIllness);
            terminal.initMedicalPrescriptionEdition();
            terminal.callDecisionMakingAI();

            // Act
            terminal.askAIForSuggest(null);

        } catch (IllegalArgumentException e) {
            passed = true; // Expected exception
        } catch (Exception e) {
            System.out.println("Wrong exception type: " + e.getClass().getName());
        }

        printTestResult("askAI - IllegalArgumentException for null prompt", passed);
    }

    /**
     * Test: askAI with empty prompt should throw IllegalArgumentException.
     */
    public void testAskAI_EmptyPrompt() {
        setUp();
        boolean passed = false;

        try {
            // Arrange
            terminal.setHealthNationalService(hnsSuccess);
            terminal.setDecisionMakingAI(aiSuccess);
            terminal.initRevision(validCip, validIllness);
            terminal.initMedicalPrescriptionEdition();
            terminal.callDecisionMakingAI();

            // Act
            terminal.askAIForSuggest("   ");

        } catch (IllegalArgumentException e) {
            passed = true; // Expected exception
        } catch (Exception e) {
            System.out.println("Wrong exception type: " + e.getClass().getName());
        }

        printTestResult("askAI - IllegalArgumentException for empty prompt", passed);
    }

    /**
     * Test: askAI with bad prompt should throw BadPromptException.
     */
    public void testAskAI_BadPromptException() {
        setUp();
        boolean passed = false;

        try {
            // Arrange
            DecisionMakingAIStubWithErrors aiError =
                    (DecisionMakingAIStubWithErrors) aiWithErrors;
            aiError.setThrowBadPromptException(true);

            terminal.setHealthNationalService(hnsSuccess);
            terminal.setDecisionMakingAI(aiError);
            terminal.initRevision(validCip, validIllness);
            terminal.initMedicalPrescriptionEdition();
            terminal.callDecisionMakingAI();

            // Act
            terminal.askAIForSuggest("unclear prompt");

        } catch (BadPromptException e) {
            passed = true; // Expected exception
        } catch (Exception e) {
            System.out.println("Wrong exception type: " + e.getClass().getName());
        }

        printTestResult("askAI - BadPromptException for unclear prompt", passed);
    }


    // ========== TESTS FOR extractGuidelinesFromSugg ==========

    /**
     * Test: extractGuidelines should parse AI response into suggestions.
     */
    public void testExtractGuidelines_Success() {
        setUp();
        boolean passed = false;

        try {
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
            if (suggestions != null && !suggestions.isEmpty()) {
                passed = true;
            }

        } catch (Exception e) {
            System.out.println("Unexpected exception: " + e.getMessage());
        }

        printTestResult("extractGuidelines - Success scenario", passed);
    }

    /**
     * Test: extractGuidelines without prescription edition should throw ProceduralException.
     */
    public void testExtractGuidelines_NoPrescriptionEdition() {
        setUp();
        boolean passed = false;

        try {
            // Arrange
            terminal.setHealthNationalService(hnsSuccess);
            terminal.setDecisionMakingAI(aiSuccess);
            terminal.initRevision(validCip, validIllness);
            // NO initMedicalPrescriptionEdition

            // Act
            terminal.extractGuidelinesFromSugg();

        } catch (ProceduralException e) {
            passed = true; // Expected exception
        } catch (Exception e) {
            System.out.println("Wrong exception type: " + e.getClass().getName());
        }

        printTestResult("extractGuidelines - ProceduralException when prescription edition not active", passed);
    }

    /**
     * Test: extractGuidelines without AI response should throw ProceduralException.
     */
    public void testExtractGuidelines_NoAIResponse() {
        setUp();
        boolean passed = false;

        try {
            // Arrange
            terminal.setHealthNationalService(hnsSuccess);
            terminal.setDecisionMakingAI(aiSuccess);
            terminal.initRevision(validCip, validIllness);
            terminal.initMedicalPrescriptionEdition();
            terminal.callDecisionMakingAI();
            // NO askAIForSuggest called

            // Act
            terminal.extractGuidelinesFromSugg();

        } catch (ProceduralException e) {
            passed = true; // Expected exception
        } catch (Exception e) {
            System.out.println("Wrong exception type: " + e.getClass().getName());
        }

        printTestResult("extractGuidelines - ProceduralException when AI has not responded", passed);
    }


    // ========== TESTS FOR enterMedicineWithGuidelines ==========

    /**
     * Test: enterMedicine with valid data should add prescription line.
     */
    public void testEnterMedicine_Success() {
        setUp();
        boolean passed = false;

        try {
            // Arrange
            terminal.setHealthNationalService(hnsSuccess);
            terminal.initRevision(validCip, validIllness);
            terminal.initMedicalPrescriptionEdition();

            // Act
            terminal.enterMedicineWithGuidelines(validProductID, validGuidelines);

            // Assert - should not throw exception
            passed = true;

        } catch (Exception e) {
            System.out.println("Unexpected exception: " + e.getMessage());
        }

        printTestResult("enterMedicine - Success scenario", passed);
    }

    /**
     * Test: enterMedicine without prescription edition should throw ProceduralException.
     */
    public void testEnterMedicine_NoPrescriptionEdition() {
        setUp();
        boolean passed = false;

        try {
            // Arrange
            terminal.setHealthNationalService(hnsSuccess);
            terminal.initRevision(validCip, validIllness);
            // NO initMedicalPrescriptionEdition

            // Act
            terminal.enterMedicineWithGuidelines(validProductID, validGuidelines);

        } catch (ProceduralException e) {
            passed = true; // Expected exception
        } catch (Exception e) {
            System.out.println("Wrong exception type: " + e.getClass().getName());
        }

        printTestResult("enterMedicine - ProceduralException when prescription edition not active", passed);
    }

    /**
     * Test: enterMedicine with null ProductID should throw IllegalArgumentException.
     */
    public void testEnterMedicine_NullProductID() {
        setUp();
        boolean passed = false;

        try {
            // Arrange
            terminal.setHealthNationalService(hnsSuccess);
            terminal.initRevision(validCip, validIllness);
            terminal.initMedicalPrescriptionEdition();

            // Act
            terminal.enterMedicineWithGuidelines(null, validGuidelines);

        } catch (IllegalArgumentException e) {
            passed = true; // Expected exception
        } catch (Exception e) {
            System.out.println("Wrong exception type: " + e.getClass().getName());
        }

        printTestResult("enterMedicine - IllegalArgumentException for null ProductID", passed);
    }

    /**
     * Test: enterMedicine with null guidelines should throw IncorrectTakingGuidelinesException.
     */
    public void testEnterMedicine_NullGuidelines() {
        setUp();
        boolean passed = false;

        try {
            // Arrange
            terminal.setHealthNationalService(hnsSuccess);
            terminal.initRevision(validCip, validIllness);
            terminal.initMedicalPrescriptionEdition();

            // Act
            terminal.enterMedicineWithGuidelines(validProductID, null);

        } catch (IncorrectTakingGuidelinesException e) {
            passed = true; // Expected exception
        } catch (Exception e) {
            System.out.println("Wrong exception type: " + e.getClass().getName());
        }

        printTestResult("enterMedicine - IncorrectTakingGuidelinesException for null guidelines", passed);
    }

    /**
     * Test: enterMedicine with empty guidelines should throw IncorrectTakingGuidelinesException.
     */
    public void testEnterMedicine_EmptyGuidelines() {
        setUp();
        boolean passed = false;

        try {
            // Arrange
            terminal.setHealthNationalService(hnsSuccess);
            terminal.initRevision(validCip, validIllness);
            terminal.initMedicalPrescriptionEdition();

            // Act
            terminal.enterMedicineWithGuidelines(validProductID, new String[]{});

        } catch (IncorrectTakingGuidelinesException e) {
            passed = true; // Expected exception
        } catch (Exception e) {
            System.out.println("Wrong exception type: " + e.getClass().getName());
        }

        printTestResult("enterMedicine - IncorrectTakingGuidelinesException for empty guidelines", passed);
    }

    /**
     * Test: enterMedicine with insufficient guidelines should throw IncorrectTakingGuidelinesException.
     */
    public void testEnterMedicine_InsufficientGuidelines() {
        setUp();
        boolean passed = false;

        try {
            // Arrange
            terminal.setHealthNationalService(hnsSuccess);
            terminal.initRevision(validCip, validIllness);
            terminal.initMedicalPrescriptionEdition();
            String[] insufficientGuidelines = {"BEFORELUNCH", "15"}; // Only 2 elements, need at least 5

            // Act
            terminal.enterMedicineWithGuidelines(validProductID, insufficientGuidelines);

        } catch (IncorrectTakingGuidelinesException e) {
            passed = true; // Expected exception
        } catch (Exception e) {
            System.out.println("Wrong exception type: " + e.getClass().getName());
        }

        printTestResult("enterMedicine - IncorrectTakingGuidelinesException for insufficient guidelines", passed);
    }


    // ========== TESTS FOR modifyDoseInLine ==========

    /**
     * Test: modifyDose with valid data should succeed.
     */
    public void testModifyDose_Success() {
        setUp();
        boolean passed = false;

        try {
            // Arrange
            terminal.setHealthNationalService(hnsSuccess);
            terminal.initRevision(validCip, validIllness);
            terminal.initMedicalPrescriptionEdition();
            terminal.enterMedicineWithGuidelines(validProductID, validGuidelines);

            // Act
            terminal.modifyDoseInLine(validProductID, 3.0f);

            // Assert - should not throw exception
            passed = true;

        } catch (Exception e) {
            System.out.println("Unexpected exception: " + e.getMessage());
        }

        printTestResult("modifyDose - Success scenario", passed);
    }

    /**
     * Test: modifyDose without prescription edition should throw ProceduralException.
     */
    public void testModifyDose_NoPrescriptionEdition() {
        setUp();
        boolean passed = false;

        try {
            // Arrange
            terminal.setHealthNationalService(hnsSuccess);
            terminal.initRevision(validCip, validIllness);
            // NO initMedicalPrescriptionEdition

            // Act
            terminal.modifyDoseInLine(validProductID, 3.0f);

        } catch (ProceduralException e) {
            passed = true; // Expected exception
        } catch (Exception e) {
            System.out.println("Wrong exception type: " + e.getClass().getName());
        }

        printTestResult("modifyDose - ProceduralException when prescription edition not active", passed);
    }

    /**
     * Test: modifyDose with null ProductID should throw IllegalArgumentException.
     */
    public void testModifyDose_NullProductID() {
        setUp();
        boolean passed = false;

        try {
            // Arrange
            terminal.setHealthNationalService(hnsSuccess);
            terminal.initRevision(validCip, validIllness);
            terminal.initMedicalPrescriptionEdition();

            // Act
            terminal.modifyDoseInLine(null, 3.0f);

        } catch (IllegalArgumentException e) {
            passed = true; // Expected exception
        } catch (Exception e) {
            System.out.println("Wrong exception type: " + e.getClass().getName());
        }

        printTestResult("modifyDose - IllegalArgumentException for null ProductID", passed);
    }

    /**
     * Test: modifyDose with negative dose should throw IllegalArgumentException.
     */
    public void testModifyDose_NegativeDose() {
        setUp();
        boolean passed = false;

        try {
            // Arrange
            terminal.setHealthNationalService(hnsSuccess);
            terminal.initRevision(validCip, validIllness);
            terminal.initMedicalPrescriptionEdition();

            // Act
            terminal.modifyDoseInLine(validProductID, -1.0f);

        } catch (IllegalArgumentException e) {
            passed = true; // Expected exception
        } catch (Exception e) {
            System.out.println("Wrong exception type: " + e.getClass().getName());
        }

        printTestResult("modifyDose - IllegalArgumentException for negative dose", passed);
    }

    /**
     * Test: modifyDose with zero dose should throw IllegalArgumentException.
     */
    public void testModifyDose_ZeroDose() {
        setUp();
        boolean passed = false;

        try {
            // Arrange
            terminal.setHealthNationalService(hnsSuccess);
            terminal.initRevision(validCip, validIllness);
            terminal.initMedicalPrescriptionEdition();

            // Act
            terminal.modifyDoseInLine(validProductID, 0.0f);

        } catch (IllegalArgumentException e) {
            passed = true; // Expected exception
        } catch (Exception e) {
            System.out.println("Wrong exception type: " + e.getClass().getName());
        }

        printTestResult("modifyDose - IllegalArgumentException for zero dose", passed);
    }


    // ========== TESTS FOR removeLine ==========

    /**
     * Test: removeLine with valid ProductID should succeed.
     */
    public void testRemoveLine_Success() {
        setUp();
        boolean passed = false;

        try {
            // Arrange
            terminal.setHealthNationalService(hnsSuccess);
            terminal.initRevision(validCip, validIllness);
            terminal.initMedicalPrescriptionEdition();
            terminal.enterMedicineWithGuidelines(validProductID, validGuidelines);

            // Act
            terminal.removeLine(validProductID);

            // Assert - should not throw exception
            passed = true;

        } catch (Exception e) {
            System.out.println("Unexpected exception: " + e.getMessage());
        }

        printTestResult("removeLine - Success scenario", passed);
    }

    /**
     * Test: removeLine without prescription edition should throw ProceduralException.
     */
    public void testRemoveLine_NoPrescriptionEdition() {
        setUp();
        boolean passed = false;

        try {
            // Arrange
            terminal.setHealthNationalService(hnsSuccess);
            terminal.initRevision(validCip, validIllness);
            // NO initMedicalPrescriptionEdition

            // Act
            terminal.removeLine(validProductID);

        } catch (ProceduralException e) {
            passed = true; // Expected exception
        } catch (Exception e) {
            System.out.println("Wrong exception type: " + e.getClass().getName());
        }

        printTestResult("removeLine - ProceduralException when prescription edition not active", passed);
    }

    /**
     * Test: removeLine with null ProductID should throw IllegalArgumentException.
     */
    public void testRemoveLine_NullProductID() {
        setUp();
        boolean passed = false;

        try {
            // Arrange
            terminal.setHealthNationalService(hnsSuccess);
            terminal.initRevision(validCip, validIllness);
            terminal.initMedicalPrescriptionEdition();

            // Act
            terminal.removeLine(null);

        } catch (IllegalArgumentException e) {
            passed = true; // Expected exception
        } catch (Exception e) {
            System.out.println("Wrong exception type: " + e.getClass().getName());
        }

        printTestResult("removeLine - IllegalArgumentException for null ProductID", passed);
    }


    // ========== MAIN METHOD TO RUN ALL TESTS ==========

    public static void main(String[] args) {
        ConsultationTerminalTest test = new ConsultationTerminalTest();

        System.out.println("\n" + "=".repeat(70));
        System.out.println("RUNNING CONSULTATION TERMINAL UNIT TESTS");
        System.out.println("=".repeat(70) + "\n");

        // Tests for initRevision
        System.out.println(">>> Testing initRevision()");
        test.testInitRevision_Success();
        test.testInitRevision_NullCIP();
        test.testInitRevision_NullIllness();
        test.testInitRevision_EmptyIllness();
        test.testInitRevision_ConnectException();
        test.testInitRevision_HealthCardIDException();
        test.testInitRevision_AnyCurrentPrescriptionException();
        test.printTestSeparator();

        // Tests for enterMedicalAssessmentInHistory
        System.out.println(">>> Testing enterMedicalAssessmentInHistory()");
        test.testEnterAssessment_Success();
        test.testEnterAssessment_NoRevisionInitialized();
        test.testEnterAssessment_NullAssessment();
        test.testEnterAssessment_EmptyAssessment();
        test.printTestSeparator();

        // Tests for initMedicalPrescriptionEdition
        System.out.println(">>> Testing initMedicalPrescriptionEdition()");
        test.testInitPrescriptionEdition_Success();
        test.testInitPrescriptionEdition_NoRevisionInitialized();
        test.printTestSeparator();

        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEST EXECUTION COMPLETED");
        System.out.println("=".repeat(70) + "\n");
    }
}