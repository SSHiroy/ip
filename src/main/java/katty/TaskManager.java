package katty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import katty.task.Task;
import katty.task.TaskParser;

/**
 * Provides a management system for tasks.
 *
 * <p>This class provides a way for Katty to handle the creation,
 * deletion or modification of tasks for the user.</p>
 */
public class TaskManager {
    private List<Task> tasks;

    public TaskManager() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Parses user command and input for generating the corresponding task.
     *
     * @param command type of task
     * @param input   task details
     * @return success of operation
     */
    public KattyResult parser(String command, String input) {
        try {
            Task t = TaskParser.parser(command, input);
            tasks.add(t);
            tasks.sort(Comparator.comparing(Task::getSortDate));
            saveFile();
            return new KattyResult(true, "Got it! This is what's up...", t.toString(), null);
        } catch (KattyException e) {
            return new KattyResult(false, "I couldn't add that task!", "", e);
        }
    }

    /**
     * Marks a task in the task list {@code tasks} as complete using an index.
     *
     * @param i index of task in the list
     * @return success of operation
     */
    public KattyResult markDone(int i) {
        i = i - 1;
        try {
            boolean success = this.tasks.get(i).markComplete();
            if (success) {
                saveFile();
                return new KattyResult(success, "I've marked it as complete! Nice work!",
                        this.tasks.get(i).toString(), null);
            } else {
                return new KattyResult(success, "Task is already completed!",
                        this.tasks.get(i).toString(), KattyException.invalidCompletion());
            }
        } catch (IndexOutOfBoundsException e) {
            return new KattyResult(false, "That task doesn't exist!", null, KattyException.noTaskFound());
        }
    }

    /**
     * Marks a task in the task list {@code tasks} as incomplete using an index.
     *
     * @param i index of task in the list
     * @return success of operation
     */
    public KattyResult markIncomplete(int i) {
        i = i - 1;
        try {
            boolean success = this.tasks.get(i).markIncomplete();
            saveFile();
            if (success) {
                return new KattyResult(success,
                        "I've marked it as incomplete. Let's hope it doesn't stay that way for long...",
                        this.tasks.get(i).toString(), null);
            } else {
                return new KattyResult(success, "Task was never completed!",
                        this.tasks.get(i).toString(), KattyException.invalidCompletion());
            }
        } catch (IndexOutOfBoundsException e) {
            return new KattyResult(false, "That task doesn't exist!", null, KattyException.noTaskFound());
        }
    }

    /**
     * Deletes a task in the task list {@code tasks} using an index.
     *
     * @param i index of task in the list
     * @return success of operation
     */
    public KattyResult deleteTask(int i) {
        i = i - 1;
        try {
            Task task = this.tasks.remove(i);
            saveFile();
            return new KattyResult(true, "", task.toString(), null);
        } catch (IndexOutOfBoundsException e) {
            return new KattyResult(false, "That task doesn't exist!", null, KattyException.noTaskFound());
        }
    }

    public String getFormattedTaskList() {
        if (tasks.isEmpty()) {
            return "";
        }
        List<String> taskStringFormat = IntStream.range(0, tasks.size())
                .mapToObj(i -> String.format("%d. %s", i + 1, tasks.get(i).toString()))
                .toList();

        return String.join("\n", taskStringFormat);
    }

    public String getListByName() {
        if (tasks.isEmpty()) {
            return "";
        }
        List<Task> sortedByName = tasks.stream()
                .sorted(Comparator.comparing(Task::getTaskName, String.CASE_INSENSITIVE_ORDER))
                .toList();

        List<String> taskStringFormat = IntStream.range(0, sortedByName.size())
                .mapToObj(i -> String.format("%d. %s", i + 1, sortedByName.get(i).toString()))
                .toList();

        return String.join("\n", taskStringFormat);
    }

    /**
     * Saves serialized state of tasks {@code TaskManager} in a file.
     *
     * @return success of operation
     */
    public KattyResult saveFile() {
        try (java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.FileWriter("kattySave.txt"))) {
            for (Task t : tasks) {
                writer.println(t.toFileString());
            }
            return new KattyResult(true, "Saved to text file!", "", null);
        } catch (IOException e) {
            return new KattyResult(false, "Save file could not be made!", "", KattyException.failToSave());
        }
    }

    /**
     * Loads state of tasks {@code TaskManager} from a file.
     *
     * @return success of operation
     */
    public KattyResult loadFile() {
        java.io.File file = new java.io.File("kattySave.txt");
        if (!file.exists()) {
            return new KattyResult(false, "No save file found!", "", KattyException.noSaveFile());
        }

        try (java.util.Scanner sc = new java.util.Scanner(file)) {
            this.tasks = new ArrayList<>();
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.isBlank()) {
                    continue;
                }

                Task t = TaskParser.fromFileString(line);
                if (t != null) {
                    this.tasks.add(t);
                }
            }
            this.tasks.sort(Comparator.comparing(Task::getSortDate));
            return new KattyResult(true, "Data loaded from text!", this.getFormattedTaskList(), null);
        } catch (Exception e) {
            return new KattyResult(false, "File could not be read!", "", KattyException.corruptFile());
        }
    }

    /**
     * Searches the task list for tasks whose names contain the specified keyword.
     *
     * @param keyword the keyword to search for within task names.
     * @return A KattyResult containing the matching tasks or an error if none found.
     */
    public KattyResult findTasksByName(String keyword) {
        String lowerKeyword = keyword.toLowerCase();

        List<Task> matchingTasks = tasks.stream()
                .filter(t -> t.getTaskName().toLowerCase().contains(lowerKeyword))
                .toList();

        if (matchingTasks.isEmpty()) {
            return new KattyResult(false, "I couldn't find anything!",
                    "Try a different keyword?", KattyException.noTaskFound());
        }

        String formattedMatches = IntStream.range(0, matchingTasks.size())
                .mapToObj(i -> String.format("%d. %s", i + 1, matchingTasks.get(i).toString()))
                .reduce((a, b) -> a + "\n" + b)
                .orElse("");

        return new KattyResult(true, "I found these matches!", formattedMatches, null);
    }
}
