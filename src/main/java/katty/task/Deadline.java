package katty.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Extends an {@code Task}, as a deadline {@code Deadline} comes with a name, completion status and a deadline.
 */
public class Deadline extends Task {
    public static final String DEADLINE_FORMAT = "dd-MM-yyyy HH:mm";
    private static final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern(DEADLINE_FORMAT);
    private LocalDateTime deadlineBy;

    /**
     * Creates a new {@code Deadline} with a name, deadline and completion status. <br>
     * Deadlines are incomplete by default.
     *
     * @param deadlineName name of deadline
     * @param deadlineBy datetime the deadline is due
     */
    public Deadline(String deadlineName, String deadlineBy) {
        super(deadlineName);
        this.deadlineBy = LocalDateTime.parse(deadlineBy, dateTimeFormat);
    }

    @Override
    public LocalDateTime getSortDate() {
        return deadlineBy;
    }

    @Override
    public String toFileString() {
        return String.format("D | %d | %s | %s",
                super.isComplete() ? 1 : 0,
                super.getTaskName(),
                deadlineBy.format(dateTimeFormat));
    }

    @Override
    public String toString() {
        return String.format("[D] %s (by: %s)", super.toString(), this.deadlineBy.format(dateTimeFormat));
    }
}
