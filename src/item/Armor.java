package item;

public class Armor extends Item {

    public enum ArmorType {
        HEAD, CHEST, LEGS
    }

    public ArmorType armorType;
    public int defense;

    public Armor(String name, String description, ArmorType armorType, int levelReq, int defense) {
        super(name, description, levelReq, ItemType.ARMOR);
        this.armorType = armorType;
        this.defense   = defense;
    }
}