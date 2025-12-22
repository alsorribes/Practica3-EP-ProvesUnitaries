package services;

import exceptions.AIException;
import exceptions.BadPromptException;
import medicalconsultation.Suggestion;
import java.util.List;

/**
 * Interface for the Decision Making AI service that provides medical treatment suggestions.
 * The AI assists doctors in adjusting patient treatments based on medical data.
 */
public interface DecisionMakingAI {

    /**
     * Initializes the Decision Making AI system.
     * Must be called before requesting any suggestions.
     *
     * @throws AIException if there's any problem starting the AI system
     */
    void initDecisionMakingAI() throws AIException;

    /**
     * Sends a prompt to the AI and retrieves suggestions in text format.
     *
     * @param prompt the question or request sent to the AI by the doctor
     * @return the AI's response as a text string to be displayed
     * @throws BadPromptException if the prompt is unclear or inconsistent
     */
    String getSuggestions(String prompt) throws BadPromptException;

    /**
     * Parses the AI's text response into structured suggestions following a specific pattern.
     * Each suggestion represents an action on a medication line:
     * - I (Insert): Add new medication with complete guidelines
     * - E (Eliminate): Remove a medication (no guidelines needed)
     * - M (Modify): Update specific guidelines (only changed fields provided)
     *
     * Pattern format: <ACTION, ProductID, [guidelines...]>
     * @param aiAnswer the AI's text response to parse
     * @return a list of structured Suggestion objects
     */
    List<Suggestion> parseSuggest(String aiAnswer);
}