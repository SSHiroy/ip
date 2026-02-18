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

        // This matches the new Top-Speech logic:
        // 1. The Cat's head and the first message
        // 2. The Cat's chin/feet
        // (No dividers because messages[1] and [2] are blank)
        String expected = "\n /\\_/\\\n( o.o )  Hi there! Katty's ready to help!\n > ^ <\n";

        assertEquals(
                expected,
                Katty.kattyMessage(messages, Katty.KattyExpression.NORMAL)
        );
    }
}
