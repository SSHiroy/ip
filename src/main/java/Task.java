/**
 * Provides a simple implementation for various types of tasks.
 *
 * <p>This class helps to structure and
 * manage data for a variety of tasks.</p>
 */
abstract class Task {
    private String taskName;
    private boolean status;

    public Task(String taskName) {
        this.taskName = taskName;
        this.status = false;
    }

    public boolean markDone() {
        return !this.status && (this.status = true);
    }

    public boolean markIncomplete() {
        return this.status && !(this.status = false);
    }

    @Override
    public String toString() {
        return String.format("[ %s ] ", this.status ? "X" : " ") + this.taskName;
    }
}
