import java.awt.*;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteCanvas extends Remote{
    SerializableImage getImage() throws RemoteException;
    void makeText(String text, int startX, int startY, Color color) throws RemoteException;
    void makeLine(int startX, int startY, int endX, int endY, Color color) throws RemoteException;
    void makeCircle(int startX, int startY, int radius, Color color) throws RemoteException;
    void makeOval(int startX, int startY, int endX, int endY, Color color) throws RemoteException;
    void makeRectangle(int startX, int startY, int endX, int endY, Color color) throws RemoteException;
    void setImage(SerializableImage image) throws RemoteException;
    void clear() throws RemoteException;
}
