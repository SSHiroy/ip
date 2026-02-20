package katty;

import java.util.Scanner;

/**
 * The main entry point for the Katty Chatbot application.
 *
 * <p>This class manages the primary lifecycle of the chatbot, including
 * displaying the startup branding and formatting the bot's visual
 * responses (ASCII persona).
 * </p>
 */
public class Katty {
    private static final TaskManager taskManager = new TaskManager();
    private static boolean isExceptionsVisible = false;
    private static final String LOGO =
                    """
                     __   ___        __       ___________   ___________   ___  ___
                    |/"| /  ")      /""\\     ("     _   ") ("     _   ") |"  \\/"  |
                    (: |/   /      /    \\     )__/  \\\\__/   )__/  \\\\__/   \\   \\  /
                    |    __/      /' /\\  \\       \\\\_ /         \\\\_ /       \\\\  \\/
                    (// _  \\     //  __'  \\      |.  |         |.  |       /   /
                    |: | \\  \\   /   /  \\\\  \\     \\:  |         \\:  |      /   /
                    (__|  \\__) (___/    \\___)     \\__|          \\__|     |___/
                    """;

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
     *     Each string should be capped to a length of 50 characters for aesthetics.
     * @param expression A KattyExpression to set the facial expression used for the message.
     *     If an invalid expression is provided, defaults to NORMAL expression.
     * @return A formatted String containing the ASCII art and the messages.
     * @throws IllegalArgumentException if the messages array is null or
     *     its length is not equal to 3.
     */
    public static String kattyMessage(String[] messages, KattyExpression expression) {
        if (messages == null || messages.length != 3) {
            throw new IllegalArgumentException("Must provide exactly 3 messages.");
        }

        final String eyes = switch (expression) {
        case HAPPY -> "^.^";
        case THINKING -> "-.-";
        case CONFUSED -> "?.?";
        case NORMAL -> "o.o";
        };

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("\n /\\_/\\\n( %s )  ", eyes));

        if (messages[0] != null && !messages[0].isBlank()) {
            sb.append(messages[0]);
        }
        sb.append("\n > ^ <\n");

        boolean hasDetails = (messages[1] != null && !messages[1].isBlank())
                || (messages[2] != null && !messages[2].isBlank());

        if (hasDetails) {
            sb.append("  --------------------------------------------------\n");
            if (messages[1] != null && !messages[1].isBlank()) {
                sb.append("  ").append(messages[1]).append("\n");
            }
            if (messages[2] != null && !messages[2].isBlank()) {
                sb.append("  ").append(messages[2]).append("\n");
            }
            sb.append("  --------------------------------------------------\n");
        }

