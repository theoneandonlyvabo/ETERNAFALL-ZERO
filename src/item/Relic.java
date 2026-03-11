package item;

public class Relic extends Item {

    public String passiveEffect;

    public Relic(String name, String description, int levelReq, String passiveEffect) {
        super(name, description, levelReq, ItemType.relic);
        this.passiveEffect = passiveEffect;
    }
}