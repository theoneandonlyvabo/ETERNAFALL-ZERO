package gui;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class UI_Paused {

    GamePanel gp;
    BufferedImage pausedTitle;

    public UI_Paused(GamePanel gp) {
        this.gp = gp;

        try {

            pausedTitle = ImageIO.read(getClass().getResourceAsStream("/gui/UI_paused.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {

        g2.drawImage(pausedTitle, 0, 0, null);
        
    }
    
}
