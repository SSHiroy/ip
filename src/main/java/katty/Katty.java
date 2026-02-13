package katty;

import java.util.Scanner;

import katty.task.Deadline;
import katty.task.Event;


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
    public static String kattyMessage(String[] messages, KattyExpression expression)
            throws IllegalArgumentException {

        if (messages == null || messages.length != 3) {
            throw new IllegalArgumentException("Must provide exactly 3 messages.");
        }

        final String eyes = switch (expression) {
        case HAPPY -> "^.^";
        case THINKING -> "-.-";
        case CONFUSED -> "?.?";
        case NORMAL -> "o.o";
        };

        return "\n" + String.format(
            """
            /\\_/\\  | %-35s
           ( %s ) | %-35s
            > ^ <  | %-35s""" + "\n",
            messages[0] == null ? "" : messages[0],
            eyes,
            messages[1] == null ? "" : messages[1],
            messages[2] == null ? "" : messages[2]) + "\n";
    }

    /**
     * Initializes the chatbot, displays the welcome logo, and starts
     * the interaction session.
     */
    public static void kattyStart() {
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

        // Welcome message
        System.out.println("\n\nWelcome to\n" + logo + "\n");

        // Katty's welcome message
        System.out.println(kattyMessage(new String[]{"Hi there! Katty's ready to help!", "", ""},
                                        KattyExpression.NORMAL));

        Scanner scanner = new Scanner(System.in);
        TaskManager taskManager = new TaskManager();

        KattyResult saveFileFound = taskManager.loadFile();

        boolean visibleExceptions = false;

        if (saveFileFound.isSuccess()) {
            System.out.println(kattyMessage(new String[]{"I found a save file!", "", "Data has been restored."},
                    KattyExpression.HAPPY));
        } else {
            System.out.println(kattyMessage(new String[]{"I could not find a save file! "
                    + "Maybe it's your first time meeting me...", saveFileFound.getMessage(),
                            saveFileFound.getException().getMessage()},
                    KattyExpression.NORMAL));
        }

        // User command loop
        while (true) {
            String userCommand = scanner.nextLine().strip();
            String[] command = userCommand.split(" ", 2);
            boolean userExit = false;
            switch (command[0]) {

            case "" -> System.out.println(kattyMessage(new String[]{
                "Meow?",
                visibleExceptions ? KattyException.emptyInputException().toString() : "",
                "(Try typing a command..."},
                KattyExpression.NORMAL));

            case "dev" -> {
                visibleExceptions = !visibleExceptions;
                if (visibleExceptions) {
                    System.out.println(kattyMessage(new String[]{
                        "Dev mode enabled!", "", "I'll have to think extra hard now..."},
                        KattyExpression.THINKING));
                } else {
                    System.out.println(kattyMessage(new String[]{
                        "Dev mode disabled.", "", ""}, KattyExpression.NORMAL));
                }
            }

            case "todo", "deadline", "event" -> {
                if (command.length != 2) {
                    System.out.println(kattyMessage(new String[]{
                        "I need more details! Follow the format",
                        visibleExceptions ? KattyException.invalidTask().toString() : "", String.format(
                        "todo title ; deadline title /by %s ; "
                            + "event title /from %s /to %s", Deadline.DEADLINE_FORMAT, Event.EVENT_FORMAT,
                                    Event.EVENT_FORMAT)},
                        KattyExpression.CONFUSED));
                    break;
                }
                KattyResult result = taskManager.parser(command[0], command[1]);
                System.out.println(kattyMessage(new String[]{
                            result.getMessage(), "",
                            result.isSuccess() ? result.getData() : result.getException().getMessage()},
                            result.isSuccess() ? KattyExpression.NORMAL : KattyExpression.CONFUSED));
            }

            case "list" -> {
                System.out.println(kattyMessage(new String[]{"Let me recall try to recall!", "",
                    "If I remember correctly..."}, KattyExpression.THINKING));

                String tasks = taskManager.getFormattedTaskList();

                if (tasks.isBlank()) {
                    System.out.println(Katty.kattyMessage(new String[]{"Nothing to do!", "", ""},
                            KattyExpression.NORMAL));
                } else {
                    System.out.println(tasks);
                }

                System.out.println(Katty.kattyMessage(new String[]{"Hope that helps!", "", ""},
                        Katty.KattyExpression.HAPPY));
            }

            case "mark" -> {
                KattyResult result;
                try {
                    if (command.length != 2) {
                        result = new KattyResult(false, "That's not a valid task number!",
                                "", KattyException.noTaskFound());
                    } else {
                        int i = Integer.parseInt(command[1]);
                        result = taskManager.markDone(i);
                    }

                } catch (NumberFormatException e) {
                    result = new KattyResult(false, "That's not a valid task number!",
                            "", KattyException.noTaskFound());
                }

                String exceptionMessage = !result.isSuccess() && visibleExceptions
                        ? result.getException().toString() : "";

                System.out.println(kattyMessage(new String[]{result.getMessage(), exceptionMessage,
                                result.getData()},
                        result.isSuccess() ? KattyExpression.NORMAL : KattyExpression.CONFUSED));
            }

            case "unmark" -> {
                KattyResult result;
                try {
                    if (command.length != 2) {
                        result = new KattyResult(false, "That's not a valid task number!",
                                "", KattyException.noTaskFound());
                    } else {
                        int i = Integer.parseInt(command[1]);
                        result = taskManager.markIncomplete(i);
                    }

                } catch (NumberFormatException e) {
                    result = new KattyResult(false, "That's not a valid task number!",
                                        "", KattyException.noTaskFound());
                }

                String exceptionMessage = !result.isSuccess() && visibleExceptions
                        ? result.getException().getMessage() : "";

                System.out.println(kattyMessage(new String[]{result.getMessage(), exceptionMessage,
                                result.getData()},
                        result.isSuccess() ? KattyExpression.NORMAL : KattyExpression.CONFUSED));
            }

            case "delete" -> {
                KattyResult result;
                try {
                    if (command.length != 2) {
                        result = new KattyResult(false, "That's not a valid task number!",
                                "", KattyException.noTaskFound());
                    } else {
                        int i = Integer.parseInt(command[1]);
                        result = taskManager.deleteTask(i);
                    }

                } catch (NumberFormatException e) {
                    result = new KattyResult(false, "That's not a valid task number!",
                                        "", KattyException.noTaskFound());
                }

                String exceptionMessage = !result.isSuccess() && visibleExceptions
                        ? result.getException().getMessage() : "";

                if (result.isSuccess()) {
                    System.out.println(kattyMessage(new String[]{"Got it! I've forgotten all about",
                        result.getData(), "What were we talking about...?"},
                            result.isSuccess() ? KattyExpression.NORMAL : KattyExpression.CONFUSED));
                } else {
                    System.out.println(kattyMessage(new String[]{result.getMessage(), "", exceptionMessage},
                                       KattyExpression.CONFUSED));
                }
            }

            case "bye" -> userExit = true;

            case "find" -> {
                System.out.println(kattyMessage(new String[]{"Let me see...", "", "I think this is it!"},
                                   KattyExpression.NORMAL));
                System.out.println(taskManager.findTasksByName(command[1]) + "\n");
            }

            default -> System.out.println(kattyMessage(
                    new String[]{"I'm not sure what to do...",
                                 visibleExceptions ? KattyException.invalidCommand().toString() : "",
                                 "(Try typing a valid command..."},
                    KattyExpression.CONFUSED));
            }

            if (userExit) {
                break;
            }
        }

        scanner.close();

        // Goodbye message
        System.out.println(kattyMessage(new String[]{"Always glad to help!", "", "Goodbye..."},
                           KattyExpression.HAPPY));
    }
}
