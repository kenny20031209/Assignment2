import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteCanvas extends Remote{
    Line2D.Double drawLine(Point start, Point end) throws RemoteException;
    Ellipse2D.Double drawCircle(Point start, Point end) throws RemoteException;
    Ellipse2D.Double drawOval(Point start, Point end) throws RemoteException;
    Rectangle2D.Double drawRectangle(Point start, Point end) throws RemoteException;

}
