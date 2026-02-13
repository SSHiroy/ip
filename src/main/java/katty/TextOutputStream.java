package katty;

import java.io.OutputStream;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

class TextOutputStream extends OutputStream {

    private final TextArea textArea;
    private final StringBuilder buffer = new StringBuilder();

    public TextOutputStream(TextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void write(int b) {
        char c = (char) b;
        buffer.append(c);

        if (c == '\n') {
            flushBuffer();
        }
    }

    @Override
    public void write(byte[] b, int off, int len) {
        String text = new String(b, off, len);
        buffer.append(text);

        int index;
        while ((index = buffer.indexOf("\n")) != -1) {
            String line = buffer.substring(0, index + 1);
            appendToTextArea(line);
            buffer.delete(0, index + 1);
        }
    }

    private void flushBuffer() {
        if (!buffer.isEmpty()) {
            appendToTextArea(buffer.toString());
            buffer.setLength(0);
        }
    }

    private void appendToTextArea(String text) {
        Platform.runLater(() -> textArea.appendText(text));
    }
}
