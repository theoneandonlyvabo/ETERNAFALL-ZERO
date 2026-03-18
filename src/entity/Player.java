package entity;

import item.Accessory;
import item.Armament;
import item.Armor;
import item.Relic;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity {

    KeyHandler keyH;

    public final int screenX;
    public final int screenY;

    // =========================================================
    // EDITABLE CONSTANTS (balance / testing)
    // =========================================================
    private static final int START_LEVEL  = 1;
    private static final int BASE_HP      = 10;
    private static final int HP_PER_LEVEL = 10;
    private static final int BASE_DAMAGE  = 2;
    private static final int DEBUG_HP     = -1;
    // =========================================================

    // -------------------------
    // Stats
    // -------------------------
    public int level;
    public int maxHp;
    public int currentHp;
    public final int baseDamage = BASE_DAMAGE;
    public int witherShards;

    // -------------------------
    // Equipment Slots
    // -------------------------
    public Armor     headpiece;
    public Armor     chestpiece;
    public Armor     legpiece;
    public Armament  mainHand;
    public Relic     offHand;
    public Accessory accessory;

    // -------------------------
    // Character Identity
    // -------------------------
    public String currentPath;

    public Player(GamePanel gp, KeyHandler keyH) {

        super(gp);

        this.keyH = keyH;

        screenX = gp.screenWidth  / 2 - gp.tileSize / 2;
        screenY = gp.screenHeight / 2 - gp.tileSize / 2;

        hitbox          = new Rectangle();
        hitbox.x        = 14 * gp.scale / 3;
        hitbox.y        = 30 * gp.scale / 3;
        hitbox.width    = 20 * gp.scale / 3;
        hitbox.height   = 16 * gp.scale / 3;
        hitboxDefaultX  = hitbox.x;
        hitboxDefaultY  = hitbox.y;

        setDefaultValues();
        getImage();

    }

    public void setDefaultValues() {

        worldX = gp.tileSize * 7;
        worldY = gp.tileSize * 4;
        speed  = gp.scale;
        direction = "down";

        level        = START_LEVEL;
        maxHp        = BASE_HP + (HP_PER_LEVEL * (level - 1));
        currentHp    = (DEBUG_HP >= 0) ? DEBUG_HP : maxHp;
        witherShards = 0;

        headpiece  = null;
        chestpiece = null;
        legpiece   = null;
        mainHand   = null;
        offHand    = null;
        accessory  = null;
        currentPath = null;

    }

    // -------------------------
    // Computed Stats
    // -------------------------
    public int getTotalDamage() {
        int flat    = baseDamage + (mainHand != null ? mainHand.getDamage() : 0);
        float multi = accessory  != null ? accessory.damageMultiplier : 1.0f;
        return (int)(flat * multi);
    }

    public int getTotalMaxHp() {
        int flat = maxHp
            + (headpiece  != null ? headpiece.getHpBonus()  : 0)
            + (chestpiece != null ? chestpiece.getHpBonus() : 0)
            + (legpiece   != null ? legpiece.getHpBonus()   : 0);
        float multi = accessory != null ? accessory.hpMultiplier : 1.0f;
        return (int)(flat * multi);
    }

    // -------------------------
    // Level Up
    // -------------------------
    public void levelUp() {
        level++;
        maxHp    += HP_PER_LEVEL;
        currentHp = getTotalMaxHp();
    }

    public void getImage() {

        up1    = setup("/player/efplayer_u_1");
        up2    = setup("/player/efplayer_u_2");
        up3    = setup("/player/efplayer_u_3");
        up4    = setup("/player/efplayer_u_4");
        down1  = setup("/player/efplayer_d_1");
        down2  = setup("/player/efplayer_d_2");
        down3  = setup("/player/efplayer_d_3");
        down4  = setup("/player/efplayer_d_4");
        left1  = setup("/player/efplayer_l_1");
        left2  = setup("/player/efplayer_l_2");
        left3  = setup("/player/efplayer_l_3");
        left4  = setup("/player/efplayer_l_4");
        right1 = setup("/player/efplayer_r_1");
        right2 = setup("/player/efplayer_r_2");
        right3 = setup("/player/efplayer_r_3");
        right4 = setup("/player/efplayer_r_4");

    }

    public void update() {

        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {

            if      (keyH.upPressed)    direction = "up";
            else if (keyH.downPressed)  direction = "down";
            else if (keyH.leftPressed)  direction = "left";
            else if (keyH.rightPressed) direction = "right";

            collisionMade = false;
            gp.cChecker.checkTile(this);

            int objIndex = gp.cChecker.checkObject(this, true);
            if (objIndex != 999 && gp.obj[objIndex] != null) {
                // TODO: handle object interaction
            }

            gp.cChecker.checkEntity(this, gp.npc);

            if (!collisionMade) {
                switch (direction) {
                    case "up"    -> worldY -= speed;
                    case "down"  -> worldY += speed;
                    case "left"  -> worldX -= speed;
                    case "right" -> worldX += speed;
                }
            }

            spriteCounter++;
            if (spriteCounter > 7) {
                spriteNum = (spriteNum % 4) + 1;
                spriteCounter = 0;
            }

        } else {
            spriteNum = 4;
        }

    }

    public void draw(Graphics2D g2) {

        BufferedImage image = null;

        switch (direction) {
            case "up"    -> image = switch (spriteNum) { case 1 -> up1;    case 2 -> up2;    case 3 -> up3;    default -> up4;    };
            case "down"  -> image = switch (spriteNum) { case 1 -> down1;  case 2 -> down2;  case 3 -> down3;  default -> down4;  };
            case "left"  -> image = switch (spriteNum) { case 1 -> left1;  case 2 -> left2;  case 3 -> left3;  default -> left4;  };
            case "right" -> image = switch (spriteNum) { case 1 -> right1; case 2 -> right2; case 3 -> right3; default -> right4; };
        }

        g2.drawImage(image, screenX, screenY, null);

    }

}