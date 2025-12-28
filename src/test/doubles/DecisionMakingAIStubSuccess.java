package test.doubles;

import data.ProductID;
import exceptions.*;
import medicalconsultation.Suggestion;
import services.DecisionMakingAI;

import java.util.ArrayList;
import java.util.List;

/**
 * Test Double (Stub) for DecisionMakingAI - Success scenario.
 * Simulates successful AI interactions with predefined responses.
 */
public class DecisionMakingAIStubSuccess implements DecisionMakingAI {

    private boolean initialized;
    private String lastResponse;

    public DecisionMakingAIStubSuccess() {
        this.initialized = false;
        this.lastResponse = null;
    }

    @Override
    public void initDecisionMakingAI() throws AIException {
        // Simulate successful initialization
        this.initialized = true;
    }

    @Override
    public String getSuggestions(String prompt) throws BadPromptException {
        if (!initialized) {
            throw new IllegalStateException("AI not initialized");
        }

        // Simulate AI response based on prompt
        String response = "Based on your query, I recommend the following adjustments:\n" +
                "1. Add Paracetamol 500mg - 1 tablet every 8 hours for 7 days\n" +
                "2. Modify Ibuprofen dose to 3 tablets\n" +
                "3. Remove Aspirin from treatment";

        this.lastResponse = response;
        return response;
    }

    @Override
    public List<Suggestion> parseSuggest(String aiAnswer) {
        List<Suggestion> suggestions = new ArrayList<>();

        try {
            // Simulate parsing the AI response into structured suggestions
            // INSERT suggestion
            suggestions.add(new Suggestion(
                    Suggestion.Action.INSERT,
                    new ProductID("243516578917"),
                    new String[]{"BEFORELUNCH", "15", "1", "1", "DAY", "Tomar con abundante agua"}
            ));

            // MODIFY suggestion
            suggestions.add(new Suggestion(
                    Suggestion.Action.MODIFY,
                    new ProductID("640557143200"),
                    new String[]{"", "", "3", "", "", ""} // Only dose modified
            ));

            // ELIMINATE suggestion
            suggestions.add(new Suggestion(
                    Suggestion.Action.ELIMINATE,
                    new ProductID("789012345678"),
                    null // No guidelines for elimination
            ));
        } catch (IncorrectParametersException e) {
            // This should never happen in our test stub with valid codes
            throw new RuntimeException("Test stub configuration error: " + e.getMessage(), e);
        }

        return suggestions;
    }

    public boolean isInitialized() {
        return initialized;
    }
}