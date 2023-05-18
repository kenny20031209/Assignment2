import javax.swing.*;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class JoinWhiteboard {
    private static String userName;
    public static void main(String args[]){
        userName = "David";

        Whiteboard whiteboard = new Whiteboard(false);
        ChatWindow chatWindow = new ChatWindow();
        try{
            Registry registry = LocateRegistry.getRegistry("localhost",1233);
            RemoteCanvas remoteCanvas = (RemoteCanvas) registry.lookup("RemoteCanvas");
            RemoteUser remoteUser = (RemoteUser) registry.lookup("RemoteUser");

            whiteboard.setRemoteCanvas(remoteCanvas);
            whiteboard.setRemoteUser(remoteUser);

            ConnectionSocket socket = new ConnectionSocket("localhost", 1235);
            Connection connection = new Connection(socket);
            whiteboard.setConnection(connection);
            chatWindow.setConnection(connection);
            JOptionPane.showMessageDialog(null, "Please wait for manager approval");
            connection.userConnect(whiteboard, chatWindow, userName);
            System.out.println("Joined");

            while(true) {
                String request = socket.receive();
                UserThread thread = new UserThread(whiteboard, chatWindow, request);
                thread.start();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
