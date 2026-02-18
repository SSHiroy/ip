package katty;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class KattyGui extends Application {

    private VBox chatContainer;
    private ScrollPane scrollPane;

    @Override
    public void start(Stage primaryStage) {
        chatContainer = new VBox(20);
        chatContainer.setPadding(new Insets(20));
        chatContainer.setStyle("-fx-background-color: #2b2b2b;");

        scrollPane = new ScrollPane(chatContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background: #2b2b2b; -fx-background-color: #2b2b2b; -fx-border-color: #2b2b2b;");

        chatContainer.heightProperty().addListener((obs, oldVal, newVal) -> scrollPane.setVvalue(1.0));

        TextField inputField = new TextField();
        inputField.setPromptText("Type a command...");
        inputField.setFont(Font.font("Monospaced", 16));
        inputField.setStyle("-fx-background-radius: 25; -fx-background-color: "
                + "#3e3e3e; -fx-text-fill: white; -fx-padding: 12;");

        Button sendButton = new Button("➤");
        sendButton.setStyle("-fx-background-radius: 50; -fx-background-color: #4caf50; "
                + "-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 18px;");

        HBox inputBox = new HBox(15, inputField, sendButton);
        HBox.setHgrow(inputField, Priority.ALWAYS);
        inputBox.setPadding(new Insets(15));
        inputBox.setAlignment(Pos.CENTER);
        inputBox.setStyle("-fx-background-color: #3e3e3e;");

        VBox root = new VBox(scrollPane, inputBox);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        Scene scene = new Scene(root, 900, 800);
        primaryStage.setTitle("Katty Chat");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(700);
        primaryStage.show();

        addMessage(Katty.getInitialGreeting(), false);

        sendButton.setOnAction(e -> handleInput(inputField));
        inputField.setOnAction(e -> handleInput(inputField));
        inputField.requestFocus();
    }

    private void handleInput(TextField inputField) {
        String input = inputField.getText().strip();
        if (input.isEmpty()) {
            return;
        }

        addMessage(input, true);
        String response = Katty.getResponse(input);
        addMessage(response, false);

        inputField.clear();
        if (input.equalsIgnoreCase("bye")) {
            Platform.exit();
        }
    }

    private void addMessage(String text, boolean isUser) {
        Label label = new Label(text);
        label.setFont(Font.font("Monospaced", 15));
        label.setWrapText(true);
        label.setMaxWidth(700);
        label.setPadding(new Insets(15, 20, 15, 20));

        HBox wrapper = new HBox(label);

        if (isUser) {
            wrapper.setAlignment(Pos.CENTER_RIGHT);
            label.setStyle("-fx-background-color: #005c4b; "
                    + "-fx-text-fill: white; "
                    + "-fx-background-radius: 20 20 0 20;");

            HBox.setMargin(label, new Insets(0, 0, 0, 100));
        } else {
            wrapper.setAlignment(Pos.CENTER_LEFT);
            label.setStyle("-fx-background-color: #343f46; "
                    + "-fx-text-fill: #dcdcdc; "
                    + "-fx-background-radius: 20 20 20 0;");

            HBox.setMargin(label, new Insets(0, 100, 0, 0));
        }

        chatContainer.getChildren().add(wrapper);
    }
}