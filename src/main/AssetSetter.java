package main;

import entity.NPC_TestDummy;
import object.OBJ_TestItem;

public class AssetSetter {
    
    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {

        gp.obj[0] = new OBJ_TestItem(gp);
        gp.obj[0].worldX = 5 * gp.tileSize;
        gp.obj[0].worldY = 8 * gp.tileSize;

    }

    public void setNPC() {

        gp.npc[0] = new NPC_TestDummy(gp);
        gp.npc[0].worldX = gp.tileSize * 7;
        gp.npc[0].worldY = gp.tileSize * 8;

    }

}
