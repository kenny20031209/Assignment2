import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class WhiteboardServer{
    public static void main(String[] args){
        Manager manager = new Manager();

        try{
            RemoteCanvas remoteCanvas = new WhiteboardImpl();
            RemoteUser remoteUser = new WhiteboardImpl();
            Registry registry = LocateRegistry.createRegistry(1233);
            registry.rebind("RemoteCanvas", remoteCanvas);
            registry.rebind("RemoteUser", remoteUser);
            manager.setRemoteUser(remoteUser);
            System.out.println("RMI is ready!");


            ServerSocketFactory factory = ServerSocketFactory.getDefault();
            try(ServerSocket server = factory.createServerSocket(1235)) {
                System.out.println("Socket is ready");
                while (true) {
                    Socket socket = server.accept();
                    UserRequestThread userRequestThread = new UserRequestThread(new ConnectionSocket(socket), manager, remoteUser);
                    userRequestThread.start();
                }
            } catch (IOException e) {
                System.out.println("Fail to create server socket!");
            }
        } catch (RemoteException e) {
            System.out.println("RMI connection fails!");
        }
    }
}
