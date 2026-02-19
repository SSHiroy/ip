package katty;

import katty.task.Deadline;
import katty.task.Event;

/**
 * Represents custom exceptions used in Katty,
 * capable of providing messages that are more relevant to the dev.
 *
 * <p>This class extends {@code Exception} and provides static methods to
 *  create specific exception messages for common error conditions.</p>
 */
public class KattyException extends Exception {
    public KattyException(String exception) {
        super(exception);
    }

    public static KattyException invalidTodo() {
        return new KattyException("Format: todo [description]");
    }

    public static KattyException invalidDeadline() {
        return new KattyException(String.format("Format: deadline [description] /by %s", Deadline.DEADLINE_FORMAT));
    }

    /**
     * Creates an exception indicating that an "event" command was entered with an invalid format.
     * * <p>The message includes the expected usage pattern, dynamically pulling the
     * required date-time format from the {@code Event} class constants.</p>
     *
     * @return A {@code KattyException} containing the correct format for an event task.
     */
    public static KattyException invalidEvent() {
        return new KattyException(String.format("Format: event [description] /from %s /to %s",
                Event.EVENT_FORMAT, Event.EVENT_FORMAT));
    }

    public static KattyException noTaskFound() {
        return new KattyException("The number is not a valid index in the task list.");
    }

    public static KattyException searchResultEmpty() {
        return new KattyException("Meow! I couldn't find any tasks matching that keyword.");
    }

    public static KattyException invalidCommand() {
        return new KattyException("Invalid command entered.");
    }

    /**
     * Creates an exception indicating a completed task cannot be completed and
     * an incomplete task cannot be unmarked for completion.
     *
     * @return {@code KattyException}
     */
    public static KattyException invalidCompletion() {
        return new KattyException("Incomplete tasks can only be marked as complete, "
                                    + "complete tasks can only be marked as incomplete.");
    }

    public static KattyException emptyInputException() {
        return new KattyException("User input is empty.");
    }

    public static KattyException failToSave() {
        return new KattyException("Save file could not be written to disk.");
    }

    public static KattyException noSaveFile() {
        return new KattyException("No valid save file was found.");
    }

    public static KattyException corruptFile() {
        return new KattyException("Save file is corrupted.");
    }

    public static KattyException badDateFormat() {
        return new KattyException("The date given is either invalid or not in a valid format as dd-MM-yyyy HH:mm.");
    }

    public static KattyException invalidTimeRange() {
        return new KattyException("Meow! Your event can't end before it starts. Are you trying to time travel?");
    }
}
