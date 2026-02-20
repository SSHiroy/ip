package katty.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an event {@link Task} with a defined start and end time.
 * <p>
 * An {@code Event} extends {@link Task} by including a start time
 * ({@code timeFrom}) and an end time ({@code timeTo}) to indicate
 * when the event takes place.
 */

public class Event extends Task {
    public static final String EVENT_FORMAT = "dd-MM-yyyy HH:mm";
    private static final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern(EVENT_FORMAT);
    private LocalDateTime timeFrom;
    private LocalDateTime timeTo;

    /**
     * Constructs an {@code Event} with the specified name,
     * start time, and end time. Events are marked as incomplete by default.
     *
     * @param eventName the name of the event
     * @param timeFrom  the start time of the event in {@value #EVENT_FORMAT} format
     * @param timeTo    the end time of the event in {@value #EVENT_FORMAT} format
     */
    public Event(String eventName, String timeFrom, String timeTo) {
        super(eventName);
        this.timeFrom = LocalDateTime.parse(timeFrom, dateTimeFormat);
        this.timeTo = LocalDateTime.parse(timeTo, dateTimeFormat);
    }

    @Override
    public LocalDateTime getSortDate() {
        return timeFrom;
    }

    @Override
    public String toFileString() {
        // Format: E | status | description | fromDate | toDate
        return String.format("E | %d | %s | %s | %s",
                super.isComplete() ? 1 : 0,
                super.getTaskName(),
                timeFrom.format(dateTimeFormat),
                timeTo.format(dateTimeFormat));
    }

    @Override
    public String toString() {
        return String.format("[E] %s (from: %s to: %s)", super.toString(),
                timeFrom.format(dateTimeFormat), timeTo.format(dateTimeFormat));
    }
}
