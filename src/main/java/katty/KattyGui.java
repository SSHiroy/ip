package katty;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * GUI for Katty
 */
public class KattyGui extends Application {

    @Override
    public void start(Stage primaryStage) {
        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setFont(Font.font("Monospaced"));
        outputArea.setStyle("-fx-control-inner-background: #1e1e1e; -fx-text-fill: #dcdcdc;");
        outputArea.setWrapText(false);

        TextField inputField = new TextField();
        inputField.setFont(Font.font("Monospaced"));
        inputField.setStyle("-fx-control-inner-background: #2e2e2e; -fx-text-fill: #ffffff;");

        Button sendButton = new Button("Send");
        sendButton.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white;");

        HBox inputBox = new HBox(5, inputField, sendButton);
        HBox.setHgrow(inputField, Priority.ALWAYS);
        inputBox.setStyle("-fx-padding: 5px; -fx-background-color: #3e3e3e;");
        inputBox.setMaxHeight(30);

        VBox root = new VBox(outputArea, inputBox);
        VBox.setVgrow(outputArea, Priority.ALWAYS);
        root.setStyle("-fx-padding: 10px; -fx-background-color: #2b2b2b; -fx-spacing: 5px;");

        Scene scene = new Scene(root, 750, 750, Color.BLACK);
        primaryStage.setTitle("Katty GUI");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Display the initial greeting and load data
        outputArea.setText(Katty.getInitialGreeting());

        sendButton.setOnAction(e -> sendInput(outputArea, inputField));
        inputField.setOnAction(e -> sendInput(outputArea, inputField));

        primaryStage.setOnCloseRequest(e -> Platform.exit());
        inputField.requestFocus();
    }

    private void sendInput(TextArea outputArea, TextField inputField) {
        String input = inputField.getText().strip();
        if (input.isEmpty()) {
            return;
        }

        String response = Katty.getResponse(input);

        outputArea.appendText("\nUser: " + input + "\n");
        outputArea.appendText(response + "\n");
        outputArea.setScrollTop(Double.MAX_VALUE);
        inputField.clear();

        if (input.equalsIgnoreCase("bye")) {
            Platform.exit();
        }
    }
}
