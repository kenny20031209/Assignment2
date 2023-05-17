import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class UserThread extends Thread{
    private Whiteboard whiteboard;
    private String request;

    public UserThread(Whiteboard whiteboard, String request) {
        this.whiteboard = whiteboard;
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
            } else {
                String message = (String) object.get("Response");
                String username = (String) object.get("Username");
                whiteboard.displayMessage(message, username);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
