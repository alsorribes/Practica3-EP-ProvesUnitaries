package exceptions;

/**
 * Exception thrown when a use case operation is called without meeting its preconditions.
 * This exception handles all procedural violations in the workflow sequence.
 *
 * Examples:
 * - Trying to edit a prescription before initializing a revision
 * - Trying to stamp signature before setting the ending date
 * - Calling AI operations before initializing the AI system
 */
public class ProceduralException extends Exception {

  public ProceduralException() {
    super("Procedural error: preconditions not met for this operation");
  }

  public ProceduralException(String message) {
    super(message);
  }
}