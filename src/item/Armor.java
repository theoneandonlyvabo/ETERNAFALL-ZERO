package item;

public class Armor extends Item {

    public int defense;

    public Armor(String name, String description, int levelReq, ItemType type, int defense) {
        super(name, description, levelReq, type);
        this.defense = defense;
    }
}