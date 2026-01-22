/**
 * Provides a simple implementation for tasks.
 *
 * <p>This class helps to structure and
 * manage data for tasks.</p>
 */
class Task {
    private String taskName;

    public Task(String taskName) {
        this.taskName = taskName;
    }

    @Override
    public String toString() {
        return this.taskName;
    }
}
