package battle;

import item.Armament;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import main.GamePanel;

public class BattleManager {

    // -------------------------------------------------------------------------
    // CONSTANTS
    // -------------------------------------------------------------------------
    public static final int MAX_AP       = 6;
    public static final int DEFLECT_COST = 1;
    public static final int ITEM_COST    = 1;
    public static final int FLEE_COST    = 0;

    // -------------------------------------------------------------------------
    // STATE
    // -------------------------------------------------------------------------
    public enum Phase { OFFENSE, DEFENSE, ENEMY_DEAD, PLAYER_DEAD }

    private final GamePanel gp;
    private final Random    rng = new Random();

    public EnemyData enemy;
    public int       enemyHP;
    public int       currentAP;
    public Phase     phase;
    public int       strikesRemaining;
    public String    battleLog  = "";
    public String    feedbackMsg = "";

    private List<BattleMove> roundMoves = new ArrayList<>();
    private int              moveIndex  = 0;

    public BattleManager(GamePanel gp) {
        this.gp = gp;
    }

    // -------------------------------------------------------------------------
    // START
    // -------------------------------------------------------------------------
    public void startBattle(EnemyData enemyData) {
        this.enemy       = enemyData;
        this.enemyHP     = enemyData.maxHP;
        this.currentAP   = MAX_AP;
        this.phase       = Phase.OFFENSE;
        this.battleLog   = "";
        this.feedbackMsg = "";
        gp.gameState     = GamePanel.battleState;
    }

    // -------------------------------------------------------------------------
    // OFFENSE
    // -------------------------------------------------------------------------
    public boolean tryAttack(BattleAction action) {
        if (phase != Phase.OFFENSE) return false;

        Armament.ArmamentType equipped = gp.player.mainHand != null
                ? gp.player.mainHand.armamentType : null;

        if (!action.isUnlocked(equipped)) {
            feedbackMsg = action.label + " requires " + action.requiredArmament;
            return false;
        }
        if (!action.canAfford(currentAP)) {
            feedbackMsg = "AP not enough";
            return false;
        }

        currentAP -= action.apCost;
        feedbackMsg = "";

        float multiplier = gp.player.accessory != null
                ? gp.player.accessory.damageMultiplier : 1.0f;
        int dmg = action.calcDamage(gp.player.baseDamage, multiplier);
        enemyHP = Math.max(0, enemyHP - dmg);
        battleLog = "You used " + action.label + " — " + dmg + " damage.";

        if (enemyHP <= 0) phase = Phase.ENEMY_DEAD;

        return true;
    }

    public void endOffensePhase() {
        if (phase != Phase.OFFENSE) return;
        phase = Phase.DEFENSE;
        prepareRoundMoves();
    }

    // -------------------------------------------------------------------------
    // DEFENSE
    // -------------------------------------------------------------------------
    private void prepareRoundMoves() {
        roundMoves.clear();
        List<BattleMove> pool = new ArrayList<>(enemy.movePool);
        for (int i = 0; i < enemy.strikesPerRound; i++) {
            roundMoves.add(pool.get(rng.nextInt(pool.size())));
        }
        strikesRemaining = enemy.strikesPerRound;
        moveIndex        = 0;
    }

    public BattleMove peekNextMove() {
        if (moveIndex >= roundMoves.size()) return null;
        return roundMoves.get(moveIndex);
    }

    public boolean tryDeflect() {
        if (phase != Phase.DEFENSE) return false;
        if (currentAP < DEFLECT_COST) {
            feedbackMsg = "AP not enough";
            return false;
        }
        currentAP -= DEFLECT_COST;
        feedbackMsg = "";
        return true;
    }

    /** Dipanggil setelah parry bar selesai. outcome: "MISS","EARLY","PERFECT","LATE" */
    public void resolveDeflect(String outcome, BattleMove move) {
        int dmgTaken = switch (outcome) {
            case "PERFECT" -> 0;
            case "EARLY"   -> move.damage / 2;
            default        -> move.damage;
        };
        applyDamageToPlayer(dmgTaken);
        battleLog = enemy.name + " used " + move.name +
                    (dmgTaken == 0 ? " — blocked." : " — " + dmgTaken + " damage.");
        advanceStrike();
    }

    public void skipStrike() {
        if (phase != Phase.DEFENSE) return;
        BattleMove move = peekNextMove();
        if (move == null) return;
        applyDamageToPlayer(move.damage);
        battleLog = enemy.name + " used " + move.name + " — " + move.damage + " damage.";
        advanceStrike();
    }

    public boolean tryUseItem() {
        if (phase != Phase.DEFENSE) return false;
        if (currentAP < ITEM_COST) {
            feedbackMsg = "AP not enough";
            return false;
        }
        currentAP -= ITEM_COST;
        feedbackMsg = "";
        // TODO: buka item menu
        return true;
    }

    private void advanceStrike() {
        moveIndex++;
        strikesRemaining--;
        if (strikesRemaining <= 0) endDefensePhase();
    }

    private void endDefensePhase() {
        currentAP = MAX_AP;
        phase     = Phase.OFFENSE;
        battleLog = "— New Round —";
    }

    // -------------------------------------------------------------------------
    // DAMAGE
    // -------------------------------------------------------------------------
    private void applyDamageToPlayer(int dmg) {
        gp.player.currentHp = Math.max(0, gp.player.currentHp - dmg);
        if (gp.player.currentHp <= 0) phase = Phase.PLAYER_DEAD;
    }

}