package main;

import entity.Claude;
import entity.Messmer;
import item.Armor;
import object.OBJ_TestAccessory;
import object.OBJ_TestArmament;
import object.OBJ_TestArmor;
import object.OBJ_TestRelic;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {

        int i = 0;

        gp.obj[i] = new OBJ_TestArmament(gp);
        gp.obj[i].worldX = 5 * gp.tileSize;
        gp.obj[i].worldY = 8 * gp.tileSize;
        i++;

        gp.obj[i] = new OBJ_TestArmor(gp, Armor.ArmorType.HEAD);
        gp.obj[i].worldX = 6 * gp.tileSize;
        gp.obj[i].worldY = 8 * gp.tileSize;
        i++;

        gp.obj[i] = new OBJ_TestArmor(gp, Armor.ArmorType.CHEST);
        gp.obj[i].worldX = 7 * gp.tileSize;
        gp.obj[i].worldY = 8 * gp.tileSize;
        i++;

        gp.obj[i] = new OBJ_TestArmor(gp, Armor.ArmorType.LEGS);
        gp.obj[i].worldX = 8 * gp.tileSize;
        gp.obj[i].worldY = 8 * gp.tileSize;
        i++;

        gp.obj[i] = new OBJ_TestRelic(gp);
        gp.obj[i].worldX = 9 * gp.tileSize;
        gp.obj[i].worldY = 8 * gp.tileSize;
        i++;

        gp.obj[i] = new OBJ_TestAccessory(gp);
        gp.obj[i].worldX = 10 * gp.tileSize;
        gp.obj[i].worldY = 8 * gp.tileSize;

    }

    public void setNPC() {

        gp.npc[0] = new Claude(gp);
        gp.npc[1] = new Messmer(gp);

    }

}