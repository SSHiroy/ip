import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Provides a management system for tasks.
 *
 * <p>This class provides a way for Katty to handle the creation,
 * deletion or modification of tasks for the user.</p>
 */
class TaskManager implements Serializable {
    private static final long serialVersionUID = 2L;
    private List<Task> tasks;

    public TaskManager() {
        this.tasks = new ArrayList<>();
    }

    public KattyResult parser(String command, String input) {
        try {
            Task t = TaskParser.parser(command, input);
            this.tasks.add(t);
            saveFile();
            return new KattyResult(true, "Got it! This is what's up...", t.toString(), null);
        } catch (KattyException e) {
            return new KattyResult(false, "That's not right...", "", e);
        }
    }

    public KattyResult markDone(int i) {
        i = i - 1;
        try {
            boolean success = this.tasks.get(i).markDone();
            if (success) {
                saveFile();
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
            saveFile();
            if (success) {
                return new KattyResult(success,
                        "I've marked it as incomplete. Let's hope it doesn't stay that way for long...",
                        this.tasks.get(i).toString(), KattyException.invalidCompletion());
            } else {
                return new KattyResult(success, "Task was never completed!",
                        this.tasks.get(i).toString(), KattyException.invalidCompletion());
            }
        } catch (IndexOutOfBoundsException e) {
            return new KattyResult(false, "That task doesn't exist!", null, KattyException.noTaskFound());
        }
    }

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
        if (this.tasks.isEmpty()) {
            return "";
        }

        List<String> taskStringFormat = IntStream.range(0, this.tasks.size())
                .mapToObj(i -> String.format("Task %d: %s", i + 1, this.tasks.get(i).toString()))
                .toList();

        return "----------\n" + String.join("\n", taskStringFormat) + "\n-----------";
    }

    public KattyResult saveFile() {
        try (FileOutputStream fos = new FileOutputStream("kattySave");
            ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(this.tasks);

        } catch (IOException e) {
            return new KattyResult(false, "Save file could not be made!", "", KattyException.failToSave());
        }

        return new KattyResult(true, "", "", null);
    }

    @SuppressWarnings("unchecked")
    public KattyResult loadFile() {
        try (FileInputStream fis = new FileInputStream("kattySave");
            ObjectInputStream ois = new ObjectInputStream(fis)) {

            // This requires an unchecked cast but should be safe using exception catching
            this.tasks = (List<Task>) ois.readObject();

        } catch (FileNotFoundException e) {
            return new KattyResult(false, "No save file found!", "", KattyException.noSaveFile());
        } catch (IOException e) {
            return new KattyResult(false, "File could not be read. It might be corrupted!",
                    "", KattyException.corruptFile());
        } catch (ClassNotFoundException e) {
            return new KattyResult(false, "File could not be read. The save file could be a different version!",
                    "", KattyException.noSaveFile());
        }
        return new KattyResult(true, "Save file found. Data has been loaded!", this.getFormattedTaskList(), null);
    }
}
