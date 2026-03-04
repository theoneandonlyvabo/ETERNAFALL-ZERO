package object;

import javax.imageio.ImageIO;

public class _FlaskRed extends ObjectManager {

    public _FlaskRed() {

        name = "Lesser Flask of Healing";
        
        try {
            image = ImageIO.read(getClass().getResourceAsStream
            ("/object/_flask_red.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
