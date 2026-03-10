package gui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class HUD {

    GamePanel gp;
    BufferedImage statsBar;
    BufferedImage itemSlot;

    public HUD(GamePanel gp) {
        this.gp = gp;

        try {

            statsBar = ImageIO.read(getClass().getResourceAsStream("/gui/HUD_stats_bar.png"));
            itemSlot = ImageIO.read(getClass().getResourceAsStream("/gui/HUD_item_slot.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {

        g2.drawImage(statsBar, 30, 30, null);
        g2.drawImage(itemSlot, 30, gp.screenHeight - itemSlot.getHeight() - 30, null);
        
    }
}