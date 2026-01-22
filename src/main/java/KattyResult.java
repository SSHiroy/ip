/**
 * A utility class to help communicate the result of operations.
 *
 * <p>This class helps to communicate the result of
 * operations back to Katty for dispatch.</p>
 */
public class KattyResult {
    private final boolean success;
    private final String message;
    private final String data;
    private final Exception exception;

    /**
     * A constructor for storing the result.
     *
     * @param success A boolean value for whether operation was successful
     * @param message A message to convey back to the user
     * @param data The data that was relevant to the operation
     * @param exception The relevant exception in the event of failure.
     */
    public KattyResult(boolean success, String message, String data, Exception exception) {
        this.success = success;
        this.message = message != null ? message : "";
        this.data = data != null ? data : "";
        this.exception = exception;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getData() {
        return data;
    }

    public String getException() {
        return exception == null ? "" : "Developer's message: " + exception;
    }
}
