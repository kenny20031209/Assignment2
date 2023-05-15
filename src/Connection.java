import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class Connection {
    private ConnectionSocket socket;
    public static final String USER_REQUEST = "request";
    public static final String CREATE_WHITEBOARD = "Create Whiteboard";
    public static final String USERNAME = "username";
    public static final String RESPONSE = "response";
    public static final String SUCCESSFUL_CREATE_WHITEBOARD = "Successful create Whiteboard";

    public Connection(ConnectionSocket socket){
        this.socket = socket;
    }

    public void Connect(Whiteboard whiteboard, String username){
        JSONObject object = new JSONObject();
        JSONParser parser = new JSONParser();

        object.put(USER_REQUEST, CREATE_WHITEBOARD);
        object.put(USERNAME, username);
        String response = " ";
        try {
            socket.send(object.toJSONString());
            response = socket.receive();
            object = (JSONObject) parser.parse(response);
            String responseType = (String) object.get(RESPONSE);
            if (responseType == SUCCESSFUL_CREATE_WHITEBOARD){

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
