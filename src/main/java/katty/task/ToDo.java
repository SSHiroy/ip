package katty.task;

/**
 * Represents a simple to-do {@link Task}.
 * <p>
 * A {@code ToDo} consists only of a task name and its completion status,
 * without any associated date or time.
 */
public class ToDo extends Task {
    public ToDo(String taskName) {
        super(taskName);
    }

    @Override
    public String toString() {
        return String.format("[T] %s", super.toString());
    }
}
