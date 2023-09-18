package src;

public class GameEngine implements Runnable {
    @Override
    public void run() {
        double drawInterval = 1000000000 / 10;
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
                update();
                delta--;
            }
            if (timer >= 1000000000) {
                timer = 0;
            }
        }
    }

    private void update() {
        try {
            //update
            // move from right to left
            for (int i = 0; i < 49; i++) {
                Main.line[i] = Main.line[i + 1];
            }
        } catch (Exception e) {
            System.out.println("Tried updating we failed");
            e.printStackTrace();
        }
    }
}