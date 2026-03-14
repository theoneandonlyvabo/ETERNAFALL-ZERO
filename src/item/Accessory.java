package item;

public class Accessory extends Item {

    public float hpMultiplier;      // 1.05f = +5%, 1.0f = no boost
    public float damageMultiplier;  // same

    public Accessory(String name, String description, int levelReq, float hpMultiplier, float damageMultiplier) {
        super(name, description, levelReq, ItemType.ACCESSORY);
        this.hpMultiplier     = hpMultiplier;
        this.damageMultiplier = damageMultiplier;
    }
}