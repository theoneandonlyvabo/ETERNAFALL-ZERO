package object;

import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

public class OBJ_Chest_Wood_01 extends ObjectManager {

    public OBJ_Chest_Wood_01() {

        name = "Chest";
        
        try {

            image = ImageIO.read(new FileInputStream("res/object/chest_wood_01.png"));
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}