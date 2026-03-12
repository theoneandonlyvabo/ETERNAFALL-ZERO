package object;

import item.Item;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class OBJ_TestItem extends ObjectManager {

    GamePanel gp;
    public Item item;

    public OBJ_TestItem(GamePanel gp, Item item) {
        this.gp = gp;
        this.item = item;

        name = item.name;
        interactPrompt = "PICK UP";

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/object/obj_testitem.png"));
            image = gTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }

        collision = false;
        interactRadius = 1.5f * gp.tileSize;
    }

    @Override
    public void interact() {
        gp.itemManager.lastPickedName = item.name;
        if (gp.itemManager.addItem(item)) {
            for (int i = 0; i < gp.obj.length; i++) {
                if (gp.obj[i] == this) {
                    gp.obj[i] = null;
                    break;
                }
            }
        }
    }
}