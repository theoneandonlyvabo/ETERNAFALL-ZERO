package object;

import java.io.IOException;
import javax.imageio.ImageIO;

public class OBJ_Chest extends ObjectManager {

    public OBJ_Chest() {

        name = "Chest";
        
        try {

            image = ImageIO.read(getClass().getResourceAsStream("assets/object/chest_wood_01.png"));
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
