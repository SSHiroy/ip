/**
 * The main entry point for the Katty Chatbot application.
 *
 * <p>This class manages the primary lifecycle of the chatbot, including
 * displaying the startup branding and formatting the bot's visual
 * responses (ASCII persona).
 * </p>
 */
public class Katty {

    /**
     * Sets Katty's expression for messages.
     */
    public enum KattyExpression {
        NORMAL, THINKING, HAPPY, CONFUSED
    }

    /**
     * Formats a three-line message inside an ASCII cat text box.
     *
     * @param messages An array of exactly 3 Strings to be displayed next to Katty.
     *     Null elements are treated as empty strings.
     *     Each string should be capped to a length of 35 characters for aesthetics.
     * @param expression A KattyExpression to set the facial expression used for the message.
     *     If an invalid expression is provided, defaults to NORMAL expression.
     * @return A formatted String containing the ASCII art and the messages.
     * @throws IllegalArgumentException if the messages array is null or
     *     its length is not equal to 3.
     */
    public static String kattyMessage(String[] messages, KattyExpression expression) throws IllegalArgumentException {
        if (messages == null || messages.length != 3) {
            throw new IllegalArgumentException("Must provide exactly 3 messages.");
        }

        final String eyes = switch (expression) {
            case HAPPY -> "^.^";
            case THINKING -> "-.-";
            case CONFUSED -> "?.?";
            case NORMAL -> "o.o";
        };

        return String.format(
            """
            /\\_/\\  | %-35s
           ( %s ) | %-35s
            > ^ <  | %-35s""" + "\n",
            messages[0] == null ? "" : messages[0],
            eyes,
            messages[1] == null ? "" : messages[1],
            messages[2] == null ? "" : messages[2]);
    }

    /**
     * Initializes the chatbot, displays the welcome logo, and starts
     * the interaction session.
     *
     * @param args Command line arguments for configuration (not used).
     */
    public static void main(String[] args) {
        String logo =
                    """
                     __   ___        __       ___________   ___________   ___  ___
                    |/"| /  ")      /""\\     ("     _   ") ("     _   ") |"  \\/"  |
                    (: |/   /      /    \\     )__/  \\\\__/   )__/  \\\\__/   \\   \\  /
                    |    __/      /' /\\  \\       \\\\_ /         \\\\_ /       \\\\  \\/
                    (// _  \\     //  __'  \\      |.  |         |.  |       /   /
                    |: | \\  \\   /   /  \\\\  \\     \\:  |         \\:  |      /   /
                    (__|  \\__) (___/    \\___)     \\__|          \\__|     |___/
                    """;
        System.out.println("\n\nWelcome to\n" + logo + "\n");

        System.out.println(kattyMessage(new String[]{"Hi there! Katty's ready to help!", "", ""},
                                        KattyExpression.NORMAL));

        System.out.println(kattyMessage(new String[]{"Always glad to help!", "", "Goodbye..."}, KattyExpression.HAPPY));
    }
}
