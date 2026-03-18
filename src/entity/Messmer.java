package entity;

import battle.BattleMove;
import battle.EnemyData;
import java.awt.Rectangle;
import java.util.List;
import main.GamePanel;
import main.Interactable;

public class Messmer extends Entity implements Interactable {

    // -------------------------------------------------------------------------
    // CONSTANTS — tune di sini
    // -------------------------------------------------------------------------
    private static final int    MAX_HP           = 100;
    private static final int    STRIKES_PER_ROUND = 3;

    // Frame count placeholder — ganti waktu animasi udah ada
    private static final int    FRAME_DEFAULT    = 120;

    public static final EnemyData DATA = new EnemyData(
        "Messmer the Impaler",
        MAX_HP,
        STRIKES_PER_ROUND,
        List.of(
            new BattleMove("Spear Thrust",       3, FRAME_DEFAULT),
            new BattleMove("Double Spear Swing", 5, FRAME_DEFAULT),
            new BattleMove("Fire Serpent",        7, FRAME_DEFAULT),
            new BattleMove("Flaming Assault",     6, FRAME_DEFAULT),
            new BattleMove("Flaming Spear Lunge", 8, FRAME_DEFAULT)
        )
    );

    // -------------------------------------------------------------------------
    // CONSTRUCTOR
    // -------------------------------------------------------------------------
    public Messmer(GamePanel gp) {

        super(gp);

        direction = "down";
        speed     = 1;

        hitbox          = new Rectangle();
        hitbox.x        = 14 * gp.scale / 3;
        hitbox.y        = 30 * gp.scale / 3;
        hitbox.width    = 20 * gp.scale / 3;
        hitbox.height   = 16 * gp.scale / 3;
        hitboxDefaultX  = hitbox.x;
        hitboxDefaultY  = hitbox.y;

        getImage();

    }

    public void getImage() {
        // TODO: ganti path ke sprite Messmer waktu udah ada
        up1    = setup("/npc/messmer/icon_messmer_the_impaler");
        up2    = setup("/npc/messmer/icon_messmer_the_impaler");
        up3    = setup("/npc/messmer/icon_messmer_the_impaler");
        up4    = setup("/npc/messmer/icon_messmer_the_impaler");
        down1  = setup("/npc/messmer/icon_messmer_the_impaler");
        down2  = setup("/npc/messmer/icon_messmer_the_impaler");
        down3  = setup("/npc/messmer/icon_messmer_the_impaler");
        down4  = setup("/npc/messmer/icon_messmer_the_impaler");
        left1  = setup("/npc/messmer/icon_messmer_the_impaler");
        left2  = setup("/npc/messmer/icon_messmer_the_impaler");
        left3  = setup("/npc/messmer/icon_messmer_the_impaler");
        left4  = setup("/npc/messmer/icon_messmer_the_impaler");
        right1 = setup("/npc/messmer/icon_messmer_the_impaler");
        right2 = setup("/npc/messmer/icon_messmer_the_impaler");
        right3 = setup("/npc/messmer/icon_messmer_the_impaler");
        right4 = setup("/npc/messmer/icon_messmer_the_impaler");
    }

    @Override public float  getInteractRadius() { return gp.tileSize * 1.5f; }
    @Override public int    getWorldX()         { return worldX; }
    @Override public int    getWorldY()         { return worldY; }
    @Override public String getPromptText()     { return "Talk"; }

    @Override
    public void interact() {
        gp.dialogManager.startDialog("icon_messmer_the_impaler");
    }

}