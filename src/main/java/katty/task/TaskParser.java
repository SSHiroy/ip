package katty.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import katty.KattyException;
import katty.TaskManager;

/**
 * Parses user commands and converts them into {@link Task} objects.
 * <p>
 * Acts as a bridge between {@link katty.Katty} and {@link TaskManager},
 * translating raw user input into concrete task instances.
 */
public class TaskParser {

    /**
     * Parses user commands and inputs.
     *
     * @param command decides how input is processed
     * @param input   user input
     * @return an object of {@code Task}
     * @throws KattyException if there is an invalid command or input
     */
    public static Task parser(String command, String input) throws KattyException {
        String safeInput = input.replace("|", "");
        switch (command) {
        case "todo" -> {
            if (safeInput.isBlank()) {
                throw KattyException.invalidTodo();
            }
            return new ToDo(safeInput);
        }
        case "deadline" -> {
            String[] s = safeInput.split(" /by ");
            if (s.length != 2) {
                throw KattyException.invalidDeadline();
            }
            try {
                return new Deadline(s[0], s[1]);
            } catch (DateTimeParseException e) {
                throw KattyException.badDateFormat();
            }
        }
        case "event" -> {
            String[] s = safeInput.split(" /from | /to ");
            if (s.length != 3) {
                throw KattyException.invalidEvent();
            }
            try {
                LocalDateTime start = LocalDateTime.parse(s[1], DateTimeFormatter.ofPattern(Event.EVENT_FORMAT));
                LocalDateTime end = LocalDateTime.parse(s[2], DateTimeFormatter.ofPattern(Event.EVENT_FORMAT));

                if (start.isAfter(end)) {
                    throw KattyException.invalidTimeRange();
                }

                return new Event(s[0], s[1], s[2]);
            } catch (DateTimeParseException e) {
                throw KattyException.badDateFormat();
            }
        }
        default -> throw KattyException.invalidCommand();
        }
    }

    /**
     * Reconstructs a {@link Task} object from a formatted string stored in the save file.
     * <p>
     * The method expects a pipe-delimited format: {@code Type | Status | Description | Dates}.
     * It handles ToDos (T), Deadlines (D), and Events (E). If the status is "1",
     * the task is automatically marked as complete.
     * </p>
     *
     * @param line the raw line read from the save file (e.g., "T | 1 | buy milk")
     * @return the corresponding {@code Task} object, or {@code null} if the format is invalid
     */
    public static Task fromFileString(String line) throws KattyException {
        try {
            String[] parts = line.split(" \\| ");
            String type = parts[0];
            boolean isDone = parts[1].equals("1");
            String desc = parts[2];

            Task t = switch (type) {
            case "T" -> new ToDo(desc);
            case "D" -> new Deadline(desc, parts[3]);
            case "E" -> new Event(desc, parts[3], parts[4]);
            default -> throw KattyException.corruptFile();
            };

            if (t != null && isDone) {
                t.markComplete();
            }
            return t;
        } catch (Exception e) {
            throw KattyException.corruptFile();
        }
    }
}
