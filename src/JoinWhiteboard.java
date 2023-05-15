import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class JoinWhiteboard {
    public static void main(String args[]){
        Whiteboard client = new Whiteboard();
        try{
            Registry registry = LocateRegistry.getRegistry("localhost",1233);
            RemoteCanvas remoteCanvas = (RemoteCanvas) registry.lookup("RemoteCanvas");
            client.setRemoteCanvas(remoteCanvas);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }
}
