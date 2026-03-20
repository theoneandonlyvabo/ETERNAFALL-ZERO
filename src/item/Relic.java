package item;

public class Relic extends Item {

    public String passiveEffect;

    public Relic(String name, String description, int levelReq, String passiveEffect, int value) {
        super(name, description, levelReq, ItemType.RELIC, value);
        this.passiveEffect = passiveEffect;
    }

}