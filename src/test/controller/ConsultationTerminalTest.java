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
}