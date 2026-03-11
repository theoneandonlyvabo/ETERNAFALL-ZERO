package item;

public abstract class Item {

    public String name;
    public String description;
    public int levelReq;
    public ItemType type;

    public enum ItemType {
        // Armor
        head, chest, legs,
        // Player gear
        armament, relic, accessory,
        // World
        consumable, keyItem
    }

    public Item(String name, String description, int levelReq, ItemType type) {
        this.name        = name;
        this.description = description;
        this.levelReq    = levelReq;
        this.type        = type;
    }

    // Cek apakah player cukup level untuk equip/use item ini
    public boolean meetsLevelReq(int playerLevel) {
        return playerLevel >= levelReq;
    }
}