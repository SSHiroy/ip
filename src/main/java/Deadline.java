class Deadline extends Task {
    private String deadlineBy;

    public Deadline(String deadlineName, String deadlineBy) {
        super(deadlineName);
        this.deadlineBy = deadlineBy;
    }

    @Override
    public String toString() {
        return String.format("[D] %s (by: %s)", super.toString(), this.deadlineBy);
    }
}
