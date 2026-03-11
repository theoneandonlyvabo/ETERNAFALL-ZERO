package item;

public class Armament extends Item {

    public int damage;
    public String weaponType; // "katana", "greatsword", etc.

    public Armament(String name, String description, int levelReq, int damage, String weaponType) {
        super(name, description, levelReq, ItemType.armament);
        this.damage     = damage;
        this.weaponType = weaponType;
    }
}