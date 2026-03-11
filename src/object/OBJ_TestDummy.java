package object;

import item.Armament;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class OBJ_TestDummy extends ObjectManager {

    GamePanel gp;
    Armament item = new Armament(

        "Test Dummy", 
        "Haha item go brrr", 
        0, 
        67, 
        "longSword"
        
    );

    public OBJ_TestDummy(GamePanel gp) {

        this.gp = gp;

        name = item.name;
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
        gp.itemManager.lastPickedName = name;
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