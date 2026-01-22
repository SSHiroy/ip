import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Provides a management system for tasks.
 *
 * <p>This class provides a way for Katty to handle the creation,
 * deletion or modification of tasks for the user.</p>
 */
class TaskManager {
    private final List<Task> tasks;

    public TaskManager() {
        this.tasks = new ArrayList<>();
    }

    public KattyResult addTask(Task task) {
        this.tasks.add(task);
        return new KattyResult(true, "Let's get to work...", task.toString(), null);
    }

    public KattyResult markDone(int i) {
        i = i - 1;
        try {
            boolean success = this.tasks.get(i).markDone();
            if (success) {
                return new KattyResult(success, "I've marked it as complete! Nice work!",
                        this.tasks.get(i).toString(), null);
            } else {
                return new KattyResult(success, "Task is already completed!",
                                       this.tasks.get(i).toString(), null);
            }
        } catch (IndexOutOfBoundsException e) {
            return new KattyResult(false, "That task doesn't exist!", null, e);
        }
    }

    public KattyResult markIncomplete(int i) {
        i = i - 1;
        try {
            boolean success = this.tasks.get(i).markIncomplete();
            if (success) {
                return new KattyResult(success,
                        "I've marked it as incomplete. Let's hope it doesn't stay that way for long...",
                        this.tasks.get(i).toString(), null);
            } else {
                return new KattyResult(success, "Task was never completed!",
                        this.tasks.get(i).toString(), null);
            }
        } catch (IndexOutOfBoundsException e) {
            return new KattyResult(false, "That task doesn't exist!", null, e);
        }
    }

    public String getFormattedTaskList() {
        if (this.tasks.isEmpty()) {
            return "";
        }

        List<String> taskStringFormat = IntStream.range(0, this.tasks.size())
                .mapToObj(i -> String.format("Task %d: %s", i + 1, this.tasks.get(i).toString()))
                .toList();

        return "----------\n" + String.join("\n", taskStringFormat) + "\n-----------";
    }
}
