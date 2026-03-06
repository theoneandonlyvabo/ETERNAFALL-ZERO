package object;

import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

public class OBJ_Door_Wood_02 extends ObjectManager {

    public OBJ_Door_Wood_02() {

        name = "Chest";
        
        try {

            image = ImageIO.read(new FileInputStream("res/object/door_wood_02.png"));
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}