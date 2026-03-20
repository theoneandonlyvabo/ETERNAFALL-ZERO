package object;

import item.Relic;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class OBJ_TestRelic extends ObjectManager {

    private final GamePanel gp;
    public Relic item;

    public OBJ_TestRelic(GamePanel gp) {

        this.gp  = gp;
        this.item = new Relic(
            "Test Relic", "Placeholder relic.",
            1, "No effect.", 120
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