package katty;

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

    public static KattyException noTaskFound() {
        return new KattyException("The number is not a valid index in the task list.");
    }

    public static KattyException invalidTask() {
        return new KattyException("The task was not constructed using the correct syntax.");
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
}
