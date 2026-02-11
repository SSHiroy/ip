package katty.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {
    public static final String DEADLINE_FORMAT = "dd-MM-yyyy HH:mm";
    private static final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern(DEADLINE_FORMAT);
    private LocalDateTime deadlineBy;

    public Deadline(String deadlineName, String deadlineBy) {
        super(deadlineName);
        this.deadlineBy = LocalDateTime.parse(deadlineBy, dateTimeFormat);
    }

    @Override
    public String toString() {
        return String.format("[D] %s (by: %s)", super.toString(), this.deadlineBy.format(dateTimeFormat));
    }
}
