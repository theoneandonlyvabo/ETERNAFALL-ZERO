package main;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

public class Main {
    
    public static void main(String[] args) {

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

        JFrame window = new JFrame();
        
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setUndecorated(true); // hapus title bar & border
        window.setSize(screen.width, screen.height);
        window.setLocation(0, 0);

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        window.pack();
        window.setSize(screen.width, screen.height); // pack() bisa override, reset lagi
        window.setVisible(true);

        gamePanel.loadMap();
        gamePanel.startGameThread();
    }

}