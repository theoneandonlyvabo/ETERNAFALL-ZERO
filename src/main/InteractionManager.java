package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class InteractionManager {

    GamePanel gp;
    BufferedImage promptIcon;
    public Interactable currentTarget;

    public InteractionManager(GamePanel gp) {
        this.gp = gp;

        try {
            promptIcon = ImageIO.read(getClass().getResourceAsStream("/gui/HUD_buttonE.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {

        List<Interactable> candidates = new ArrayList<>();

        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] instanceof Interactable target) {
                float dist = getDistance(gp.player.worldX, gp.player.worldY, target.getWorldX(), target.getWorldY());
                if (dist <= target.getInteractRadius()) {
                    candidates.add(target);
                }
            }
        }

        // TODO: tambah NPC loop di sini nanti

        if (candidates.isEmpty()) {
            currentTarget = null;
            return;
        }

        FacingDirection lastDir = gp.keyH.lastDir;
        candidates.sort((a, b) -> {
            int alignA = getAlignmentScore(a, lastDir);
            int alignB = getAlignmentScore(b, lastDir);
            if (alignB != alignA) return alignB - alignA;
            return Float.compare(
                getDistance(gp.player.worldX, gp.player.worldY, a.getWorldX(), a.getWorldY()),
                getDistance(gp.player.worldX, gp.player.worldY, b.getWorldX(), b.getWorldY())
            );
        });

        currentTarget = candidates.get(0);

        if (gp.keyH.interactPressed) {
            currentTarget.interact();
            gp.keyH.interactPressed = false;
        }
    }

    private int getAlignmentScore(Interactable target, FacingDirection dir) {
        int dx = target.getWorldX() - gp.player.worldX;
        int dy = target.getWorldY() - gp.player.worldY;

        return switch (dir) {
            case UP    -> -dy + Math.abs(dx) * -1;
            case DOWN  ->  dy + Math.abs(dx) * -1;
            case LEFT  -> -dx + Math.abs(dy) * -1;
            case RIGHT ->  dx + Math.abs(dy) * -1;
        };
    }

    private float getDistance(int x1, int y1, int x2, int y2) {
        int dx = x2 - x1;
        int dy = y2 - y1;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    public void draw(Graphics2D g2) {
        if (currentTarget == null || promptIcon == null) return;
        if (gp.gameState == GamePanel.menuState) return;

        int screenX = currentTarget.getWorldX() - gp.player.worldX + gp.player.screenX;
        int screenY = currentTarget.getWorldY() - gp.player.worldY + gp.player.screenY;

        int iconW = 48, iconH = 48;
        int promptX = screenX + (gp.tileSize / 2) - (iconW / 2);
        int promptY = screenY - iconH + 8;

        g2.drawImage(promptIcon, promptX, promptY, iconW, iconH, null);
    }
}