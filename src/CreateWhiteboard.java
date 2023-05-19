import javax.swing.*;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CreateWhiteboard {

    public static void main(String args[]){
        if(args.length != 3){
            System.err.println("Format: java –jar CreateWhiteboard.jar <serverIPAddress> <port> username");
            System.exit(1);
        }

        String serverAddress = args[0];
        int port = Integer.parseInt(args[1]);
        String managerName = args[2];

        Whiteboard whiteboard = new Whiteboard(true);
        try {
            Registry registry = LocateRegistry.getRegistry(serverAddress, port);
            RemoteCanvas remoteCanvas = (RemoteCanvas) registry.lookup("RemoteCanvas");
            RemoteUser remoteUser = (RemoteUser) registry.lookup( "RemoteUser");
            whiteboard.setRemoteCanvas(remoteCanvas);
            whiteboard.setRemoteUser(remoteUser);

            ConnectionSocket socket = new ConnectionSocket(serverAddress, 1000);
            Connection connection = new Connection(socket);
            ChatWindow chatWindow = new ChatWindow();
            whiteboard.setConnection(connection);
            chatWindow.setConnection(connection);
            connection.managerConnect(whiteboard, chatWindow, managerName);

            while (true) {
                String request = socket.receive();
                ManagerThread managerThread = new ManagerThread(whiteboard, chatWindow, request, socket);
                managerThread.start();
            }
        } catch (RemoteException e) {
            System.err.println("Fail to get server port!");
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "You close the whiteboard and socket is closed!");
        }
    }
}
