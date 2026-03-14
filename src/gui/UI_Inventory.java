package gui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class UI_Inventory {

    GamePanel gp;
    BufferedImage inventoryImg;

    public UI_Inventory(GamePanel gp) {
        this.gp = gp;

        try {
            inventoryImg = ImageIO.read(getClass().getResourceAsStream("/gui/UI_inventory.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        if (inventoryImg == null) return;
        g2.drawImage(inventoryImg, 0, 0, null);
        // TODO: render item slots
    }
}