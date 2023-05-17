import org.json.simple.JSONObject;
import java.io.*;
import java.net.Socket;

public class ConnectionSocket {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;


    public ConnectionSocket(String serverAdd, int serverPort){
        try{
            this.socket = new Socket(serverAdd, serverPort);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ConnectionSocket(Socket socket) {
        try{
            this.socket = socket;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String request) throws IOException {
        System.out.println("Send: " + request);
        out.writeUTF(request);
        out.flush();
    }

    public String receive() throws IOException {
        String receive = in.readUTF();
        System.out.println("Receive: " + receive);
        return receive;
    }

    public void close() throws IOException {
        socket.close();
        in.close();
        out.close();
        System.out.println("Socket is closed!");
    }

    public void createWhiteboard(String username) throws IOException {
        JSONObject object = new JSONObject();
        object.put("Response", Connection.Created);
        object.put("Manager Name", username);
        System.out.println("Send: " + object);
        out.writeUTF(object.toJSONString());
        out.flush();
    }

    public void joinRequest(String waitingName) throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Request", Connection.AskJoinWhiteboard);
        jsonObject.put("Waiting Name", waitingName);
        System.out.println("Send: " + jsonObject);
        out.writeUTF(jsonObject.toJSONString());
        out.flush();
    }

    public void joinResult(String keyType, String key, boolean result, String waitingName) throws IOException {
        JSONObject object = new JSONObject();
        object.put(keyType, key);
        object.put("Username", waitingName);
        object.put("Result", Boolean.toString(result));
        System.out.println("Send: " + object);
        out.writeUTF(object.toJSONString());
        out.flush();
    }

    public void kickOutRequest() throws IOException {
        JSONObject object = new JSONObject();
        object.put("Request", Connection.kickOutUser);
        System.out.println("Send: " + object);
        out.writeUTF(object.toJSONString());
        out.flush();
    }

    public void receiveMessage(String message, String username) throws IOException {
        JSONObject object = new JSONObject();
        object.put("Response", message);
        object.put("Username", username);
        System.out.println("Send: " + object);
        out.writeUTF(object.toJSONString());
        out.flush();
    }
}