        return sb.toString();
    }

    /**
     * Dispatches a KattyResult by formatting it into a Katty-styled message.
     * Automatically handles dev mode visibility and text constraints.
     *
     * @param result The result to be processed.
     * @return A formatted ASCII cat message string.
     */
    private static String dispatch(KattyResult result) {
        KattyExpression expression = result.isSuccess() ? KattyExpression.NORMAL : KattyExpression.CONFUSED;

        String mainMsg = result.getMessage();
        String detail = result.isSuccess() ? result.getData() : result.getException().getMessage();
        String devLabel = "";

        if (!result.isSuccess() && isExceptionsVisible) {
            expression = KattyExpression.THINKING;
            devLabel = "DEBUG: [" + result.getException().getClass().getSimpleName() + "]";
        }

        return kattyMessage(new String[]{mainMsg, devLabel, detail}, expression);
    }

    public static String getInitialGreeting() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n\nWelcome to\n").append(LOGO).append("\n");
        sb.append(kattyMessage(new String[]{"Hi there! Katty's ready to help!", "", ""},
                KattyExpression.NORMAL));

        KattyResult loadResult = taskManager.loadFile();

        if (loadResult.isSuccess()) {
            if (loadResult.getException() != null) {
                sb.append(kattyMessage(new String[]{
                    "I found your tasks but skipped over some messy parts...",
                    loadResult.getException().getMessage(),
                    "Close the app now if you want to fix the file manually!"
                }, KattyExpression.CONFUSED));
            } else {
                sb.append(kattyMessage(new String[]{"I found a save file!", "", "Data has been restored."},
                        KattyExpression.HAPPY));
            }
        } else {
            sb.append(kattyMessage(new String[]{
                "Meow! I couldn't load your tasks.",
                loadResult.getMessage(),
                "I'll start a fresh list for you!"
            }, KattyExpression.CONFUSED));
        }
        return sb.toString();
    }

    public static String getResponse(String userCommand) {
        try {
            StringBuilder response = new StringBuilder();
            String[] command = userCommand.split(" ", 2);
            command[0] = command[0].toLowerCase();

            switch (command[0]) {

            case "" -> response.append(dispatch(new KattyResult(false, "Meow?",
                    "(Try typing a command...)", KattyException.emptyInputException())));

            case "dev" -> {
                isExceptionsVisible = !isExceptionsVisible;
                String status = isExceptionsVisible ? "enabled!" : "disabled.";
                String sub = isExceptionsVisible ? "I'll have to think extra hard now..." : "";
                response.append(kattyMessage(new String[]{"Dev mode " + status, "", sub},
                        isExceptionsVisible ? KattyExpression.THINKING : KattyExpression.NORMAL));
            }

            case "todo", "deadline", "event" -> {
                String input = (command.length == 2) ? command[1] : "";
                response.append(dispatch(taskManager.parser(command[0], input)));
            }

            case "list", "listByName" -> {
                response.append(kattyMessage(new String[]{"Let me recall try to recall!", "",
                    "If I remember correctly..."}, KattyExpression.THINKING));

                String tasks = command[0].equals("list") ? taskManager.getFormattedTaskList()
                        : taskManager.getListByName();

                if (tasks.isBlank()) {
                    response.append(kattyMessage(new String[]{"Nothing to do!", "", ""},
                            KattyExpression.NORMAL));
                } else {
                    response.append(tasks);
                }
                response.append(kattyMessage(new String[]{"Hope that helps!", "", ""},
                        KattyExpression.HAPPY));
            }

            case "mark", "unmark" -> {
                try {
                    int i = (command.length == 2) ? Integer.parseInt(command[1]) : -1;
                    KattyResult res = command[0].equals("mark") ? taskManager.markDone(i)
                            : taskManager.markIncomplete(i);
                    response.append(dispatch(res));
                } catch (NumberFormatException e) {
                    response.append(dispatch(new KattyResult(false, "Invalid task number!",
                            "", KattyException.noTaskFound())));
                }
            }

            case "delete" -> {
                try {
                    int i = (command.length == 2) ? Integer.parseInt(command[1]) : -1;
                    KattyResult result = taskManager.deleteTask(i);
                    if (result.isSuccess()) {
                        response.append(kattyMessage(new String[]{
                            "Got it! I've forgotten all about:",
                            result.getData(),
                            "What were we talking about...?"
                        }, KattyExpression.NORMAL));
                    } else {
                        response.append(dispatch(result));
                    }
                } catch (NumberFormatException e) {
                    response.append(dispatch(new KattyResult(false, "Invalid task number!",
                            "", KattyException.noTaskFound())));
                }
            }

            case "find" -> {
                if (command.length != 2) {
                    response.append(dispatch(new KattyResult(false, "Find what?",
                            "Try: find book", KattyException.invalidCommand())));
                } else {
                    KattyResult result = taskManager.findTasksByName(command[1]);
                    if (result.isSuccess()) {
                        response.append(kattyMessage(new String[]{"Searching my memory...", "", ""},
                                KattyExpression.THINKING));
                        response.append("----------\n").append(result.getData()).append("\n----------\n");
                        response.append(kattyMessage(new String[]{"Found them!", "", ""}, KattyExpression.HAPPY));
                    } else {
                        response.append(dispatch(result));
                    }
                }
            }

            default -> response.append(dispatch(new KattyResult(false, "I'm not sure what to do...",
                    "(Try typing a valid command...)", KattyException.invalidCommand())));
            }
            return response.toString();

        } catch (Exception e) {
            return dispatch(new KattyResult(false, "Internal Error!", "", e));
        }
    }

    /**
     * Initializes the chatbot, displays the welcome logo, and starts
     * the interaction session.
     */
    public static void kattyStart() {
        System.out.println(getInitialGreeting());

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine().strip();
            if (input.isEmpty()) {
                continue;
            }

            String response = getResponse(input);
            System.out.println(response);

            if (input.equals("bye")) {
                break;
            }
        }
        scanner.close();

        System.out.println(kattyMessage(new String[]{"Always glad to help!", "", "Goodbye..."},
                KattyExpression.HAPPY));
    }
}
