package item;

public abstract class Item {

    public String   name;
    public String   description;
    public int      levelReq;
    public ItemType type;
    public int      value;

    public enum ItemType {
        ARMOR, ARMAMENT, RELIC, ACCESSORY, CONSUMABLE, KEY_ITEM
    }

    public Item(String name, String description, int levelReq, ItemType type, int value) {
        this.name        = name;
        this.description = description;
        this.levelReq    = levelReq;
        this.type        = type;
        this.value       = value;
    }

    public boolean meetsLevelReq(int playerLevel) {
        return playerLevel >= levelReq;
    }

}