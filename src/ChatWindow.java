import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatWindow {
    private Connection connection = null;
    private String username;
    JFrame frame;
    JTextArea area;

    public void start(String username) {
        frame.setVisible(true);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public ChatWindow(){
        frame = new JFrame("Chat Window (" + username + ")");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        area = new JTextArea();
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(400, 40));
        field.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connection.sendMessage(field.getText().trim(), username);
                field.setText("");
            }
        });

        JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(field, BorderLayout.SOUTH);
        frame.setSize(500, 300);
        frame.setVisible(true);
    }

    public void showMessage(String message, String username) {
        String messageFormat = String.format("%s (%s)", message, username);
        area.append(messageFormat + "\n");
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
