package object;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import main.GamePanel;
import main.GameTool;
import main.Interactable;

public class ObjectManager implements Interactable {

    public BufferedImage image;
    public String name;
    public boolean collision = false;
    public int worldX, worldY;
    public Rectangle hitbox = new Rectangle(0, 0, 48, 48);
    public int hitboxDefaultX = 0;
    public int hitboxDefaultY = 0;
    public float interactRadius = 0f;
    GameTool gTool = new GameTool();
    public String interactPrompt = "";

    public void draw(Graphics2D g2, GamePanel gp) {

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
            worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
            worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
            worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

            g2.drawImage(image, screenX, screenY, null);
        }

    }

    @Override
    public float getInteractRadius() { return interactRadius; }

    @Override
    public int getWorldX() { return worldX; }

    @Override
    public int getWorldY() { return worldY; }

    @Override
    public String getPromptText() { return interactPrompt; }

    @Override
    public void interact() {}

}