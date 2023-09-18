package src;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

public class Main {

    public static Image radioOff;
    public static Image radioOn;
    public static Image radioConnected;
    public static int radioFrequency = 99;
    public static boolean connected = false;
    public static boolean on = false;
    public static int line[] = new int[50];
    private static JFrame window;
    public static GameClient client;
    private static GamePanel gamePanel = new GamePanel();
    private static GameEngine gameEngine = new GameEngine();
    private static Thread engineThread;
    private static Thread panelThread;
    public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public static void main(String[] args) {
        initImages();
        createWindow();
        startGameThread();

        GameServer server = new GameServer();
        server.start();
        String serverAddress = "localhost";
        client = new GameClient(serverAddress);
        client.start();
    }

    private static void initImages() {
        try {
            radioOff = ImageIO.read(new File("res/radio.png"));
            radioOn = ImageIO.read(new File("res/radio_on.png"));
            radioConnected = ImageIO.read(new File("res/radio_on_connected.png"));
        } catch (Exception e) {

        }
    }

    private static void createWindow() {
        window = new JFrame();
        window.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // din poza (radio) butonul e la coord (394 235) si se termina la 423 263
                // poza se afla la Main.screenSize.width / 2 - 250, Main.screenSize.height / 2 - 150
                // deci butonul se afla de la
                // Main.screenSize.width / 2 - 250 + 394, Main.screenSize.height / 2 - 150 + 235
                // la
                // Main.screenSize.width / 2 - 250 + 423, Main.screenSize.height / 2 - 150 + 263
                if (MouseInfo.getPointerInfo().getLocation().x >= Main.screenSize.width / 2 - 250 + 394 && MouseInfo.getPointerInfo().getLocation().x <= Main.screenSize.width / 2 - 250 + 423) {
                    if (MouseInfo.getPointerInfo().getLocation().y >= Main.screenSize.height / 2 - 150 + 235 && MouseInfo.getPointerInfo().getLocation().y <= Main.screenSize.height / 2 - 150 + 263) {
                        if (on)
                            if (connected)
                                client.disconnect();
                        on = !on;
                    }
                }

                // sagetile
                // stanga (-)
                // 125 234 - 152 265
                // la fel ca la restul
                if (MouseInfo.getPointerInfo().getLocation().x >= Main.screenSize.width / 2 - 250 + 125 && MouseInfo.getPointerInfo().getLocation().x <= Main.screenSize.width / 2 - 250 + 152) {
                    if (MouseInfo.getPointerInfo().getLocation().y >= Main.screenSize.height / 2 - 150 + 234 && MouseInfo.getPointerInfo().getLocation().y <= Main.screenSize.height / 2 - 150 + 265) {
                        if (on) {
                            if (radioFrequency > 10) {
                                if (connected)
                                    client.disconnect();
                                radioFrequency--;
                            }
                        }
                    }
                }

                // dreapta (+)
                // 314 232 - 341 263
                // la fel ca la restul
                if (MouseInfo.getPointerInfo().getLocation().x >= Main.screenSize.width / 2 - 250 + 314 && MouseInfo.getPointerInfo().getLocation().x <= Main.screenSize.width / 2 - 250 + 341) {
                    if (MouseInfo.getPointerInfo().getLocation().y >= Main.screenSize.height / 2 - 150 + 232 && MouseInfo.getPointerInfo().getLocation().y <= Main.screenSize.height / 2 - 150 + 263) {
                        if (on) {
                            if (radioFrequency < 99) {
                                if (connected)
                                    client.disconnect();
                                radioFrequency++;
                            }
                        }
                    }
                }


                // 366 84
                // 428 142
                if (MouseInfo.getPointerInfo().getLocation().x >= Main.screenSize.width / 2 - 250 + 366 && MouseInfo.getPointerInfo().getLocation().x <= Main.screenSize.width / 2 - 250 + 428) {
                    if (MouseInfo.getPointerInfo().getLocation().y >= Main.screenSize.height / 2 - 150 + 84 && MouseInfo.getPointerInfo().getLocation().y <= Main.screenSize.height / 2 - 150 + 142) {
                        if (on) {
                            if (!connected) {
                                client.connect();
                            } else {
                                line[49] = 3;
                                client.ping();
                            }
                        }
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

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
        window.setUndecorated(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Radio");

        initPanel(screenSize);

        window.add(gamePanel);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    private static void initPanel(Dimension size) {
        gamePanel.setPreferredSize(size);
        gamePanel.setBackground(new Color(120, 120, 120));
//        gamePanel.setBackground(new Color(123, 0, 154));
        gamePanel.setDoubleBuffered(true);
        gamePanel.setFocusable(true);
    }

    public static void startGameThread() {
        engineThread = new Thread(gameEngine);
        panelThread = new Thread(gamePanel);
        engineThread.start();
        panelThread.start();
    }


}