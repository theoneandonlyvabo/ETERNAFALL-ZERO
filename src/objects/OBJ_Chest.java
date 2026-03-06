package objects;

import java.io.IOException;
import javax.imageio.ImageIO;

public class OBJ_Chest extends ObjectHandler {

    public OBJ_Chest() {

        name = "Chest";
        
        try {

            image = ImageIO.read(getClass().getResourceAsStream("/objects/chest_wood_01.png"));
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
