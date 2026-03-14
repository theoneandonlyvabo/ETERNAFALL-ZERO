package main;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

public class Main {

    public static void main(String[] args) {

        // [FIX] Paksa Windows timer resolution ke 1ms
        // Default Windows timer = 15.6ms → bikin FPS nggak stabil
        // Thread dummy ini nahan resolusi tinggi selama app jalan
        new Thread(() -> {
            try { Thread.sleep(Integer.MAX_VALUE); }
            catch (InterruptedException e) {}
        }).start();

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

        JFrame window = new JFrame();

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setUndecorated(true);
        window.setSize(screen.width, screen.height);
        window.setLocation(0, 0);

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        window.pack();
        window.setSize(screen.width, screen.height);
        window.setVisible(true);

        gamePanel.loadMap();
        gamePanel.startGameThread();
    }
}