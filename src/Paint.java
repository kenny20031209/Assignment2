import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.rmi.RemoteException;

public class Paint {
    private String shapeType;
    private Color shapeColor;
    private RemoteCanvas remoteCanvas;

    public final ActionListener shapeListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            shapeType = e.getActionCommand();
        }
    };

    public Paint(){}
    public String getShapeType() {
        return shapeType;
    }

    public void setColor(Color color) {
        shapeColor = color;
    }

    public void drawText(String text, Point start) {
        try{
            remoteCanvas.makeText(text, start.x, start.y, shapeColor);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void drawLine(Point start, Point end) {
        try{
            remoteCanvas.makeLine(start.x, start.y, end.x, end.y, shapeColor);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void drawCircle(Point start, Point end) {
        int radius = (int) Math.sqrt(Math.pow(end.getX()- start.getX(), 2) + Math.pow(end.getY() - start.getY(), 2));

        try{
            remoteCanvas.makeCircle(start.x, start.y, radius,shapeColor);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void drawOval(Point start, Point end) {
        int x1 = (int) Math.min(start.getX(), end.getX());
        int y1 = (int) Math.min(start.getY(), end.getY());
        int width = (int) Math.abs(start.getX() - end.getX());
        int height = (int) Math.abs(start.getY() - end.getY());

        try{
            remoteCanvas.makeOval(x1, y1, width, height, shapeColor);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void drawRectangle(Point start, Point end) {
        int x1 = (int) Math.min(start.getX(), end.getX());
        int y1 = (int) Math.min(start.getY(), end.getY());
        int width = (int) Math.abs(start.getX() - end.getX());
        int height = (int) Math.abs(start.getY() - end.getY());

        try{
            remoteCanvas.makeRectangle(x1, y1, width, height, shapeColor);
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
}
