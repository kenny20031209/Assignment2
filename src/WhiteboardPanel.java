import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class WhiteboardPanel extends JPanel implements MouseListener, MouseMotionListener{
    private WhiteboardImpl whiteboardImpl;
    private ArrayList<ColoredShape> shapes;
    Shape shape;
    private Color shapeColor;
    private Point startPoint, endPoint;
    private int shapeType;
    private static final int LINE = 0;
    private static final int CIRCLE = 1;
    private static final int OVAL = 2;
    private static final int RECTANGLE = 3;
    private static final int TEXT = 4;
    private static final Color[] colors = {Color.BLACK, Color.BLUE, Color.CYAN, Color.DARK_GRAY,
            Color.GRAY, Color.GREEN, Color.LIGHT_GRAY, Color.MAGENTA,
            Color.ORANGE, Color.PINK, Color.RED, Color.WHITE, Color.YELLOW,
            new Color(128, 0, 128), new Color(0, 128, 128), new Color(128, 128, 0)};

    public WhiteboardPanel(WhiteboardImpl whiteboardImpl){
        this.whiteboardImpl = whiteboardImpl;
        setBackground(Color.WHITE);
        shapes = new ArrayList<>();
        shapeColor = Color.BLACK;
        shapeType = LINE;
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        for (ColoredShape coloredShape : shapes) {
            g2d.setColor(coloredShape.getColor());
            g2d.draw(coloredShape.getShape());
        }
    }

    public void setColor(Color color) {
        shapeColor = color;
    }

    public void setShapeType(int type) {
        shapeType = type;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        JTextField textField = new JTextField();
        if (shapeType == TEXT) {
            textField.setBounds((int) startPoint.getX(), (int) startPoint.getY(), 100, 20);
            add(textField);
            textField.requestFocus();
        }
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        startPoint = e.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        endPoint = e.getPoint();
        try{
            switch (shapeType) {
                case CIRCLE:
                    shape = whiteboardImpl.drawCircle(startPoint, endPoint);
                    break;
                case OVAL:
                    shape = whiteboardImpl.drawOval(startPoint, endPoint);
                    break;
                case RECTANGLE:
                    shape = whiteboardImpl.drawRectangle(startPoint,endPoint);
                    break;
                default:
                    shape = whiteboardImpl.drawLine(startPoint, endPoint);
            }
            shapes.add(new ColoredShape(shape,shapeColor));
        }
        catch(RemoteException exception){
                exception.printStackTrace();
        }
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    public static void main(String[] args) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry("localhost",1234);
        RemoteCanvas canvas = (RemoteCanvas) registry.lookup("Whiteboard");
        WhiteboardImpl whiteboard = new WhiteboardImpl();
        whiteboard.setRemoteCanvas(canvas);

        JFrame frame = new JFrame("Whiteboard");
        WhiteboardPanel board = new WhiteboardPanel(new WhiteboardImpl());

        board.setPreferredSize(new Dimension(600, 500));
        JPanel buttonPanel = new JPanel();
        JButton lineButton = new JButton("Line");
        JButton circleButton = new JButton("Circle");
        JButton ovalButton = new JButton("Oval");
        JButton rectangleButton = new JButton("Rectangle");
        JButton textButton = new JButton("Text");
        buttonPanel.add(lineButton);
        buttonPanel.add(circleButton);
        buttonPanel.add(ovalButton);
        buttonPanel.add(rectangleButton);
        buttonPanel.add(textButton);
        String[] colorNames = {"Black", "Blue", "Cyan", "Dark Gray", "Gray", "Green", "Light Gray",
                "Magenta", "Orange", "Pink", "Red", "White", "Yellow", "Purple",
                "Teal", "Olive"};
        JComboBox<String> colorComboBox = new JComboBox<>(new DefaultComboBoxModel<>(colorNames));
        colorComboBox.setPreferredSize(new Dimension(130, 18));
        colorComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = colorComboBox.getSelectedIndex();
                if (index >= 0 && index < colors.length) {
                    board.setColor(colors[index]);
                }
            }
        });
        buttonPanel.add(colorComboBox);
        lineButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.setShapeType(LINE);
            }
        });
        circleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.setShapeType(CIRCLE);
            }
        });
        ovalButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.setShapeType(OVAL);
            }
        });
        rectangleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.setShapeType(RECTANGLE);
            }
        });
        textButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.setShapeType(TEXT);
            }
        });
        frame.add(board, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.NORTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
