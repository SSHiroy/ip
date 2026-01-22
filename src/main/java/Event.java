class Event extends Task {
    private String timeFrom, timeTo;

    public Event(String eventName, String timeFrom, String timeTo) {
        super(eventName);
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
    }

    @Override
    public String toString() {
        return String.format("[E] %s (from: %s to: %s)", super.toString(), this.timeFrom, this.timeTo);
    }
}
