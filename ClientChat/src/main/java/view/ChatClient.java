package view;

import model.User;
import socket.ChatAccess;
import utils.ScreenResolution;
import utils.ServerIP;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;


public class ChatClient {
    /**
     * Chat client UI
     */
    static class ChatFrame extends JFrame implements Observer {

        private JLabel emojiLabel;
        private JLabel flowerLabel;
        private JPanel jPanel;
        private JPanel jPanelSide;
        private JLabel usernameLabel;
        private JLabel settingsLabel;
        private JTextPane textPane;
        private JTextField inputTextField;
        private JButton sendButton;
        private ChatAccess chatAccess;
        private JScrollPane jScrollPane;
        private JLabel exitLabel;
        ScreenResolution screenResolution = new ScreenResolution();

        InputStream path = this.getClass().getClassLoader().getResourceAsStream("images/background.jpg");
        Image img2 = ImageIO.read(path);

        public ChatFrame(ChatAccess chatAccess) throws IOException {
            this.chatAccess = chatAccess;
            chatAccess.addObserver(this);
            buildGUI();
        }

        /**
         * Builds the user interface
         */
        private void buildGUI() throws IOException {
            jPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(img2, 0, 0, null);
                }
            };
            jPanel.setVisible(true);
            jPanel.setLayout(null);
            jPanel.getAutoscrolls();
            jPanel.setBounds(100, 0, screenResolution.getWidth() - 100, screenResolution.getHeight());
            this.add(jPanel);

            buildJPanelSide();
            chatGUI();

