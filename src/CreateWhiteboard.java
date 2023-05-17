import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CreateWhiteboard {
    private static String managerName = "Kenny";

    public static void main(String args[]){
        Whiteboard whiteboard = new Whiteboard(true);
        try{
            Registry registry = LocateRegistry.getRegistry("localhost",1233);
            RemoteCanvas remoteCanvas = (RemoteCanvas) registry.lookup("RemoteCanvas");
            RemoteUser remoteUser = (RemoteUser) registry.lookup( "RemoteUser");
            whiteboard.setRemoteCanvas(remoteCanvas);
            whiteboard.setRemoteUser(remoteUser);

            ConnectionSocket socket = new ConnectionSocket("localhost", 1235);
            Connection connection = new Connection(socket);
            whiteboard.setConnection(connection);
            connection.managerConnect(whiteboard, managerName);
            System.out.println("Created!");

            while (true) {
                String request = socket.receive();
                ManagerThread managerThread = new ManagerThread(whiteboard, request, socket);
                managerThread.start();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error receive join requests from server");
        }
    }
}
