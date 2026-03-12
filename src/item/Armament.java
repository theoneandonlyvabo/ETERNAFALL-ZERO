package item;

public class Armament extends Item {

    public ArmamentType armamentType;
    public int damage;

    public enum ArmamentType {
        AXE, DAGGER, KATANA, SPEAR, STAFF, SWORD
    }

    public Armament(String name, String description, ArmamentType armamentType, int levelReq, int damage) {
        super(name, description, levelReq, ItemType.ARMAMENT);
        this.damage     = damage;
        this.armamentType = armamentType;
    }
}