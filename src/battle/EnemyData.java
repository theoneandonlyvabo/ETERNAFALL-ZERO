package battle;

import java.util.List;

public class EnemyData {

    public final String          name;
    public final int             maxHP;
    public final int             strikesPerRound;
    public final List<BattleMove> movePool;

    public EnemyData(String name, int maxHP, int strikesPerRound, List<BattleMove> movePool) {
        this.name            = name;
        this.maxHP           = maxHP;
        this.strikesPerRound = strikesPerRound;
        this.movePool        = movePool;
    }

}