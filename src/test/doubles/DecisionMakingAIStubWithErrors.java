package test.doubles;

import exceptions.*;
import medicalconsultation.Suggestion;
import services.DecisionMakingAI;

import java.util.List;

/**
 * Test Double (Stub) for DecisionMakingAI - Error scenarios.
 * Simulates various error conditions for testing exception handling.
 */
public class DecisionMakingAIStubWithErrors implements DecisionMakingAI {

    private boolean throwAIException;
    private boolean throwBadPromptException;

    public DecisionMakingAIStubWithErrors() {
        this.throwAIException = false;
        this.throwBadPromptException = false;
    }

    public void setThrowAIException(boolean value) {
        this.throwAIException = value;
    }

    public void setThrowBadPromptException(boolean value) {
        this.throwBadPromptException = value;
    }

    @Override
    public void initDecisionMakingAI() throws AIException {
        if (throwAIException) {
            throw new AIException("Simulated AI initialization failure");
        }
        // Otherwise, initialization succeeds silently
    }

    @Override
    public String getSuggestions(String prompt) throws BadPromptException {
        if (throwBadPromptException) {
            throw new BadPromptException("Simulated unclear prompt error");
        }

        // If no error, return a basic response
        return "Generic AI response";
    }

    @Override
    public List<Suggestion> parseSuggest(String aiAnswer) {
        // Return empty list for simplicity in error scenarios
        return List.of();
    }
}