package entity;

import java.awt.Graphics2D; // TAMBAHAN: Perlu import ini untuk parameter draw
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Entity {

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
        // Biarkan kosong. Ini akan di-override secara otomatis oleh Player / NPC.
    }
}