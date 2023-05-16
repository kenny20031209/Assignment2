import javax.net.ServerSocketFactory;
import java.awt.image.TileObserver;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class WhiteboardServer{
    public static void main(String[] args) throws Exception {
        Manager manager = new Manager();
        try{
            RemoteCanvas remoteCanvas = new WhiteboardImpl();
            RemoteUser remoteUser = new WhiteboardImpl();
            Registry registry = LocateRegistry.createRegistry(1233);
            registry.rebind("RemoteCanvas", remoteCanvas);
            registry.rebind("RemoteUser", remoteUser);

            System.out.println("RMI is ready!");
            manager.setRemoteUser(remoteUser);
        } catch (RemoteException e) {
            System.out.println("RMI connection fails!");
        }

        ServerSocketFactory factory = ServerSocketFactory.getDefault();
        try(ServerSocket server = factory.createServerSocket(1235)) {
            System.out.println("Socket is ready");
            while (true) {
                Socket socket = server.accept();
                UserRequestThread thread = new UserRequestThread(new ConnectionSocket(socket), manager);
                thread.start();
            }
        } catch (IOException e) {
            System.out.println("Fail to create server socket!");
        }
    }
}
