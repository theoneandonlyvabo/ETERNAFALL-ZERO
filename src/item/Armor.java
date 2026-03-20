package item;

public class Armor extends Item {

    public enum ArmorType {
        HEAD, CHEST, LEGS
    }

    public ArmorType armorType;
    public int       hpBonus;

    public Armor(String name, String description, ArmorType armorType, int levelReq, int hpBonus, int value) {
        super(name, description, levelReq, ItemType.ARMOR, value);
        this.armorType = armorType;
        this.hpBonus   = hpBonus;
    }

    public int getHpBonus() {
        return hpBonus;
    }

}