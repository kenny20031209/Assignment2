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
            String reqType = (String) object.get("Request");

            if (reqType == Connection.kickOutUser) {
                whiteboard.kickOut();
            } else if (reqType == Connection.managerClose) {
                whiteboard.managerClose();
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
