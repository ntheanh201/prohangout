package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Notifications extends JFrame {
    public Notifications() {
        Toolkit.getDefaultToolkit().beep();
        String message = "You got a new notification message";
        String header = "ProHangout";
        JFrame frame = new JFrame();
        frame.setSize(300, 125);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1.0f;
        constraints.weighty = 1.0f;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.fill = GridBagConstraints.BOTH;
        JLabel headingLabel = new JLabel(header);
        try {

//            File file = new File("chat.png");
//            FileInputStream fis = new FileInputStream(file);
//            BufferedImage sendImg = ImageIO.read(fis);
            InputStream path = this.getClass().getClassLoader().getResourceAsStream("images/chat.png");
            ImageIcon sendImg= new ImageIcon(ImageIO.read(path));
            headingLabel.setIcon(sendImg);
        } catch (IOException e) {
            System.out.println("error");
        }
        headingLabel.setOpaque(false);
        frame.add(headingLabel, constraints);
        constraints.gridx++;
        constraints.weightx = 0f;
        constraints.weighty = 0f;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.gridx = 0;
        constraints.gridy++;
        constraints.weightx = 1.0f;
        constraints.weighty = 1.0f;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.fill = GridBagConstraints.BOTH;
        JLabel messageLabel = new JLabel(message);
        frame.add(messageLabel, constraints);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);

        Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
        Insets toolHeight = Toolkit.getDefaultToolkit().getScreenInsets(frame.getGraphicsConfiguration());// height of the task bar
        frame.setLocation(scrSize.width - frame.getWidth(), scrSize.height - toolHeight.bottom - frame.getHeight());

        new Thread(() -> {
            try {
                Thread.sleep(3500);
                frame.dispose();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

//    public static void main(String[] args) {
//        new Notifications();
//    }
}