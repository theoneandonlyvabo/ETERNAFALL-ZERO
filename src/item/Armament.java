package item;

public class Armament extends Item {

    public enum ArmamentType {
        AXE, DAGGER, KATANA, SPEAR, STAFF, SWORD
    }

    public ArmamentType armamentType;
    public int          damage;

    public Armament(String name, String description, ArmamentType armamentType, int levelReq, int damage, int value) {
        super(name, description, levelReq, ItemType.ARMAMENT, value);
        this.armamentType = armamentType;
        this.damage       = damage;
    }

    public int getDamage() {
        return damage;
    }

}