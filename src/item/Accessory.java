package item;

public class Accessory extends Item {

    public float hpMultiplier;
    public float damageMultiplier;

    public Accessory(String name, String description, int levelReq, float hpMultiplier, float damageMultiplier, int value) {
        super(name, description, levelReq, ItemType.ACCESSORY, value);
        this.hpMultiplier     = hpMultiplier;
        this.damageMultiplier = damageMultiplier;
    }

}