package entity;

import java.awt.Rectangle;
import main.GamePanel;
import main.Interactable;

public class NPC_TestDummy extends Entity implements Interactable {

    public NPC_TestDummy(GamePanel gp) {

        super(gp);

        direction = "down";
        speed = 1;

        hitbox = new Rectangle();
        hitbox.x      = 14 * gp.scale / 3;
        hitbox.y      = 30 * gp.scale / 3;
        hitbox.width  = 20 * gp.scale / 3;
        hitbox.height = 16 * gp.scale / 3;

        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;

        getImage();

    }

    public void getImage() {

        up1    = setup("/npc/testdummy/npc_testdummy");
        up2    = setup("/npc/testdummy/npc_testdummy");
        up3    = setup("/npc/testdummy/npc_testdummy");
        up4    = setup("/npc/testdummy/npc_testdummy");
        down1  = setup("/npc/testdummy/npc_testdummy");
        down2  = setup("/npc/testdummy/npc_testdummy");
        down3  = setup("/npc/testdummy/npc_testdummy");
        down4  = setup("/npc/testdummy/npc_testdummy");
        left1  = setup("/npc/testdummy/npc_testdummy");
        left2  = setup("/npc/testdummy/npc_testdummy");
        left3  = setup("/npc/testdummy/npc_testdummy");
        left4  = setup("/npc/testdummy/npc_testdummy");
        right1 = setup("/npc/testdummy/npc_testdummy");
        right2 = setup("/npc/testdummy/npc_testdummy");
        right3 = setup("/npc/testdummy/npc_testdummy");
        right4 = setup("/npc/testdummy/npc_testdummy");

    }

    @Override
    public float getInteractRadius() { return gp.tileSize * 1.5f; }

    @Override
    public int getWorldX() { return worldX; }

    @Override
    public int getWorldY() { return worldY; }

    @Override
    public String getPromptText() { return "Talk"; }

    @Override
    public void interact() {

        System.out.println("interact called");
        gp.dialogManager.startDialog("npc_testdummy");

        // dialog nanti
        
    }

}