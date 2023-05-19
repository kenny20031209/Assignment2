import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;

import static javax.swing.JOptionPane.showInputDialog;

public class Whiteboard extends JFrame {
    private Paint paint;
    private boolean isManager;
    private String username;
    private int counter = 0;
    JFrame frame;
    private Thread updateUserThread;
    private RemoteUser remoteUser;
    private Connection connection;
    private boolean result;
    private static final String LINE = "Line";
    private static final String CIRCLE = "Circle";
    private static final String OVAL = "Oval";
    private static final String RECTANGLE = "Rectangle";
    private static final String TEXT = "Text";
    private static final Color[] colors = {Color.BLACK, Color.BLUE, Color.CYAN, Color.DARK_GRAY,
            Color.GRAY, Color.GREEN, Color.LIGHT_GRAY, Color.MAGENTA,
            Color.ORANGE, Color.PINK, Color.RED, new Color(0,0,128), Color.YELLOW,
            new Color(128, 0, 128), new Color(0, 128, 128), new Color(128, 128, 0)};

    public void initial(String username) {
        frame.setVisible(true);
        this.username = username;
    }

    public Whiteboard(boolean isManager) {
        this.paint = new Paint();
        this.isManager = isManager;
        frame = new JFrame("Whiteboard");
        frame.setLayout(null);
        frame.setSize(1000, 900);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(isManager) {
                    paint.clearWhiteboard();
                    connection.managerDisconnect(username);
                } else {
                    connection.userDisconnect(username);
                }
                updateUserThread.interrupt();
                e.getWindow().dispose();
                System.exit(0);
            }
        });

        if (this.isManager) {
            JMenuBar menuBar = new JMenuBar();
            frame.setJMenuBar(menuBar);
            JMenu fileMenu = new JMenu("Manager Access");
            this.connection = null;

            JMenuItem newWhiteboard = new JMenuItem("New");
            newWhiteboard.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                }
            });
            JMenuItem open = new JMenuItem("Open");
            open.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser fileChooser = new JFileChooser();
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("Image", "png");
                    fileChooser.setFileFilter(filter);
                    int openDialog = fileChooser.showOpenDialog(frame);
                    if (openDialog == JFileChooser.APPROVE_OPTION) {
                        File file = fileChooser.getSelectedFile();
                        try {
                            FileInputStream fileInputStream = new FileInputStream(file);
                        } catch (FileNotFoundException ex) {
                            ex.printStackTrace();
                        }
                        BufferedImage image = null;
                        try {
                            image = ImageIO.read(file);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        paint.setImage(image);
                        if (connection != null) {
                            connection.notifyUser("Open File");
                        }
                    }
                }
            });
            JMenuItem save = new JMenuItem("Save");
            save.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String filePath = "image.png";
                    paint.saveImage(filePath);
                    JOptionPane.showMessageDialog(null, "Save the image successfully!");
                }
            });
            JMenuItem saveAs = new JMenuItem("Save As");
            saveAs.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser fileChooser = new JFileChooser();
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("Image", "png");
                    String imageName = String.format("%s(%d)", "image", counter);
                    fileChooser.setCurrentDirectory(new File("/haofengchen/Desktop/" + imageName));
                    counter ++;

                    fileChooser.setFileFilter(filter);
                    int result = fileChooser.showSaveDialog(null);
                    if(result == JFileChooser.APPROVE_OPTION) {
                        File file = fileChooser.getSelectedFile();
                        String fileName = file.getPath();
                        paint.saveImage(fileName);
                        JOptionPane.showMessageDialog(null, "Save the image successfully!");
                    }
                }
            });
            JMenuItem kickOut = new JMenuItem("Kick Out");
            kickOut.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String kickOutName = showInputDialog("Please input a username to kick out:");
                    if (kickOutName != null && !kickOutName.isEmpty()) {
                        try {
                            if (kickOutName.equals(remoteUser.getManagerName())) {
                                JOptionPane.showMessageDialog(null, "You are the manager!");
                            } else {
                                connection.kickOut(kickOutName);
                            }
                        } catch (RemoteException exception) {
                            exception.printStackTrace();
                        }
                    }
                }
            });
            JMenuItem close = new JMenuItem("Close");
            close.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    paint.clearWhiteboard();
                    connection.managerDisconnect(username);
                    updateUserThread.interrupt();
                    frame.dispose();
                    System.exit(0);
                }
            });

            fileMenu.add(newWhiteboard);
            fileMenu.add(open);
            fileMenu.add(save);
            fileMenu.add(saveAs);
            fileMenu.add(kickOut);
            fileMenu.add(close);
            menuBar.add(fileMenu);
        }

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
        colorComboBox.setBounds(400, 5, 130, 40);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setBounds(0, 0, 550, 40);
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

        lineButton.addActionListener(paint.shapeListener);
        lineButton.doClick();
        circleButton.addActionListener(paint.shapeListener);
        ovalButton.addActionListener(paint.shapeListener);
        rectangleButton.addActionListener(paint.shapeListener);
        textButton.addActionListener(paint.shapeListener);
        paint.setColor(colors[0]);
        colorComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = colorComboBox.getSelectedIndex();
                if (index >= 0 && index < colors.length) {
                    paint.setColor(colors[index]);
                }
            }
        });

        WhiteboardPanel whiteboardPanel = new WhiteboardPanel(paint);
        whiteboardPanel.setBounds(10, 50 , 750, 500);
        frame.add(buttonPanel);
        frame.add(whiteboardPanel);
    }

    public void setRemoteCanvas(RemoteCanvas remoteCanvas) {
        paint.setRemoteCanvas(remoteCanvas);
    }

    public void setRemoteUser(RemoteUser remoteUser) {
        this.remoteUser = remoteUser;

        JLabel jLabel1 = new JLabel();
        jLabel1.setBounds(820, 50, 100, 20);
        jLabel1.setText("Manager:");
        JTextArea jTextArea1 = new JTextArea();
        jTextArea1.setBounds(820, 80, 100, 40);
        jTextArea1.setEditable(false);
        JLabel jLabel2 = new JLabel();
        jLabel2.setBounds(820, 150, 100, 20);
        jLabel2.setText("User:");
        JTextArea jTextArea2 = new JTextArea();
        jTextArea2.setBounds(820, 180, 100, 300);
        jTextArea2.setEditable(false);
        frame.add(jLabel1);
        frame.add(jTextArea1);
        frame.add(jLabel2);
        frame.add(jTextArea2);

        updateUserThread = new Thread() {
            public void run() {
                super.run();

                while (true) {
                    try{
                        String managerName = remoteUser.getManagerName();
                        jTextArea1.setText(managerName);

                        StringBuilder usernameList = new StringBuilder();
                        for(String string: remoteUser.getUsernames()) {
                            usernameList.append(string).append("\n");
                        }
                        if(!usernameList.toString().equals(jTextArea2.getText())){
                            jTextArea2.setText(usernameList.toString());
                        }
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        System.out.println("Thread fail to sleep!");
                        break;
                    }
                }
            }
        };

        updateUserThread.start();
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void rejected() {
        JOptionPane.showMessageDialog(null, "You are rejected by manager!");
        updateUserThread.interrupt();
        System.exit(0);
    }

    public void kickOut() {
        updateUserThread.interrupt();
        JOptionPane.showMessageDialog(null, "You are kicked out by the manager!");
        connection.userDisconnect(username);
        frame.dispose();
        System.exit(0);
    }

    public void managerClose() {
        JOptionPane.showMessageDialog(null, "The manager is closing the frame!!!");
        updateUserThread.interrupt();
        if (isManager){
            connection.managerDisconnect(username);
        } else {
            connection.userDisconnect(username);
        }
        frame.dispose();
        System.exit(0);
    }

    public boolean askAcceptWaitingName(String waitingName) {
        int choice;
        choice = JOptionPane.showConfirmDialog(null, waitingName + " want to join the whiteboard", "Join Request", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            result = true;
            frame.setEnabled(true);
        } else {
            result = false;
            frame.setEnabled(false);
        }
        return result;
    }
}
