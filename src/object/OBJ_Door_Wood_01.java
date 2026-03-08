package object;

import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class OBJ_Door_Wood_01 extends ObjectManager {

    GamePanel gp;

    public OBJ_Door_Wood_01(GamePanel gp) {

        this.gp = gp;

        name = "Chest";
        
        try {

            image = ImageIO.read(getClass().getResourceAsStream("/object/door_wood_01.png"));
            image = gTool.scaleImage(image, gp.tileSize, gp.tileSize);
            
        } catch (IOException e) {
            e.printStackTrace();
        }

        collision = true;
    }

}