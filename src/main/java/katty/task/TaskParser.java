package katty.task;

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
        switch (command) {
        case "todo" -> {
            if (input.isBlank()) {
                throw KattyException.invalidTodo();
            }
            return new ToDo(input);
        }
        case "deadline" -> {
            String[] s = input.split(" /by ");
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
            String[] s = input.split(" /from | /to ");
            if (s.length != 3) {
                throw KattyException.invalidEvent();
            }
            try {
                return new Event(s[0], s[1], s[2]);
            } catch (DateTimeParseException e) {
                throw KattyException.badDateFormat();
            }
        }
        default -> throw KattyException.invalidCommand();
        }
    }
}
