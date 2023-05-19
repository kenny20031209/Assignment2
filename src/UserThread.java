import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;

public class UserThread extends Thread{
    private Whiteboard whiteboard;
    private ChatWindow chatWindow;
    private String request;

    public UserThread(Whiteboard whiteboard, ChatWindow chatWindow,String request) {
        this.whiteboard = whiteboard;
        this.chatWindow = chatWindow;
        this.request = request;
    }

    public void run() {
        super.run();
        JSONObject object;
        JSONParser parser = new JSONParser();

        try {
            object = (JSONObject) parser.parse(request);

            if (Connection.kickOutUser.equals((String) object.get("Request"))) {
                whiteboard.kickOut();
            } else if (Connection.managerClose.equals((String) object.get("Request"))) {
                whiteboard.managerClose();
            } else if (Connection.openFile.equals((String) object.get("Request"))) {
                JOptionPane.showMessageDialog(null, "Manager open a new file!");
            } else {
                String message = (String) object.get("Response");
                String username = (String) object.get("Username");
                chatWindow.showMessage(message, username);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
