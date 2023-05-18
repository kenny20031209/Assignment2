import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class WhiteboardImpl extends UnicastRemoteObject implements RemoteCanvas, RemoteUser {
    private SerializableImage img;
    private String managerName;
    private List<String> usernames;

    public WhiteboardImpl() throws RemoteException{
        img = new SerializableImage(new BufferedImage(750, 500, BufferedImage.TYPE_INT_ARGB));
        usernames = new ArrayList<>();
    }

    public SerializableImage getImage() throws RemoteException {
        return img;
    }

    public void makeText(String text, int x, int y, Color color) throws RemoteException {
        Graphics graphics = img.getImage().getGraphics();
        graphics.setColor(color);
        graphics.setFont(new Font("Arial", Font.PLAIN,15));
        graphics.drawString(text, x, y);
    }
    @Override
    public void makeLine(int x1, int y1, int x2, int y2, Color color) throws RemoteException {
        Graphics2D graphics = (Graphics2D) img.getImage().getGraphics();
        graphics.setColor(color);
        Shape line = new Line2D.Double(x1, y1, x2, y2);
        graphics.draw(line);
    }

    @Override
    public void makeCircle(int x, int y, int radius, Color color) throws RemoteException {
        Graphics2D graphics = (Graphics2D) img.getImage().getGraphics();
        graphics.setColor(color);
        Shape circle = new Ellipse2D.Double(x - radius, y - radius, radius *2, radius *2);
        graphics.draw(circle);
    }

    @Override
    public void makeOval(int x, int y, int width, int height, Color color) throws RemoteException {
        Graphics2D graphics = (Graphics2D) img.getImage().getGraphics();
        graphics.setColor(color);
        Shape oval = new Ellipse2D.Double(x, y, width, height);
        graphics.draw(oval);
    }

    @Override
    public void makeRectangle(int x, int y, int width, int height, Color color) throws RemoteException {
        Graphics2D graphics = (Graphics2D) img.getImage().getGraphics();
        graphics.setColor(color);
        Shape rectangle = new Rectangle2D.Double(x, y, width, height);
        graphics.draw(rectangle);
    }

    public void setImage(SerializableImage image) throws RemoteException {
        img = image;
    }

    public void clear() throws RemoteException {
        img.clear();
    }

    @Override
    public List<String> getUsernames() throws RemoteException {
        return usernames;
    }

    @Override
    public String getManagerName() throws RemoteException {
        return managerName;
    }

    @Override
    public void addUsername(String username) throws RemoteException {
        usernames.add(username);
    }

    @Override
    public void setManagerName(String managerName) throws RemoteException {
        this.managerName = managerName;
    }
}