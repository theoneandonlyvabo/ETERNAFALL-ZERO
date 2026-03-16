package entity;

import java.awt.Graphics2D; // TAMBAHAN: Perlu import ini untuk parameter draw
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.GameTool;

public class Entity {

    GamePanel gp;

    public int worldX, worldY;
    public int speed;
    
    public BufferedImage
        up1, up2, up3, up4,
        down1, down2, down3, down4,
        left1, left2, left3, left4,
        right1, right2, right3, right4;
    public String direction;

    public int spriteCounter = 0;
    public int spriteNum = 1;

    public Rectangle hitbox;
    public int hitboxDefaultX, hitboxDefaultY;
    public boolean collisionMade;

    // TAMBAHAN: Method kosong agar bisa dipanggil lewat perulangan List di GamePanel
    public void draw(Graphics2D g2) {
        draw(g2, gp);
    }

    public Entity(GamePanel gp) {
        this.gp = gp;
    }

    public void draw(Graphics2D g2, GamePanel gp) {
    
        BufferedImage image = null;
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
            worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
            worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
            worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

            switch (direction) {
                case "up":
                    if      (spriteNum == 1) image = up1;
                    else if (spriteNum == 2) image = up2;
                    else if (spriteNum == 3) image = up3;
                    else if (spriteNum == 4) image = up4;
                    break;
                case "down":
                    if      (spriteNum == 1) image = down1;
                    else if (spriteNum == 2) image = down2;
                    else if (spriteNum == 3) image = down3;
                    else if (spriteNum == 4) image = down4;
                    break;
                case "left":
                    if      (spriteNum == 1) image = left1;
                    else if (spriteNum == 2) image = left2;
                    else if (spriteNum == 3) image = left3;
                    else if (spriteNum == 4) image = left4;
                    break;
                case "right":
                    if      (spriteNum == 1) image = right1;
                    else if (spriteNum == 2) image = right2;
                    else if (spriteNum == 3) image = right3;
                    else if (spriteNum == 4) image = right4;
                    break;
            }

            g2.drawImage(image, screenX, screenY, null);
        }
        
    }

    public BufferedImage setup(String imagePath) {

        GameTool gTool = new GameTool();
        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
            image = gTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;

    }
}