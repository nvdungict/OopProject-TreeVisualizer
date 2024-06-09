package views;

import java.io.OutputStream;
import java.io.PrintStream;
import javafx.scene.text.Text;

public class CustomPrintStream extends PrintStream {
    private Text notificationText;

    public CustomPrintStream(OutputStream out, Text notificationText) {
        super(out);
        this.notificationText = notificationText;
    }

    @Override
    public void write(byte[] buf, int off, int len) {
        String message = new String(buf, off, len);
        notificationText.setText(notificationText.getText() + message);
    }
}