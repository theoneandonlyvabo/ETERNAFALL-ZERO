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

    // CONFIG
    private static final int ICON_W          = 30;
    private static final int ICON_H          = 30;
    private static final int PROMPT_X_OFFSET = 0; // ubah sesuai kebutuhan
    private static final int PROMPT_Y_OFFSET = 0;
    private static final int NPC_Y_EXTRA     = 10;

    public InteractionManager(GamePanel gp) {
        this.gp = gp;

        try {
            promptIcon = ImageIO.read(getClass().getResourceAsStream("/gui/HUD_buttonE.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {

        // Kalau dialog aktif, buang interactPressed dan skip
        if (gp.dialogManager.isActive) {
            gp.keyHandler.interactPressed = false;
            return;
        }

        List<Interactable> candidates = new ArrayList<>();

        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] instanceof Interactable target) {
                float dist = getDistance(gp.player.worldX, gp.player.worldY, target.getWorldX(), target.getWorldY());
                if (dist <= target.getInteractRadius()) candidates.add(target);
            }
        }

        for (int i = 0; i < gp.npc.length; i++) {
            if (gp.npc[i] instanceof Interactable target) {
                float dist = getDistance(gp.player.worldX, gp.player.worldY, target.getWorldX(), target.getWorldY());
                if (dist <= target.getInteractRadius()) candidates.add(target);
            }
        }

        if (candidates.isEmpty()) {
            currentTarget = null;
            return;
        }

        FacingDirection lastDir = gp.keyHandler.lastDir;
        candidates.sort((a, b) -> {
            float distA = getDistance(gp.player.worldX, gp.player.worldY, a.getWorldX(), a.getWorldY());
            float distB = getDistance(gp.player.worldX, gp.player.worldY, b.getWorldX(), b.getWorldY());
            if (Math.abs(distA - distB) > 20f) return Float.compare(distA, distB);
            int alignA = getAlignmentScore(a, lastDir);
            int alignB = getAlignmentScore(b, lastDir);
            return alignB - alignA;
        });

        currentTarget = candidates.get(0);

        if (gp.keyHandler.interactPressed) {
            currentTarget.interact();
            gp.keyHandler.interactPressed = false;
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

    private boolean isNPC(Interactable target) {
        return target instanceof entity.Entity;
    }

    public void draw(Graphics2D g2) {
        if (currentTarget == null || promptIcon == null) return;
        if (gp.gameState == GameState.PAUSED) return;
        if (gp.dialogManager.isActive) return;

        int screenX = currentTarget.getWorldX() - gp.player.worldX + gp.player.screenX;
        int screenY = currentTarget.getWorldY() - gp.player.worldY + gp.player.screenY;

        int extraY  = isNPC(currentTarget) ? NPC_Y_EXTRA : 0;
        int promptX = screenX + (gp.tileSize / 2) - (ICON_W / 2) + PROMPT_X_OFFSET;
        int promptY = screenY - ICON_H + PROMPT_Y_OFFSET - extraY;

        g2.drawImage(promptIcon, promptX, promptY, ICON_W, ICON_H, null);
    }
}