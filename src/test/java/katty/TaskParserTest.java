package katty;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import katty.task.Deadline;
import katty.task.Event;
import katty.task.TaskParser;
import katty.task.ToDo;

public class TaskParserTest {
    @Test
    public void parse_todo() {
        try {
            assertEquals(new ToDo("Todo").toString(), TaskParser.parser("todo", "Todo").toString());
        } catch (KattyException e) {
            fail();
        }
    }

    @Test
    public void parse_deadline() {
        try {
            assertEquals(new Deadline("Deadline", "13-02-2026 11:30").toString(),
                         TaskParser.parser("deadline", "Deadline /by 13-02-2026 11:30").toString());
        } catch (KattyException e) {
            fail();
        }
    }

    @Test
    public void parse_event() {
        try {
            assertEquals(new Event("Event", "13-02-2026 11:30", "14-02-2026 13:30").toString(),
                         TaskParser.parser("event", "Event /from 13-02-2026 11:30 /to 14-02-2026 13:30").toString());
        } catch (KattyException e) {
            fail();
        }
    }
}
