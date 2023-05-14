import org.w3c.dom.Text;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class WhiteboardClient extends JFrame {
    private Paint paint;
    private static final String LINE = "Line";
    private static final String CIRCLE = "Circle";
    private static final String OVAL = "Oval";
    private static final String RECTANGLE = "Rectangle";
    private static final String TEXT = "Text";
    public WhiteboardClient() {
        this.paint = new Paint();
        JFrame frame = new JFrame("Whiteboard");
        frame.setLayout(null);
        frame.setSize(1000, 800);

        JButton lineButton = new JButton(LINE);
        JButton circleButton = new JButton(CIRCLE);
        JButton ovalButton = new JButton(OVAL);
        JButton rectangleButton = new JButton(RECTANGLE);
        JButton textButton = new JButton(TEXT);

        lineButton.setBounds(10, 5, 60, 36);
        circleButton.setBounds(80, 5, 60, 36);
        rectangleButton.setBounds(150, 5, 100, 36);
        ovalButton.setBounds(260, 5, 60, 36);
        textButton.setBounds(330, 5, 60, 36);

        ButtonGroup group = new ButtonGroup();
        group.add(circleButton);
        group.add(rectangleButton);
        group.add(lineButton);
        group.add(textButton);
        group.add(ovalButton);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setBounds(0, 0, 450, 40);
        buttonPanel.add(circleButton);
        buttonPanel.add(rectangleButton);
        buttonPanel.add(lineButton);
        buttonPanel.add(textButton);
        buttonPanel.add(ovalButton);

        lineButton.setActionCommand(LINE);
        circleButton.setActionCommand(CIRCLE);
        ovalButton.setActionCommand(OVAL);
        rectangleButton.setActionCommand(RECTANGLE);
        textButton.setActionCommand(TEXT);

        lineButton.addActionListener(paint.Tool_Listener);
        lineButton.doClick();
        circleButton.addActionListener(paint.Tool_Listener);
        ovalButton.addActionListener(paint.Tool_Listener);
        rectangleButton.addActionListener(paint.Tool_Listener);
        textButton.addActionListener(paint.Tool_Listener);

        WhiteboardPanel whiteboardPanel = new WhiteboardPanel(paint);
        whiteboardPanel.setBounds(10, 50 , 980, 600);
        frame.add(buttonPanel);
        frame.add(whiteboardPanel);
        frame.setVisible(true);
    }

    public void setRemoteCanvas(RemoteCanvas remoteCanvas) {
        paint.setRemoteCanvas(remoteCanvas);
    }
}
