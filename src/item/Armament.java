package item;

public class Armament extends Item {

    public int damage;
    public String armamentType; // "katana", "greatsword", etc.

    public Armament(String name, String description, int levelReq, int damage, String armamentType) {
        super(name, description, levelReq, ItemType.armament);
        this.damage     = damage;
        this.armamentType = armamentType;
    }
}