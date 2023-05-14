import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.rmi.RemoteException;

public class Paint {
    private String shapeType;
    private RemoteCanvas remoteCanvas;
    Point startPoint, endPoint;

    public final ActionListener Tool_Listener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            shapeType = e.getActionCommand();
        }
    };

    public Paint(){}
    public String getShapeType() {
        return shapeType;
    }

    public void drawText(String text, Point start) {
        try{
            remoteCanvas.makeText(text, start.x, start.y);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void drawLine(Point start, Point end) {
        try{
            remoteCanvas.makeLine(start.x, start.y, end.x, end.y);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void drawCircle(Point start, Point end) {
        int x = (int) Math.min(start.getX(), end.getX());
        int y = (int) Math.min(start.getY(), end.getY());
        int width = (int) Math.abs(start.getX() - end.getX());

        try{
            remoteCanvas.makeCircle(x, y, width, width);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void drawOval(Point start, Point end) {
        int x = (int) Math.min(start.getX(), end.getX());
        int y = (int) Math.min(start.getY(), end.getY());
        int width = (int) Math.abs(start.getX() - end.getX());
        int height = (int) Math.abs(start.getY() - end.getY());

        try{
            remoteCanvas.makeOval(x, y, width, height);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void drawRectangle(Point start, Point end) {
        int x = (int) Math.min(start.getX(), end.getX());
        int y = (int) Math.min(start.getY(), end.getY());
        int width = (int) Math.abs(start.getX() - end.getX());
        int height = (int) Math.abs(start.getY() - end.getY());

        try{
            remoteCanvas.makeRectangle(x, y, width, height);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void setRemoteCanvas(RemoteCanvas remoteCanvas) {
        this.remoteCanvas = remoteCanvas;
    }

    public RemoteCanvas getRemoteCanvas() {
        return remoteCanvas;
    }

    public void setImage(BufferedImage image) {
        try{
            remoteCanvas.setImage(new SerializableImage(image));
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    public void clearCanvas() {
        try {
            remoteCanvas.clear();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
