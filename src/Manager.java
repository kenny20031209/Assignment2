import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Manager {
    private Map<String, ConnectionSocket> userSocket;
    private String managerName = null;
    private RemoteUser remoteUser;
    private List<String> waitingNames;

    public Manager() {
        userSocket = new HashMap<>();
        waitingNames = new ArrayList<>();
    }

    public void setRemoteUser(RemoteUser remoteUser) {
        this.remoteUser = remoteUser;
    }

    public synchronized String addNewUser(String username) {
        try{
            if (managerName == null) {
                managerName = username;
                remoteUser.setManagerName(managerName);
            } else {
                remoteUser.addUsername(username);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return username;
    }

    public synchronized void removeUser(String username) {
        try {
            if (remoteUser.getUsernames().contains(username)) {
                remoteUser.getUsernames().remove(username);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if(userSocket.containsKey(username)) {
            try{
                userSocket.get(username).close();
            } catch (IOException e){
                e.printStackTrace();
            }
            userSocket.remove(username);
        }
    }

    public synchronized String addWaitingName(String waitingName) {
        waitingNames.add(waitingName);
        return waitingName;
    }

    public synchronized void acceptWaitingName(String waitingName) {
        if (waitingNames.contains(waitingName)) {
            waitingNames.remove(waitingName);
            try{
                remoteUser.addUsername(waitingName);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void rejectWaitingName(String waitingName) {
        if (waitingNames.contains(waitingName)) {
            waitingNames.remove(waitingName);
            userSocket.remove(waitingName);
        }
    }

    public synchronized void addUserSocket(String username, ConnectionSocket socket) {
        if(!userSocket.containsKey(username)) {
            userSocket.put(username, socket);
        } else {
            System.out.println("This user has socket!");
        }
    }

    public synchronized ConnectionSocket getConnectionSocket(String username){
        if (userSocket.containsKey(username)) {
            return userSocket.get(username);
        } else {
            return null;
        }
    }

    public synchronized ConnectionSocket getManagerConnectionSocket() {
        return userSocket.get(managerName);
    }

    public synchronized void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public synchronized void clear() {
        userSocket.clear();
        managerName = null;
        try {
            remoteUser.getUsernames().clear();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        waitingNames.clear();
    }

    public synchronized void showManagerAction(String action) {
        userSocket.forEach((username, socket) -> {
            if (socket.isClosed()) {
                System.out.println(username + " socket has already closed");
            } else if (!username.equals(managerName)) {
                System.out.println(username + " sent with manager operation: " + action);
                try {
                    socket.managerAction(action);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
