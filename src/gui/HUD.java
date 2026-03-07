package gui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class HUD {

    GamePanel gp;
    BufferedImage playerStats;

    public HUD(GamePanel gp) {
        this.gp = gp;

        try {
            playerStats = ImageIO.read(getClass().getResourceAsStream("/gui/HUD_player_stats.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        g2.drawImage(playerStats, 30, 30, null);
    }
}