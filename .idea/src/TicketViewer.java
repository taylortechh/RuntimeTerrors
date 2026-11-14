import javax.swing.*;
import java.awt.Font;

public class TicketViewer {
    public static void show(String ticketId, String data) {
        JTextArea area = new JTextArea(data);
        area.setEditable(false);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        area.setCaretPosition(0);

        JFrame frame = new JFrame("Ticket " + ticketId + " (read-only)");
        frame.add(new JScrollPane(area));
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }
}