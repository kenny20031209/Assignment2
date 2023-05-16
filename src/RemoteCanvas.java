import java.awt.*;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteCanvas extends Remote{
    SerializableImage getImage() throws RemoteException;
    void makeText(String text, int x, int y, Color color) throws RemoteException;
    void makeLine(int x1, int y1, int x2, int y2, Color color) throws RemoteException;
    void makeCircle(int x, int y, int radius, Color color) throws RemoteException;
    void makeOval(int x, int y, int width, int height, Color color) throws RemoteException;
    void makeRectangle(int x, int y, int width, int height, Color color) throws RemoteException;
    void setImage(SerializableImage image) throws RemoteException;
    void clear() throws RemoteException;
}