            this.validate();
            this.repaint();
        }

        public void buildJPanelSide() throws IOException {
            jPanelSide = new JPanel() {
                @Override
                protected void paintComponent(Graphics grphcs) {
                    super.paintComponent(grphcs);
                    Graphics2D g2d = (Graphics2D) grphcs;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
                    GradientPaint gp = new GradientPaint(0, 0,
                            getBackground().brighter().brighter(), 0, getHeight(),
                            getBackground().darker());
                    g2d.setPaint(gp);
                    g2d.fillRect(0, 0, getWidth(), getHeight());

                }
            };

            jPanelSide.setLayout(null);
            jPanelSide.setVisible(true);
            jPanelSide.setBackground(Color.decode("#3e78df"));
            jPanelSide.setBounds(0, 0, 100, screenResolution.getHeight());
            this.add(jPanelSide);

            setUsernameLabel();
            setSettingsLabel();
            setFlowerLabel();
            setExit();
            setEmojiLabel();

        }

        public void setEmojiLabel() throws IOException {
            emojiLabel = new JLabel();
            InputStream path = this.getClass().getClassLoader().getResourceAsStream("images/emoji.png");
            ImageIcon imgThisImg= new ImageIcon(ImageIO.read(path));
            emojiLabel.setIcon(imgThisImg);
            emojiLabel.setBounds(32, screenResolution.getHeight() - 160, 32, 32);
            emojiLabel.setToolTipText("Emoji for ProHangout");
            emojiLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            emojiLabel.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {

                }

                @Override
                public void mousePressed(MouseEvent e) {
                    try {
                        new EmojiGuide();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
            jPanelSide.add(emojiLabel);
        }

        public void setExit() throws IOException {
            exitLabel = new JLabel();
            InputStream path = this.getClass().getClassLoader().getResourceAsStream("images/door.png");
            ImageIcon imgThisImg= new ImageIcon(ImageIO.read(path));
            exitLabel.setIcon(imgThisImg);
            exitLabel.setToolTipText("Exit");
            exitLabel.setBounds(32, screenResolution.getHeight() - 60, 32, 32);
            exitLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            exitLabel.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {

                }

                @Override
                public void mousePressed(MouseEvent e) {
                    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
                    int dialogButton = JOptionPane.YES_NO_OPTION;
                    int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure want to quit?", "NTA", dialogButton);
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        System.exit(0);
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });

            jPanelSide.add(exitLabel);
        }

        public static BufferedImage resize(BufferedImage img, int newW, int newH) {
            Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
            BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = dimg.createGraphics();
            g2d.drawImage(tmp, 0, 0, null);
            g2d.dispose();
            return dimg;
        }

        public void setUsernameLabel() throws IOException {
            usernameLabel = new JLabel();

            User user = new User();
            ImageIcon imgThisImg;
            try {
//                BufferedImage img = ImageIO.read(new URL(("http://s2bzen.me/theanhdz/images/" + user.getUsername() + ".png")));
//                InputStream path = this.getClass().getClassLoader().getResourceAsStream("avatars/" + user.getUsername() + ".png");
                String path = "/Users/macbook/Downloads/TheAnh/PTIT/NetworkProg/java-swing-prohangout/ClientChat/src/main/resources/avatars/" + user.getUsername() + ".png";
                BufferedImage img = ImageIO.read(new File(path));
                BufferedImage bufferedImage = resize((img), 64, 64);
                imgThisImg = new ImageIcon(bufferedImage);
            } catch (Exception e){
                InputStream path = this.getClass().getClassLoader().getResourceAsStream("images/user.png");
                imgThisImg= new ImageIcon(ImageIO.read(path));
            }

            usernameLabel.setBounds(18, 18, 64, 64);
            usernameLabel.setToolTipText("Get more information.");
            usernameLabel.setIcon(imgThisImg);
            usernameLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            usernameLabel.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {

                }

                @Override
                public void mousePressed(MouseEvent e) {
                    try {
                        new UsernameInformation();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });

            jPanelSide.add(usernameLabel);
        }

        public void setSettingsLabel() throws IOException {
            settingsLabel = new JLabel();
            InputStream path = this.getClass().getClassLoader().getResourceAsStream("images/settings.png");
            ImageIcon imgThisImg= new ImageIcon(ImageIO.read(path));
            settingsLabel.setBounds(32, screenResolution.getHeight() - 110, 32, 32);
            settingsLabel.setToolTipText("About Author");
            settingsLabel.setIcon(imgThisImg);
            settingsLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            settingsLabel.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {

                }

                @Override
                public void mousePressed(MouseEvent e) {
                    try {
                        new UserPopup();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
            jPanelSide.add(settingsLabel);
        }

        public void setFlowerLabel() throws IOException {
            flowerLabel = new JLabel("", JLabel.CENTER);
            InputStream path = this.getClass().getClassLoader().getResourceAsStream("images/label.png");
            ImageIcon imgThisImg= new ImageIcon(ImageIO.read(path));
            flowerLabel.setIcon(imgThisImg);
            flowerLabel.setBounds(300, 0, 300, 80);
            jPanel.add(flowerLabel);
        }

        public void chatGUI() throws IOException {

            textPane = new JTextPane();
            textPane.setEditable(false);

//            textArea = new JTextArea();
//            textArea.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
//            textArea.setEditable(false);
//            textArea.setLineWrap(true);
//            textArea.setWrapStyleWord(true);
//            jPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);
            jPanel.add(new JScrollPane(textPane), BorderLayout.CENTER);

//            jScrollPane = new JScrollPane(textArea);
            jScrollPane = new JScrollPane(textPane);
            jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            jScrollPane.setBounds(10, 80, jPanel.getWidth() - 20, jPanel.getHeight() - 150);

            setFlowerLabel();

            inputTextField = new JTextField();
            inputTextField.setBounds(5, jPanel.getHeight() - 70, jPanel.getWidth() - 100, 50);
            sendButton = new JButton("Send");
            sendButton.setBounds(jPanel.getWidth() - 105, jPanel.getHeight() - 70, 100, 50);

            ActionListener sendListener = e -> {
                String str = inputTextField.getText();
                if (str != null && str.trim().length() > 0)
                    chatAccess.send(str);
                inputTextField.selectAll();
                inputTextField.requestFocus();
                inputTextField.setText("");
            };
            inputTextField.addActionListener(sendListener);

            try {
                InputStream path = this.getClass().getClassLoader().getResourceAsStream("images/chat.png");
                BufferedImage sendImg = ImageIO.read(path);
                sendButton.setIcon(new ImageIcon(sendImg));
            } catch (IOException e) {
                System.out.println("error");
            }
            sendButton.addActionListener(sendListener);

            this.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    chatAccess.close();
                }
            });

            jPanel.add(inputTextField);
            jPanel.add(sendButton);
            jPanel.add(jScrollPane);
        }

        public void addColoredText(JTextPane pane, String text, Color color) {
            StyledDocument doc = pane.getStyledDocument();

            Style style = pane.addStyle("Color Style", null);

            StyleConstants.setForeground(style, color);
            StyleConstants.setFontSize(style, 14);
            try {
                doc.insertString(doc.getLength(), text, style);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }

        public void addIcon(JTextPane pane, String text) throws IOException {
            InputStream path;
            if (text.equals(":3")) {
                path = this.getClass().getClassLoader().getResourceAsStream("icons/cat.png");
            } else if (text.equals(":v")) {
                path = this.getClass().getClassLoader().getResourceAsStream("icons/pacman.png");
            } else if (text.equals(":)")) {
                path = this.getClass().getClassLoader().getResourceAsStream("icons/smiley.png");
            } else if (text.equals(":(")) {
                path = this.getClass().getClassLoader().getResourceAsStream("icons/cry.png");
            } else if (text.equals("o.O")) {
                path = this.getClass().getClassLoader().getResourceAsStream("icons/oO.png");
            } else if (text.equals(":poop:")) {
                path = this.getClass().getClassLoader().getResourceAsStream("icons/poop.png");
            } else if (text.equals("(^^^)")) {
                path = this.getClass().getClassLoader().getResourceAsStream("icons/shark.png");
            } else if (text.equals("-_-")) {
                path = this.getClass().getClassLoader().getResourceAsStream("icons/squint.png");
            } else if (text.equals("<(')")) {
                path = this.getClass().getClassLoader().getResourceAsStream("icons/penguine.png");
            } else if (text.equals("><")) {
                path = this.getClass().getClassLoader().getResourceAsStream("icons/mean.png");
            } else if (text.equals(":kiss:")) {
                path = this.getClass().getClassLoader().getResourceAsStream("icons/kiss.png");
            } else if (text.equals("(y)")) {
                path = this.getClass().getClassLoader().getResourceAsStream("icons/like.png");
            } else if (text.equals(":love:")) {
                path = this.getClass().getClassLoader().getResourceAsStream("icons/love.png");
            } else if (text.equals("<3")) {
                path = this.getClass().getClassLoader().getResourceAsStream("icons/heart.png");
            } else if (text.equals(":crysmiley:")) {
                path = this.getClass().getClassLoader().getResourceAsStream("icons/khoc_cuoi.png");
            } else if (text.equals(":nervous:")) {
                path = this.getClass().getClassLoader().getResourceAsStream("icons/nervous.png");
            }
            else{
                path = this.getClass().getClassLoader().getResourceAsStream("icons/khoc_cuoi.png");
            }
            ImageIcon imgThisImg= new ImageIcon(ImageIO.read(path));
            pane.insertIcon(imgThisImg);
        }

        public void update(Observable o, Object arg) {
            final Object finalArg = arg;
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    //pop up by kinn :)))
                    String line = finalArg.toString();

                    String separate = line, text="";
                    boolean isEmoji = false;
                    String[] words = separate.split(" ");
                    for (int i = 0; i < words.length; i++) {
                        if (words[i].equals("Ω:v") || words[i].equals(":3") || words[i].equals(":)") || words[i].equals(":(") || words[i].equals("o.O") || words[i].equals(":poop:")
                                || words[i].equals("(^^^)") || words[i].equals("-_-") || words[i].equals("<(')") || words[i].equals("><") || words[i].equals(":kiss:") || words[i].equals("(y)")
                                || words[i].equals(":love:") || words[i].equals("<3") || words[i].equals(":crysmiley:") || words[i].equals(":nervous:")
                        ) {
                            isEmoji = true;
                            text = words[i];
                            words[i] = "";
                        }
                    }
                    String ans = "";
                    for (String i : words) {
                        ans += (i + " ");
                    }
//                    textPane.setFont(new java.awt.Font("Arial", Font.PLAIN, 15));
                    if (line.startsWith("Tôi:")) {
                        addColoredText(textPane, ans, Color.BLACK);
                    } else if (ans.startsWith("***") || ans.startsWith("Welcome") || ans.startsWith("To")) {
                        addColoredText(textPane, ans, Color.red);
                    } else {
                        addColoredText(textPane, ans, Color.BLUE);
                        new Notifications();
                    }

                    if (isEmoji) {
                        try {
                            addIcon(textPane, text);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    addColoredText(textPane, "\n", Color.red);

                }
            });
        }
    }

    public void main(String[] args) throws IOException {
        String server = args[0];
        int port = ServerIP.chatPort;
        ChatAccess access = new ChatAccess();

        JFrame frame = new ChatFrame(access);

        try {
            InputStream path = this.getClass().getClassLoader().getResourceAsStream("images/hangouts.png");
            BufferedImage sendImg = ImageIO.read(path);
            frame.setIconImage(sendImg);
        } catch (IOException e) {
            System.out.println("Error");
        }

        ScreenResolution screenResolution = new ScreenResolution();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(screenResolution.getWidth(), screenResolution.getHeight());
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setTitle("ProHangout");
        frame.setResizable(false);

        try {
            access.InitSocket(server, port);
        } catch (IOException ex) {
            System.out.println("Cannot connect to " + server);
            ex.printStackTrace();
            System.exit(0);
        }
    }
}