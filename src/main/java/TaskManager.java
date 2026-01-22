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

    public String addTask(Task task) {
        this.tasks.add(task);
        return "added: " + task;
    }

    public String getFormattedTaskList() {
        if (this.tasks.isEmpty()) {
            return "";
        }

        List<String> taskStringFormat = IntStream.range(0, this.tasks.size())
                .mapToObj(i -> String.format("Task %d: %s", i + 1, this.tasks.get(i).toString()))
                .toList();

        return "\n----------\n" + String.join("\n", taskStringFormat) + "\n-----------\n";
    }
}
