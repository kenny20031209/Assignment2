import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
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
    private JLabel label;
    private Connection connection;
    private boolean result = false;
    private static final String LINE = "Line";
    private static final String CIRCLE = "Circle";
    private static final String OVAL = "Oval";
    private static final String RECTANGLE = "Rectangle";
    private static final String TEXT = "Text";
    private static final Color[] colors = {Color.BLACK, Color.BLUE, Color.CYAN, Color.DARK_GRAY,
            Color.GRAY, Color.GREEN, Color.LIGHT_GRAY, Color.MAGENTA,
            Color.ORANGE, Color.PINK, Color.RED, new Color(0,0,128), Color.YELLOW,
            new Color(128, 0, 128), new Color(0, 128, 128), new Color(128, 128, 0)};
    public Whiteboard(boolean isManager) {
        this.paint = new Paint();
        this.isManager = isManager;
        frame = new JFrame("Whiteboard");
        frame.setLayout(null);
        frame.setSize(1000, 1500);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int choice;
                choice = JOptionPane.showConfirmDialog(null, "Confirm to exit?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if(choice == JOptionPane.YES_OPTION){
                    System.exit(0);
                }
            }
        });

        if (this.isManager) {
            JMenuBar menuBar = new JMenuBar();
            frame.setJMenuBar(menuBar);
            JMenu fileMenu = new JMenu("Manager Access");

            this.connection = null;
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
                    frame.dispose();
                    System.exit(0);
                }
            });

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
        whiteboardPanel.setBounds(10, 50 , 980, 500);
        frame.add(buttonPanel);
        frame.add(whiteboardPanel);
        frame.setVisible(true);
    }

    public void setRemoteCanvas(RemoteCanvas remoteCanvas) {
        paint.setRemoteCanvas(remoteCanvas);
    }
    public void setRemoteUser(RemoteUser remoteUser) {
        this.remoteUser = remoteUser;

        JTextArea jTextArea1 = new JTextArea();
        jTextArea1.setBounds(10, 580, 120, 50);
        jTextArea1.setEditable(false);
        JTextArea jTextArea2 = new JTextArea();
        jTextArea2.setBounds(10, 650, 120, 250);
        jTextArea2.setEditable(false);
        jTextArea2.append("User:" + "\n");
        frame.add(jTextArea1);
        frame.add(jTextArea2);

        updateUserThread = new Thread() {
            public void run() {
                super.run();

                while (true) {
                    try{
                        String managerName = remoteUser.getManagerName();
                        jTextArea1.setText("Manager:" + "\n" + managerName);

                        StringBuilder usernameList = new StringBuilder();
                        for(String string: remoteUser.getUsernames()) {
                            usernameList.append(string).append("\n");
                        }
                        if(!usernameList.toString().equals(jTextArea2.getText())){
                            jTextArea2.append(usernameList.toString());
                        }
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };

        updateUserThread.start();
    }

    public boolean isManager() {
        return isManager;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void initial(String username) {
        this.username = username;
    }

    public void rejected() {
        JOptionPane.showMessageDialog(null, "You are rejected by manager!");
        updateUserThread.interrupt();
        System.exit(0);
    }

    public void kickOut() {
        JOptionPane.showMessageDialog(null, "You are kicked out by the manager!");
        frame.dispose();
        System.exit(0);
    }

    public void managerClose() {
        JOptionPane.showMessageDialog(null, "The manager is closing the frame!!!");
        connection.managerDisconnect(username);
        frame.dispose();
        System.exit(0);
    }

    public boolean askAcceptWaitingName(String waitingName) {
        label = new JLabel();
        String message = waitingName + " want to join the whiteboard";
        label.setText(message);
        label.setBounds(80,6, 400, 40);

        JButton confirm = new JButton("Accept");
        confirm.setBounds(100,40,80,20);
        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                result = true;
                frame.setEnabled(true);
            }
        });

        JButton reject = new JButton("Reject");
        reject.setBounds(270,40,80,20);
        reject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                result = false;
                frame.setEnabled(true);
            }
        });

        JDialog dialog = new JDialog(frame, true);
        frame.setEnabled(false);
        dialog.setLayout(null);
        dialog.add(label);
        dialog.add(confirm);
        dialog.add(reject);
        dialog.pack();
        dialog.setSize(new Dimension(500, 150));
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);

        return result;
    }
}
