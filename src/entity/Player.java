package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.GameTool;
import main.KeyHandler;

public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyH;
    
    public final int screenX;
    public final int screenY;

    public Player(GamePanel gp, KeyHandler keyH) {
        
        this.gp = gp;
        this.keyH = keyH;

        screenX = gp.screenWidth / 2 - gp.tileSize / 2;
        screenY = gp.screenHeight / 2 - gp.tileSize / 2;

        // Player Hitbox Settings

        hitbox = new Rectangle();

        hitbox.x = 14 * gp.scale / 3;
        hitbox.y = 30 * gp.scale / 3;
        hitbox.width = 20 * gp.scale / 3;
        hitbox.height = 16 * gp.scale / 3;

        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {

        worldX = gp.tileSize * 7;
        worldY = gp.tileSize * 4;
        speed = gp.scale;
        direction = "down";

    }

    public void getPlayerImage() {

        up1 = setup("efplayer_u_1");
        up2 = setup("efplayer_u_2");
        up3 = setup("efplayer_u_3");
        up4 = setup("efplayer_u_4");
        down1 = setup("efplayer_d_1");
        down2 = setup("efplayer_d_2");
        down3 = setup("efplayer_d_3");
        down4 = setup("efplayer_d_4");
        left1 = setup("efplayer_l_1");
        left2 = setup("efplayer_l_2");
        left3 = setup("efplayer_l_3");
        left4 = setup("efplayer_l_4");
        right1 = setup("efplayer_r_1");
        right2 = setup("efplayer_r_2");
        right3 = setup("efplayer_r_3");
        right4 = setup("efplayer_r_4");

    }

    public BufferedImage setup(String imageName) {

        GameTool gTool = new GameTool();
        BufferedImage image = null;

        try {

            image = ImageIO.read(getClass().getResourceAsStream("/player/" + imageName + ".png")); 
            image = gTool.scaleImage(image, gp.tileSize, gp.tileSize);
            
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;

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

            // Check Object Collision

            int objIndex = gp.cChecker.checkObject(this, true);
            
            interact(objIndex);

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

    public void interact(int i) {

        if (i != 999) {

            String testDummy = gp.obj[i].name;

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

        g2.drawImage(image, screenX, screenY, null);

        // g2.setColor(Color.red);
        // g2.drawRect(screenX + hitbox.x, screenY + hitbox.y, hitbox.width, hitbox.height);

    }
}