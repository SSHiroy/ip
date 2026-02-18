package katty.task;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Provides a simple implementation for various types of tasks.
 * <p>
 * This class helps to structure and manage data for a variety of tasks.
 */
public abstract class Task implements Serializable {
    private String taskName;
    private boolean status;

    /**
     * Creates a new {@code Task} with the given name.
     * <p>
     * Tasks are incomplete by default.
     *
     * @param taskName name of task
     */
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

    public abstract LocalDateTime getSortDate();

    public String getTaskName() {
        return taskName;
    }

    @Override
    public String toString() {
        return String.format("[ %s ] ", this.status ? "X" : " ") + this.taskName;
    }
}
