import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class WhiteboardImpl extends UnicastRemoteObject implements RemoteCanvas {
    private RemoteCanvas remoteCanvas;
    protected WhiteboardImpl() throws RemoteException {
        super();
    }

    public void setRemoteCanvas(RemoteCanvas remoteCanvas){
        this.remoteCanvas = remoteCanvas;
    }

    public RemoteCanvas getRemoteCanvas(){
        return remoteCanvas;
    }

    @Override
    public Line2D.Double drawLine(Point start, Point end) throws RemoteException {
        return new Line2D.Double(start, end);
    }

    @Override
    public Ellipse2D.Double drawCircle(Point start, Point end) throws RemoteException {
        int x = (int) Math.min(start.getX(), end.getX());
        int y = (int) Math.min(start.getY(), end.getY());
        int width = (int) Math.abs(start.getX() - end.getX());
        return new Ellipse2D.Double(x, y, width, width);
    }

    @Override
    public Ellipse2D.Double drawOval(Point start, Point end) throws RemoteException {
        int x = (int) Math.min(start.getX(), end.getX());
        int y = (int) Math.min(start.getY(), end.getY());
        int width = (int) Math.abs(start.getX() - end.getX());
        int height = (int) Math.abs(start.getY() - end.getY());
        return new Ellipse2D.Double(x, y, width, height);
    }

    @Override
    public Rectangle2D.Double drawRectangle(Point start, Point end) throws RemoteException {
        int x = (int) Math.min(start.getX(), end.getX());
        int y = (int) Math.min(start.getY(), end.getY());
        int width = (int) Math.abs(start.getX() - end.getX());
        int height = (int) Math.abs(start.getY() - end.getY());
        return new Rectangle2D.Double(x, y, width, height);
    }
}