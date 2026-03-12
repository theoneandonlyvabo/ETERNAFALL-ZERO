package item;

public abstract class Item {

    public String name;
    public String description;
    public int levelReq;
    public ItemType type;

    public enum ItemType {
        ARMOR, ARMAMENT, RELIC, ACCESSORY, CONSUMABLE, KEY_ITEM
    }

    public Item(String name, String description, int levelReq, ItemType type) {
        this.name        = name;
        this.description = description;
        this.levelReq    = levelReq;
        this.type        = type;
    }

    public boolean meetsLevelReq(int playerLevel) {
        return playerLevel >= levelReq;
    }
}