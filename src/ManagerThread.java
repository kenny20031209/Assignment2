import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class ManagerThread extends Thread {
    private Whiteboard whiteboard;
    private String request;
    private ConnectionSocket socket;

    public ManagerThread(Whiteboard whiteboard, String request, ConnectionSocket socket){
        this.whiteboard = whiteboard;
        this.request = request;
        this.socket = socket;
    }

    public void run() {
        super.run();
        JSONObject object;
        JSONParser parser = new JSONParser();

        try {
            object = (JSONObject) parser.parse(request);
            if (Connection.AskJoinWhiteboard.equals((String) object.get("Request"))) {
                String waitingName = (String) object.get("Waiting Name");
                boolean result = whiteboard.askAcceptWaitingName(waitingName);
                socket.joinResult("Request", Connection.AskJoinResult, result, waitingName);
            } else {
                String message = (String) object.get("Response");
//                String username = (String) object.get("Username");
                whiteboard.displayMessage(message);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
