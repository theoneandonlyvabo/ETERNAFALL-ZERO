package object;

import main.GamePanel;

public class ObjectSetter {
    
    GamePanel gp;

    public ObjectSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {

        gp.obj[0] = new OBJ_TestDummy(gp);
        gp.obj[0].worldX = 7 * gp.tileSize;
        gp.obj[0].worldY = 8 * gp.tileSize;

    }
}
