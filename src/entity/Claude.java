package entity;

import java.awt.Rectangle;
import main.GamePanel;
import main.Interactable;

public class Claude extends Entity implements Interactable {

    private static final int SPAWN_COL = 13;
    private static final int SPAWN_ROW = 6;

    public Claude(GamePanel gp) {

        super(gp);

        direction  = "down";
        speed      = 1;
        worldX     = gp.tileSize * SPAWN_COL;
        worldY     = gp.tileSize * SPAWN_ROW;

        hitbox          = new Rectangle();
        hitbox.x        = 14 * gp.scale / 3;
        hitbox.y        = 30 * gp.scale / 3;
        hitbox.width    = 20 * gp.scale / 3;
        hitbox.height   = 16 * gp.scale / 3;
        hitboxDefaultX  = hitbox.x;
        hitboxDefaultY  = hitbox.y;

        getImage();

    }

    public void getImage() {

        up1    = setup("/npc/npc_testdummy");
        up2    = setup("/npc/npc_testdummy");
        up3    = setup("/npc/npc_testdummy");
        up4    = setup("/npc/npc_testdummy");
        down1  = setup("/npc/npc_testdummy");
        down2  = setup("/npc/npc_testdummy");
        down3  = setup("/npc/npc_testdummy");
        down4  = setup("/npc/npc_testdummy");
        left1  = setup("/npc/npc_testdummy");
        left2  = setup("/npc/npc_testdummy");
        left3  = setup("/npc/npc_testdummy");
        left4  = setup("/npc/npc_testdummy");
        right1 = setup("/npc/npc_testdummy");
        right2 = setup("/npc/npc_testdummy");
        right3 = setup("/npc/npc_testdummy");
        right4 = setup("/npc/npc_testdummy");

    }

    @Override public float  getInteractRadius() { return gp.tileSize * 1.5f; }
    @Override public int    getWorldX()         { return worldX; }
    @Override public int    getWorldY()         { return worldY; }
    @Override public String getPromptText()     { return "Talk"; }
    @Override public String getIconPath()       { return "/npc/claude/icon_claude.png"; }

    @Override
    public void interact() {
        gp.dialogManager.startDialog("claude");
    }

}