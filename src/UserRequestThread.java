import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserRequestThread extends Thread {
    private ConnectionSocket socket;
    private Manager manager;
    private RemoteUser remoteUser;

    public UserRequestThread(ConnectionSocket socket, Manager manager, RemoteUser remoteUser) {
        this.socket = socket;
        this.manager = manager;
        this.remoteUser = remoteUser;
    }

    public void run() {
        super.run();
        JSONParser parser = new JSONParser();
        JSONObject object = new JSONObject();

        String request;
        while (true) {
            try{
                request = socket.receive();
                try{
                    object = (JSONObject) parser.parse(request);
                } catch (ParseException e){
                    e.printStackTrace();
                }

                String requestType = (String) object.get("Request");
                switch (requestType) {
                    case Connection.createWhiteboard -> {
                        String managerName = manager.addUser((String) object.get("Manager Name"));
                        manager.setManagerName(managerName);
                        manager.addUserSocket(managerName, socket);
                        socket.createWhiteboard(managerName);
                        break;
                    }
                    case Connection.joinWhiteboard -> {
                        ConnectionSocket connectionSocket = manager.getManagerConnectionSocket();
                        String username = manager.addWaitingName((String) object.get("Username"));
                        manager.addUserSocket(username, socket);
                        connectionSocket.joinRequest(username);
                        break;
                    }
                    case Connection.AskJoinResult -> {
                        String waitingName = (String) object.get("Username");
                        boolean result = Boolean.parseBoolean((String) object.get("Result"));
                        ConnectionSocket waitingSocket = manager.getConnectionSocket(waitingName);
                        assert waitingSocket != null;
                        if (result) {
                            waitingSocket.joinResult("Response", Connection.Joined, true, waitingName);
                            manager.acceptWaitingName(waitingName);
                        } else {
                            waitingSocket.joinResult("Response", Connection.Rejected, false, waitingName);
                            manager.rejectWaitingName(waitingName);
                        }
                        break;
                    }
                    case Connection.kickOutUser -> {
                        String kickOutName = (String) object.get("Username");
                        ConnectionSocket kickSocket = manager.getConnectionSocket(kickOutName);
                        if (kickSocket != null) {
                            kickSocket.kickOutRequest();
                        } else {
                            JOptionPane.showMessageDialog(null, "Please input a valid username!");
                        }
                        manager.removeUser(kickOutName);
                        break;
                    }
                    case Connection.userClose -> {
                        String leaveUserName = (String) object.get("Username");
                        manager.removeUser(leaveUserName);
                        break;
                    }
                    case Connection.managerClose -> {
                        manager.showManagerAction(Connection.managerClose);
                        manager.clear();
                        break;
                    }
                    default -> {
                        String message = (String) object.get("Request");
                        String username = (String) object.get("Username");
                        List<String> names = remoteUser.getUsernames();
                        List<ConnectionSocket> messageUserSockets = new ArrayList<>();
                        ConnectionSocket messageManagerSocket = manager.getManagerConnectionSocket();
                        for (String name: names) {
                            ConnectionSocket messageUserSocket = manager.getConnectionSocket(name);
                            messageUserSockets.add(messageUserSocket);
                        }
                        for (ConnectionSocket messageSocket: messageUserSockets) {
                            messageSocket.receiveMessage(message, username);
                        }
                        messageManagerSocket.receiveMessage(message, username);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
