package katty.task;

import java.time.format.DateTimeParseException;

import katty.KattyException;

public class TaskParser {
    public static Task parser(String command, String input) throws KattyException {
        switch (command) {
        case "todo" -> {
            return new ToDo(input);
        }
        case "deadline" -> {
            String[] s = input.split(" /by ");
            if (s.length != 2) {
                throw KattyException.invalidTask();
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
                throw KattyException.invalidTask();
            }
            try {
                return new Event(s[0], s[1], s[2]);
            } catch (DateTimeParseException e) {
                throw KattyException.badDateFormat();
            }
        }
        default -> throw KattyException.invalidTask();
        }
    }
}
