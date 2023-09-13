import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

public class Main {

    public static Image radioOff;
    public static Image radioOn;
    public static int radioFrequency = 88;

    public static boolean on = false;
    private static JFrame window;
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
        GameClient client = new GameClient(serverAddress);
        client.start();
        client.ping();
    }

    private static void initImages() {
        try {
            radioOff = ImageIO.read(new File("C:\\Users\\elev\\IdeaProjects\\Radio\\res\\radio.png"));
            radioOn = ImageIO.read(new File("C:\\Users\\elev\\IdeaProjects\\Radio\\res\\radio_on.png"));
        } catch (Exception e) {

        }
    }

    private static void createWindow() {
        window = new JFrame();
        window.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (MouseInfo.getPointerInfo().getLocation().x >= 827 && MouseInfo.getPointerInfo().getLocation().x <= 856) {
                    if (MouseInfo.getPointerInfo().getLocation().y >= 469 && MouseInfo.getPointerInfo().getLocation().y <= 496) {
                        if (on) {
                            on = false;
                        } else {
                            on = true;
                        }
                    }
                }

                if (MouseInfo.getPointerInfo().getLocation().x >= 800 && MouseInfo.getPointerInfo().getLocation().x <= 860) {
                    if (MouseInfo.getPointerInfo().getLocation().y >= 320 && MouseInfo.getPointerInfo().getLocation().y <= 380) {
                        System.out.println("clicked button");
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