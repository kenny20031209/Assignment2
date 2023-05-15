import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteCanvas extends Remote{
//    Line2D.Double drawLine(Point start, Point end) throws RemoteException;
//    Ellipse2D.Double drawCircle(Point start, Point end) throws RemoteException;
//    Ellipse2D.Double drawOval(Point start, Point end) throws RemoteException;
//    Rectangle2D.Double drawRectangle(Point start, Point end) throws RemoteException;
    SerializableImage getImage() throws RemoteException;
    void makeText(String text, int x, int y) throws RemoteException;
    void makeLine(int x1, int y1, int x2, int y2, Color color) throws RemoteException;
    void makeCircle(int x, int y, int radius) throws RemoteException;
    void makeOval(int x, int y, int width, int height) throws RemoteException;
    void makeRectangle(int x, int y, int width, int height) throws RemoteException;
    void setImage(SerializableImage image) throws RemoteException;
}
