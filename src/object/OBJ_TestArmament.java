package object;

import item.Armament;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class OBJ_TestArmament extends ObjectManager {

    private final GamePanel gp;
    public Armament item;

    public OBJ_TestArmament(GamePanel gp) {

        this.gp  = gp;
        this.item = new Armament(
            "Test Armament", "Placeholder armament.",
            Armament.ArmamentType.SWORD, 1, 10, 100
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