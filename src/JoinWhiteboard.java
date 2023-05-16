import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class JoinWhiteboard {
    private static String userName;
    public static void main(String args[]){
        userName = "123";

        Whiteboard whiteboard = new Whiteboard(false);
        try{
            Registry registry = LocateRegistry.getRegistry("localhost",1233);
            RemoteCanvas remoteCanvas = (RemoteCanvas) registry.lookup("RemoteCanvas");
            RemoteUser remoteUser = (RemoteUser) registry.lookup("RemoteUser");

            whiteboard.setRemoteCanvas(remoteCanvas);
            whiteboard.setRemoteUser(remoteUser);

            ConnectionSocket socket = new ConnectionSocket("localhost", 1233);
            Connection connection = new Connection(socket);
            whiteboard.setConnection(connection);
            connection.userConnect(whiteboard, userName);
            System.out.println("Whiteboard joined");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }
}
