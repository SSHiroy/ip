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
            boolean success = tasks.get(i).markComplete();
            if (success) {
                saveFile();
                return new KattyResult(success, "I've marked it as complete! Nice work!",
                        tasks.get(i).toString(), null);
            } else {
                return new KattyResult(success, "Task is already completed!",
                        tasks.get(i).toString(), KattyException.invalidCompletion());
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
            boolean success = tasks.get(i).markIncomplete();
            saveFile();
            if (success) {
                return new KattyResult(success,
                        "I've marked it as incomplete. Let's hope it doesn't stay that way for long...",
                        tasks.get(i).toString(), null);
            } else {
                return new KattyResult(success, "Task was never completed!",
                        tasks.get(i).toString(), KattyException.invalidCompletion());
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
            Task task = tasks.remove(i);
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
     * Loads the state of tasks into the {@code TaskManager} from the local save file.
     * <p>
     * This method attempts to read {@code kattySave.txt}. It processes the file line-by-line,
     * utilizing {@link TaskParser#fromFileString(String)} to reconstruct task objects.
     * If a line is malformed or corrupted, it is skipped to ensure maximum data recovery,
     * and a flag is set to notify the user of the partial load.
     * </p>
     *
     * @return A {@link KattyResult} indicating if the load was successful, partially
     *      successful (with corruption), or failed entirely.
     */
    public KattyResult loadFile() {
        java.io.File file = new java.io.File("kattySave.txt");
        if (!file.exists()) {
            return new KattyResult(false, "No save file found!", "", KattyException.noSaveFile());
        }

        try (java.util.Scanner sc = new java.util.Scanner(file)) {
            tasks = new ArrayList<>();
            boolean hasCorruption = false;

            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }

                try {
                    Task t = TaskParser.fromFileString(line);
                    if (t != null) {
                        tasks.add(t);
                    }
                } catch (Exception e) {
                    hasCorruption = true;
                }
            }

            tasks.sort(Comparator.comparing(Task::getSortDate));

            if (hasCorruption) {
                return new KattyResult(true,
                        "Meow! I recovered your tasks, but some corrupted lines were skipped.",
                        getFormattedTaskList(), KattyException.partialLoadSaveFile());
            }

            return new KattyResult(true, "Data loaded successfully!", getFormattedTaskList(), null);

        } catch (Exception e) {
            return new KattyResult(false, "Critical error: The save file could not be read!",
                    "", KattyException.corruptFile());
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

        String formattedMatches = IntStream.range(0, tasks.size())
                .mapToObj(i -> String.format("%d. %s", i + 1, tasks.get(i).toString()))
                .filter(t -> t.toLowerCase().contains(lowerKeyword))
                .reduce((a, b) -> a + "\n" + b)
                .orElse("");

        if (formattedMatches.isEmpty()) {
            return new KattyResult(false, "I couldn't find anything!",
                    "Try a different keyword?", KattyException.searchResultEmpty());
        }

        return new KattyResult(true, "I found these matches!", formattedMatches, null);
    }
}
