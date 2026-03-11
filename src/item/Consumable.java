package item;

public class Consumable extends Item {

    public int quantity;
    public String effect;

    public Consumable(String name, String description, String effect) {
        super(name, description, 0, ItemType.consumable);
        this.effect   = effect;
        this.quantity = 1;
    }
}