package item;

public class Accessory extends Item {

    public String statTarget; // "attack", "defense", "speed", etc.
    public float modifier;    // percentage, e.g. 0.15f = +15%

    public Accessory(String name, String description, int levelReq, String statTarget, float modifier) {
        super(name, description, levelReq, ItemType.ACCESSORY);
        this.statTarget = statTarget;
        this.modifier   = modifier;
    }
}