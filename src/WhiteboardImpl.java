import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class WhiteboardImpl extends UnicastRemoteObject implements RemoteCanvas {
    private SerializableImage img;

    public WhiteboardImpl() throws RemoteException{
        img = new SerializableImage(new BufferedImage(600, 500, BufferedImage.TYPE_INT_ARGB));
    }

    public SerializableImage getImage() throws RemoteException {
        return img;
    }

    public void makeText(String text, int x, int y) throws RemoteException {
        Graphics graphics = img.getImage().getGraphics();
        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font("Arial", Font.PLAIN,20));
        graphics.drawString(text, x, y);
    }
    @Override
    public void makeLine(int x1, int y1, int x2, int y2) throws RemoteException {
        Graphics graphics = img.getImage().getGraphics();
        graphics.setColor(Color.BLACK);
        graphics.drawLine(x1, y1, x2, y2);
    }

    @Override
    public void makeCircle(int x, int y, int width, int height) throws RemoteException {
        Graphics graphics = img.getImage().getGraphics();
        graphics.setColor(Color.BLACK);
        graphics.drawOval(x, y, width, height);
    }

    @Override
    public void makeOval(int x, int y, int width, int height) throws RemoteException {
        Graphics graphics = img.getImage().getGraphics();
        graphics.setColor(Color.BLACK);
        graphics.drawOval(x, y, width, height);
    }

    @Override
    public void makeRectangle(int x, int y, int width, int height) throws RemoteException {
        Graphics graphics = img.getImage().getGraphics();
        graphics.setColor(Color.BLACK);
        graphics.drawRect(x, y, width, height);
    }

    public void setImage(SerializableImage image) throws RemoteException {
        img = image;
    }

    public void clear() throws RemoteException {
        img.clear();
    }
}