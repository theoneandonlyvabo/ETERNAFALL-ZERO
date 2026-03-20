package item;

public class Consumable extends Item {

    public int    quantity;
    public String effect;

    public Consumable(String name, String description, String effect, int value) {
        super(name, description, 0, ItemType.CONSUMABLE, value);
        this.effect   = effect;
        this.quantity = 1;
    }

}