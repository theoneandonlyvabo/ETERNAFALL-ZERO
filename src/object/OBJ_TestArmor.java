package object;

import item.Armor;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class OBJ_TestArmor extends ObjectManager {

    private final GamePanel gp;
    public Armor item;

    public OBJ_TestArmor(GamePanel gp, Armor.ArmorType armorType) {

        this.gp  = gp;

        String label = switch (armorType) {
            case HEAD  -> "Test Headpiece";
            case CHEST -> "Test Chestpiece";
            case LEGS  -> "Test Legpiece";
        };

        this.item = new Armor(label, "Placeholder armor.", armorType, 1, 15, 80);

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