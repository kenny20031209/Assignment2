import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Manager {
    private HashMap<String, ConnectionSocket> userSocket;
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

    public synchronized String addUser(String username) {
        try{
            if(managerName == null) {
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

    public synchronized String addWaitingName(String username) {
        waitingNames.add(username);
        return username;
    }

    public synchronized void acceptWaitingName(String username) {
        if (waitingNames.contains(username)) {
            waitingNames.remove(username);
            try{
                remoteUser.addUsername(username);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void rejectWaitingName(String username) {
        if (waitingNames.contains(username)) {
            waitingNames.remove(username);
            userSocket.remove(username);
        }
    }

    public synchronized void removeUser(String username) {
        try {
            if(remoteUser.getUsernames().contains(username)) {
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

    public synchronized void addUserSocket(String username, ConnectionSocket socket) {
        if(!userSocket.containsKey(username)) {
            userSocket.put(username, socket);
        } else {
            System.out.println("This user has socket!");
        }
    }

    public synchronized ConnectionSocket getConnectionSocket(String username){
        return userSocket.getOrDefault(username, null);
    }

    public synchronized ConnectionSocket getManagerConnectionSocket() {
        return userSocket.get(managerName);
    }

    public synchronized String getManagerName() {
        return managerName;
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
}
