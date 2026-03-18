package battle;

import item.Armament;

public enum BattleAction {

    PUNCH("Punch", 1, 0, null),
    SLASH("Slash", 2, 2, Armament.ArmamentType.KATANA);

    public final String                label;
    public final int                   apCost;
    public final int                   armamentDamage;
    public final Armament.ArmamentType requiredArmament; // null = always available

    BattleAction(String label, int apCost, int armamentDamage, Armament.ArmamentType requiredArmament) {
        this.label            = label;
        this.apCost           = apCost;
        this.armamentDamage   = armamentDamage;
        this.requiredArmament = requiredArmament;
    }

    public int calcDamage(int baseDamage, float accessoryMultiplier) {
        return (int) ((baseDamage + armamentDamage) * accessoryMultiplier);
    }

    public boolean isUnlocked(Armament.ArmamentType equipped) {
        return requiredArmament == null || requiredArmament == equipped;
    }

    public boolean canAfford(int currentAP) {
        return currentAP >= apCost;
    }

}