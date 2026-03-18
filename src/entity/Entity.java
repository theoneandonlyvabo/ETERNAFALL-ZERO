package entity;

import java.awt.Graphics2D;
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
    public int spriteNum     = 1;

    public Rectangle hitbox;
    public int hitboxDefaultX, hitboxDefaultY;
    public boolean collisionMade;

    public Entity(GamePanel gp) {
        this.gp = gp;
    }

    public void draw(Graphics2D g2) {
        draw(g2, gp);
    }

    public void draw(Graphics2D g2, GamePanel gp) {

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
            worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
            worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
            worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

            BufferedImage image = switch (direction) {
                case "up"    -> switch (spriteNum) { case 1 -> up1;    case 2 -> up2;    case 3 -> up3;    default -> up4;    };
                case "down"  -> switch (spriteNum) { case 1 -> down1;  case 2 -> down2;  case 3 -> down3;  default -> down4;  };
                case "left"  -> switch (spriteNum) { case 1 -> left1;  case 2 -> left2;  case 3 -> left3;  default -> left4;  };
                case "right" -> switch (spriteNum) { case 1 -> right1; case 2 -> right2; case 3 -> right3; default -> right4; };
                default      -> down1;
            };

            if (image != null) g2.drawImage(image, screenX, screenY, null);
        }

    }

    public BufferedImage setup(String imagePath) {

        GameTool gTool = new GameTool();
        BufferedImage image = null;

        try {
            var stream = getClass().getResourceAsStream(imagePath + ".png");
            if (stream == null) {
                System.err.println("[Entity] Sprite not found: " + imagePath + ".png");
                return null;
            }
            image = ImageIO.read(stream);
            image = gTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;

    }

}