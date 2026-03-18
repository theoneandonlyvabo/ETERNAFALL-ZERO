package main;

import entity.Claude;
import entity.Messmer;
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

        gp.npc[0] = new Claude(gp);
        gp.npc[1] = new Messmer(gp);
    }

}
