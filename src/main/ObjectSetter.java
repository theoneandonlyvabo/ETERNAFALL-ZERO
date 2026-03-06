package main;

import object.OBJ_Chest_Wood_02;
import object.OBJ_Door_Wood_01;


public class ObjectSetter {
    
    GamePanel gp;

    public ObjectSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {

        gp.obj[0] = new OBJ_Door_Wood_01();
        gp.obj[0].worldX = 6 * gp.tileSize;
        gp.obj[0].worldY = 5 * gp.tileSize;

        gp.obj[1] = new OBJ_Chest_Wood_02();
        gp.obj[1].worldX = 20 * gp.tileSize;
        gp.obj[1].worldY = 6 * gp.tileSize;

    }
}
