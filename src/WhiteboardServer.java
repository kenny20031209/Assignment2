import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class WhiteboardServer{
    public static void main(String[] args) throws Exception {
        RemoteCanvas remoteCanvas = new WhiteboardImpl();
        Registry registry = LocateRegistry.createRegistry(1233);
        registry.rebind("RemoteCanvas", remoteCanvas);
        System.out.println("RMI is ready!");
    }
}
