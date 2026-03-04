package object;

import java.io.IOException;

public class _Key extends MainObject {

    public _Key() {

        name = "Key";

        try {
            image = javax.imageio.ImageIO.read(getClass().getResourceAsStream(""));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
}