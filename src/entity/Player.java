package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyH;
    
    public final int screenX;
    public final int screenY;

    public Player(GamePanel gp, KeyHandler keyH) {
        
        this.gp = gp;
        this.keyH = keyH;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        hitbox = new Rectangle();
        hitbox.x = 14;
        hitbox.y = 30;
        hitbox.width = 20;
        hitbox.height = 16;

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        worldX = gp.tileSize * 3;
        worldY = gp.tileSize * 9;
        speed = 3;
        direction = "right";
    }

    public void getPlayerImage() {

        try {

            up1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/player/efplayer_u_1.png"));
            up2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/player/efplayer_u_2.png"));
            up3 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/player/efplayer_u_3.png"));
            up4 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/player/efplayer_u_4.png"));
            down1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/player/efplayer_d_1.png"));
            down2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/player/efplayer_d_2.png"));
            down3 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/player/efplayer_d_3.png"));
            down4 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/player/efplayer_d_4.png"));
            left1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/player/efplayer_l_1.png"));
            left2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/player/efplayer_l_2.png"));
            left3 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/player/efplayer_l_3.png"));
            left4 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/player/efplayer_l_4.png"));
            right1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/player/efplayer_r_1.png"));
            right2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/player/efplayer_r_2.png"));
            right3 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/player/efplayer_r_3.png"));
            right4 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/player/efplayer_r_4.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {

        if (keyH.upPressed == true || 
            keyH.downPressed == true || 
            keyH.leftPressed == true || 
            keyH.rightPressed == true) {

            if (keyH.upPressed) {
                direction = "up"; 
            } else if (keyH.downPressed) {
                direction = "down";
            } else if (keyH.leftPressed) {
                direction = "left";
            } else if (keyH.rightPressed) {
                direction = "right";
            }

            // Check Tile Collision

            collisionMade = false;
            gp.cChecker.checkTile(this);

            // If Collision is False, Player Can Move

            if (collisionMade == false) {

                switch(direction) {

                    case "up" : worldY -= speed; break;
                    case "down" : worldY += speed; break ;
                    case "left" : worldX -= speed; break;
                    case "right" : worldX += speed; break;

                }

            }

            spriteCounter++;

            if (spriteCounter > 7) {

                if (spriteNum == 1) {
                    spriteNum = 2;
                } else if (spriteNum == 2) {
                    spriteNum = 3;
                } else if (spriteNum == 3) {
                spriteNum = 4;
                } else if (spriteNum == 4) {
                spriteNum = 1;
                }
            spriteCounter = 0;
            }

        } else {
            spriteNum = 4;
        }

    }

    public void draw(Graphics2D g2) {

        BufferedImage image = null;
        switch (direction) {

            case "up":
                if (spriteNum == 1) {
                    image = up1;
                } else if (spriteNum == 2) {
                    image = up2;
                } else if (spriteNum == 3) {
                    image = up3;
                } else if (spriteNum == 4) {
                    image = up4;
                }
                break;

            case "down":
                if (spriteNum == 1) {
                    image = down1;
                } else if (spriteNum == 2) {
                    image = down2;
                } else if (spriteNum == 3) {
                    image = down3;
                } else if (spriteNum == 4) {
                    image = down4;
                }
                break;

            case "left":
                if (spriteNum == 1) {
                    image = left1;
                } else if (spriteNum == 2) {
                    image = left2;
                } else if (spriteNum == 3) {
                    image = left3;
                } else if (spriteNum == 4) {
                    image = left4;
                }
                break;

            case "right":
                if (spriteNum == 1) {
                    image = right1;
                } else if (spriteNum == 2) {    
                    image = right2;
                } else if (spriteNum == 3) {
                    image = right3;
                } else if (spriteNum == 4) {
                    image = right4;
                }
                break;

        }

        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);

    }
}