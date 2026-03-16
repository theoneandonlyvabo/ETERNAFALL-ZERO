package object;

import item.Armament;
import item.Item;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class OBJ_TestItem extends ObjectManager {

    private final GamePanel gp;
    public Item item;

    public OBJ_TestItem(GamePanel gp) {

        this.gp   = gp;
        this.item = new Armament("Test Item", "Armament Test Item", Armament.ArmamentType.SWORD, 0, 999);

        name          = item.name;
        interactPrompt = "Pick Up";
        collision      = false;
        interactRadius = 1.5f * gp.tileSize;

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/object/obj_testitem.png"));
            image = gTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }

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