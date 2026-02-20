package katty.task;

import java.time.LocalDateTime;

/**
 * Provides a simple implementation for various types of tasks.
 * <p>
 * This class helps to structure and manage data for a variety of tasks.
 */
public abstract class Task {
    private String taskName;
    private boolean isComplete;

    /**
     * Creates a new {@code Task} with the given name.
     * <p>
     * Tasks are incomplete by default.
     *
     * @param taskName name of task
     */
    public Task(String taskName) {
        this.taskName = taskName;
        this.isComplete = false;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public boolean markComplete() {
        return !isComplete && (isComplete = true);
    }

    public boolean markIncomplete() {
        return isComplete && !(isComplete = false);
    }

    public abstract LocalDateTime getSortDate();
    public abstract String toFileString();

    public String getTaskName() {
        return taskName;
    }

    @Override
    public String toString() {
        return String.format("[%s] ", isComplete ? "X" : " ") + taskName;
    }
}
