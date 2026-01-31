import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class Event extends Task {
    public static final String EVENT_FORMAT = "yyyy-MM-dd HH:mm";
    private final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern(EVENT_FORMAT);
    private LocalDateTime timeFrom;
    private LocalDateTime timeTo;

    public Event(String eventName, String timeFrom, String timeTo) {
        super(eventName);
        this.timeFrom = LocalDateTime.parse(timeFrom, dateTimeFormat);
        this.timeTo = LocalDateTime.parse(timeTo, dateTimeFormat);
    }

    @Override
    public String toString() {
        return String.format("[E] %s (from: %s to: %s)", super.toString(),
                this.timeFrom.format(dateTimeFormat), this.timeTo.format(dateTimeFormat));
    }
}
