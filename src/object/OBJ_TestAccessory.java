package object;

import item.Accessory;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class OBJ_TestAccessory extends ObjectManager {

    private final GamePanel gp;
    public Accessory item;

    public OBJ_TestAccessory(GamePanel gp) {

        this.gp  = gp;
        this.item = new Accessory(
            "Test Accessory", "Placeholder accessory.",
            1, 1.05f, 1.10f, 150
        );

        name           = item.name;
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