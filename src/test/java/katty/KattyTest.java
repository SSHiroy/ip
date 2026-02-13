package katty;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class KattyTest {
    @Test
    public void katty_messaging() {
        String[] messages = {
            "Hi there! Katty's ready to help!",
            "",
            ""
        };

        String expected = "\n" + String.format(
                """
                /\\_/\\  | %-35s
               ( %s ) | %-35s
                > ^ <  | %-35s""" + "\n",
                messages[0],
                "o.o",
                messages[1],
                messages[2]
        ) + "\n";

        assertEquals(
                expected,
                Katty.kattyMessage(messages, Katty.KattyExpression.NORMAL)
        );
    }
}
