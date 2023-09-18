package src;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel implements Runnable {
    @Override
    public void run() {
        double drawInterval = (double) 1000000000 / 60;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;

        while (true) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += currentTime - lastTime;
            lastTime = currentTime;

            if (delta >= 1) {
                repaint();
                delta--;
            }
            if (timer >= 1000000000) {
                timer = 0;
            }
        }
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        try {
            if (Main.on) {
                g2.setColor(Color.green);
                g2.setFont(new Font("TimesRoman", Font.PLAIN, 30));
                if (Main.connected) {
                    g2.drawImage((BufferedImage) Main.radioConnected, null, Main.screenSize.width / 2 - 250, Main.screenSize.height / 2 - 150);
                } else {
                    g2.drawImage((BufferedImage) Main.radioOn, null, Main.screenSize.width / 2 - 250, Main.screenSize.height / 2 - 150);
                }
                // where do I draw frequency?
                // on image with radio frequency monitor is from 169 229 to 295 269 (126x40 pixels)
                // radio image is at Main.screenSize.width / 2 - 250, Main.screenSize.height / 2 - 150
                // so, we add monitor position + size/2
                // at height we add some font buffer
                // at width we subtract one character worth of length
                // currently we are at Main.screenSize.width / 2 - 250 + 169 + 63, Main.screenSize.height / 2 - 150 + 229 + 20 + 10
                g2.drawString(String.valueOf(Main.radioFrequency), Main.screenSize.width / 2 - 250 + 169 + 63 - 15, Main.screenSize.height / 2 - 150 + 229 + 20 + 10);
            } else {
                g2.drawImage((BufferedImage) Main.radioOff, null, Main.screenSize.width / 2 - 250, Main.screenSize.height / 2 - 150);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Tried drawing we failed");
        }
        g2.dispose();
    }

}
