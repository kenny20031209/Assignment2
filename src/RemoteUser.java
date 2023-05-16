import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RemoteUser extends Remote {
    List<String> getUsernames() throws RemoteException;
    String getManagerName() throws RemoteException;
    void addUsername(String username) throws RemoteException;
    void setManagerName(String managerName) throws RemoteException;
}
