package main;

import object.OBJ_Chest;

public class ObjectSetter {
    
    GamePanel gp;

    public ObjectSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {

        gp.obj[0] = new OBJ_Chest();
        gp.obj[0].worldX = 6 * gp.tileSize;
        gp.obj[0].worldY = 6 * gp.tileSize;

        gp.obj[1] = new OBJ_Chest();
        gp.obj[1].worldX = 6 * gp.tileSize;
        gp.obj[1].worldY = 10 * gp.tileSize;

    }
}
