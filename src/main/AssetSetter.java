package main;

import object._FlaskRed;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {

        // Object Positioning

        // Format :

        // gp.obj[0] = new ObjectName
        // (worldX * gp.tileSize, worldY * gp.tileSize);
        // gp.obj[0].gp = gp;

        gp.obj[0] = new _FlaskRed();
        gp.obj[0].worldX = 6 * gp.tileSize;
        gp.obj[0].worldY = 6 * gp.tileSize;
        gp.obj[0].gp = gp;
    }
    
}
