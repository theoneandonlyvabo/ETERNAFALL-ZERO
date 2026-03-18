package battle;

public class BattleMove {

    public final String name;
    public final int    damage;
    public final int    frameCount; // placeholder, tune later

    public BattleMove(String name, int damage, int frameCount) {
        this.name       = name;
        this.damage     = damage;
        this.frameCount = frameCount;
    }

}