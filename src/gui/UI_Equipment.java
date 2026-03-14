package gui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class UI_Equipment {

    GamePanel gp;
    BufferedImage equipmentImg;

    public UI_Equipment(GamePanel gp) {
        this.gp = gp;

        try {
            equipmentImg = ImageIO.read(getClass().getResourceAsStream("/gui/UI_equipment.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        if (equipmentImg == null) return;
        g2.drawImage(equipmentImg, 0, 0, null);
        // TODO: render slot contents
    }
}
