package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyH;

    public Player(GamePanel gp, KeyHandler keyH) {
        
        this.gp = gp;
        this.keyH = keyH;

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        x = 100;
        y = 100;
        speed = 4;
        direction = "down";
    }

    public void getPlayerImage() {

        try {

            up1 = ImageIO.read(getClass().getResourceAsStream("/assets/player/efplayer_u_1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/assets/player/efplayer_u_2.png"));
            up3 = ImageIO.read(getClass().getResourceAsStream("/assets/player/efplayer_u_3.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/assets/player/efplayer_d_1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/assets/player/efplayer_d_2.png"));
            down3 = ImageIO.read(getClass().getResourceAsStream("/assets/player/efplayer_d_3.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/assets/player/efplayer_l_1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/assets/player/efplayer_l_2.png"));
            left3 = ImageIO.read(getClass().getResourceAsStream("/assets/player/efplayer_l_3.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/assets/player/efplayer_r_1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/assets/player/efplayer_r_2.png"));
            right3 = ImageIO.read(getClass().getResourceAsStream("/assets/player/efplayer_r_3.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {

        if (keyH.upPressed) {
            direction = "up";
            y -= speed;
        } else if (keyH.downPressed) {
            direction = "down";
            y += speed;
        } else if (keyH.leftPressed) {
            direction = "left";
            x -= speed;
        } else if (keyH.rightPressed) {
            direction = "right";
            x += speed;
        }

    }

    public void draw(Graphics2D g2) {
     
        // g2.setColor(Color.white);
        // g2.fillRect(x, y, gp.tileSize, gp.tileSize);

        BufferedImage image = null;
        switch (direction) {
            case "up" -> image = up2;
            case "down" -> image = down2;
            case "left" -> image = left2;
            case "right" -> image = right2;
        }

        g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);

    }
}