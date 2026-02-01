package katty.task;

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
            } else {
                return new Deadline(s[0], s[1]);
            }
        }
        case "event" -> {
            String[] s = input.split(" /from | /to ");
            if (s.length != 3) {
                throw KattyException.invalidTask();
            } else {
                return new Event(s[0], s[1], s[2]);
            }
        }
        default -> throw KattyException.invalidTask();
        }
    }
}
