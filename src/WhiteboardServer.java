import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class WhiteboardServer{
    public static void main(String[] args) throws Exception {
        WhiteboardImpl whiteboard = new WhiteboardImpl();
        Registry registry = LocateRegistry.createRegistry(1234);
        registry.rebind("Whiteboard", whiteboard);
        System.out.println("RMI is ready!");
    }
}
