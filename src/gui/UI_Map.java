package gui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class UI_Map {

    GamePanel gp;
    BufferedImage mapImg;

    public UI_Map(GamePanel gp) {
        this.gp = gp;

        try {
            mapImg = ImageIO.read(getClass().getResourceAsStream("/gui/UI_map.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        if (mapImg == null) return;
        g2.drawImage(mapImg, 0, 0, null);
        // TODO: render map content
    }
}