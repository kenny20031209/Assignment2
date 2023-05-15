import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class WhiteboardClient extends JFrame {
    private Paint paint;
    private static final String LINE = "Line";
    private static final String CIRCLE = "Circle";
    private static final String OVAL = "Oval";
    private static final String RECTANGLE = "Rectangle";
    private static final String TEXT = "Text";
    private static final Color[] colors = {Color.BLACK, Color.BLUE, Color.CYAN, Color.DARK_GRAY,
            Color.GRAY, Color.GREEN, Color.LIGHT_GRAY, Color.MAGENTA,
            Color.ORANGE, Color.PINK, Color.RED, new Color(0,0,128), Color.YELLOW,
            new Color(128, 0, 128), new Color(0, 128, 128), new Color(128, 128, 0)};
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
        String[] colorNames = {"Black", "Blue", "Cyan", "Dark Gray", "Gray", "Green", "Light Gray",
                "Magenta", "Orange", "Pink", "Red", "Navy", "Yellow", "Purple",
                "Teal", "Olive"};
        JComboBox<String> colorComboBox = new JComboBox<>(new DefaultComboBoxModel<>(colorNames));

        lineButton.setBounds(10, 5, 60, 36);
        circleButton.setBounds(80, 5, 60, 36);
        rectangleButton.setBounds(150, 5, 100, 36);
        ovalButton.setBounds(260, 5, 60, 36);
        textButton.setBounds(330, 5, 60, 36);
        colorComboBox.setBounds(400, 5, 100, 36);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setBounds(0, 0, 500, 40);
        buttonPanel.add(circleButton);
        buttonPanel.add(rectangleButton);
        buttonPanel.add(lineButton);
        buttonPanel.add(textButton);
        buttonPanel.add(ovalButton);
        buttonPanel.add(colorComboBox);

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
        colorComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = colorComboBox.getSelectedIndex();
                if (index >= 0 && index < colors.length) {
                    paint.setColor(colors[index]);
                }
            }
        });


        WhiteboardPanel whiteboardPanel = new WhiteboardPanel(paint);
        whiteboardPanel.setBounds(10, 50 , 980, 800);
        frame.add(buttonPanel);
        frame.add(whiteboardPanel);
        frame.setVisible(true);
    }

    public void setRemoteCanvas(RemoteCanvas remoteCanvas) {
        paint.setRemoteCanvas(remoteCanvas);
    }
}
