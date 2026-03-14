package item;

import java.util.Arrays;
import java.util.Comparator;

public class ItemManager {

    private static final int playerSlots = 30;
    private static final int worldSlots  = 60;

    public Item[] playerItems = new Item[playerSlots];
    public int playerCount    = 0;

    public Item[] worldItems = new Item[worldSlots];
    public int worldCount    = 0;

    public int witherShards = 0;

    private static final float consumableCooldown = 3f;
    private float cooldownTimer = 0f;

    public Item lastPickedItem = null;
    public String lastPickedName = "";
    private float lastPickedTimer = 0f;
    private static final float lastPickedDuration = 3f;

    // -------------------------
    // ADD ITEM
    // -------------------------
    public boolean addItem(Item item) {
        if (item instanceof Consumable c) {
            return addConsumable(c);
        }

        if (isPlayerOriented(item)) {
            if (playerCount >= playerSlots) {
                System.out.println("Player inventory full: " + item.name);
                return false;
            }
            playerItems[playerCount++] = item;
            sortArray(playerItems, playerCount);
        } else {
            if (worldCount >= worldSlots) {
                System.out.println("World inventory full: " + item.name);
                return false;
            }
            worldItems[worldCount++] = item;
            sortArray(worldItems, worldCount);
        }

        lastPickedItem  = item;
        lastPickedName  = item.name;
        lastPickedTimer = lastPickedDuration;
        return true;
    }

    private boolean addConsumable(Consumable incoming) {
        for (int i = 0; i < worldCount; i++) {
            if (worldItems[i] instanceof Consumable existing
                    && existing.name.equals(incoming.name)) {
                existing.quantity++;
                lastPickedItem  = existing;
                lastPickedName  = existing.name;
                lastPickedTimer = lastPickedDuration;
                return true;
            }
        }
        if (worldCount >= worldSlots) {
            System.out.println("World inventory full: " + incoming.name);
            return false;
        }
        worldItems[worldCount++] = incoming;
        sortArray(worldItems, worldCount);
        lastPickedItem  = incoming;
        lastPickedName  = incoming.name;
        lastPickedTimer = lastPickedDuration;
        return true;
    }

    // -------------------------
    // REMOVE ITEM
    // -------------------------
    public boolean removeItem(Item item) {
        if (item instanceof Consumable c && c.quantity > 1) {
            c.quantity--;
            return true;
        }

        if (isPlayerOriented(item)) {
            return removeFromArray(playerItems, playerCount--, item);
        } else {
            return removeFromArray(worldItems, worldCount--, item);
        }
    }

    private boolean removeFromArray(Item[] arr, int count, Item target) {
        for (int i = 0; i < count; i++) {
            if (arr[i] == target) {
                arr[i] = arr[count - 1];
                arr[count - 1] = null;
                sortArray(arr, count - 1);
                return true;
            }
        }
        return false;
    }

    // -------------------------
    // CURRENCY
    // -------------------------
    public void addShards(int amount) {
        witherShards += amount;
        System.out.println("+" + amount + " Wither Shards. Total: " + witherShards);
    }

    public boolean spendShards(int amount) {
        if (witherShards < amount) {
            System.out.println("Not enough Wither Shards.");
            return false;
        }
        witherShards -= amount;
        System.out.println("-" + amount + " Wither Shards. Total: " + witherShards);
        return true;
    }

    // -------------------------
    // CONSUMABLE COOLDOWN
    // -------------------------
    public boolean useConsumable(Consumable c, int playerLevel) {
        if (!c.meetsLevelReq(playerLevel)) {
            System.out.println("Level too low: " + c.name);
            return false;
        }
        if (cooldownTimer > 0) {
            System.out.println("Cooldown: " + cooldownTimer + "s remaining.");
            return false;
        }
        cooldownTimer = consumableCooldown;
        removeItem(c);
        System.out.println("Used: " + c.name + " | Effect: " + c.effect);
        return true;
    }

    // -------------------------
    // UPDATE
    // -------------------------
    public void update(float deltaTime) {
        if (cooldownTimer > 0) cooldownTimer -= deltaTime;
        if (lastPickedTimer > 0) lastPickedTimer -= deltaTime;
        else {
            lastPickedItem = null;
            lastPickedName = "";
        }
    }

    // -------------------------
    // UTILITY
    // -------------------------
    private boolean isPlayerOriented(Item item) {
        return item instanceof Armor
            || item instanceof Armament
            || item instanceof Relic
            || item instanceof Accessory;
    }

    private void sortArray(Item[] arr, int count) {
        Arrays.sort(arr, 0, count, Comparator.comparing(i -> i.name));
    }

    // -------------------------
    // DEBUG
    // -------------------------
    public void printInventory() {
        System.out.println("\n=== Player Inventory (" + playerCount + "/" + playerSlots + ") ===");
        for (int i = 0; i < playerCount; i++) {
            System.out.println("  " + playerItems[i].name + " [" + playerItems[i].type + "]");
        }

        System.out.println("\n=== World Inventory (" + worldCount + "/" + worldSlots + ") ===");
        for (int i = 0; i < worldCount; i++) {
            Item it = worldItems[i];
            String qty = it instanceof Consumable c ? " x" + c.quantity : "";
            System.out.println("  " + it.name + qty + " [" + it.type + "]");
        }

        System.out.println("\n=== Wither Shards: " + witherShards + " ===");
        System.out.println("=== Last Picked: " + (lastPickedName.isEmpty() ? "none" : lastPickedName) + " ===\n");
    }

    // -------------------------
    // CONSOLE TEST
    // -------------------------
    public static void main(String[] args) {

        ItemManager inv = new ItemManager();

        inv.printInventory();
    }
}