package object;

import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class OBJ_TestDummy extends ObjectManager {

    GamePanel gp;

    public OBJ_TestDummy(GamePanel gp) {

        this.gp = gp;

        name = "TestDummy";
        interactPrompt = "PICK UP";

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/object/obj_testdummy.png"));
            image = gTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }

        collision = false;
        interactRadius = 1.5f * gp.tileSize;
    }

    @Override
    public void interact() {
        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] == this) {
                gp.obj[i] = null;
                break;
            }
        }
    }
}