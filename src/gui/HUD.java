package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import main.GamePanel;

public class HUD {

    // =========================================================
    // EDITABLE CONSTANTS
    // =========================================================

    // -- Shards --
    static final float  SHARDS_FONT_SIZE     = 42f;
    static final String SHARDS_COLOR         = "#3b6d62";
    static final int    SHARDS_X             = 1358;
    static final int    SHARDS_Y             = 855;

    // -- HP --
    static final float  HP_FONT_SIZE         = 42f;
    static final String HP_COLOR             = "#562923";
    static final int    HP_X                 = 498;
    static final int    HP_Y                 = 830;

    // -- HP Bar --
    static final int    HP_BAR_X             = 253;
    static final int    HP_BAR_Y             = 835;
    static final int    HP_BAR_W             = 244;
    static final int    HP_BAR_H             = 20;
    static final String HP_BAR_FILL_COLOR    = "#562923";

    // -- Level --
    static final float  LEVEL_FONT_SIZE      = 42f;
    static final String LEVEL_COLOR          = "#e3ddd1";
    static final int    LEVEL_X              = 88;
    static final int    LEVEL_Y              = 855;

    // -- Armament --
    static final float  ARMAMENT_FONT_SIZE   = 42f;
    static final String ARMAMENT_COLOR       = "#e3ddd1";
    static final int    ARMAMENT_X           = 585;
    static final int    ARMAMENT_Y           = 830;

    // -- Relic --
    static final float  RELIC_FONT_SIZE      = 42f;
    static final String RELIC_COLOR          = "#e3ddd1";
    static final int    RELIC_X              = 585;
    static final int    RELIC_Y              = 855;

    // -- Interact Prompt --
    static final float  PROMPT_FONT_SIZE     = 42f;
    static final float  PROMPT_TRACKING      = 0f;
    static final int    PROMPT_ICON_W        = 48;
    static final int    PROMPT_ICON_H        = 48;
    static final int    PROMPT_ICON_TEXT_GAP = 2;
    static final int    PROMPT_PADDING_X     = 30;
    static final int    PROMPT_PADDING_Y     = 30;
    static final int    PROMPT_ICON_OFFSET_Y = -40;

    // =========================================================

    GamePanel gp;
    BufferedImage playerBar;
    BufferedImage buttonE;

    // [FIX] font di-cache sekali di constructor, bukan derive tiap frame
    Font fontShards;
    Font fontHp;
    Font fontLevel;
    Font fontArmament;
    Font fontRelic;
    Font fontPrompt;

    public HUD(GamePanel gp) {
        this.gp = gp;

        try {
            playerBar = ImageIO.read(getClass().getResourceAsStream("/gui/HUD_playerbar.png"));
            buttonE   = ImageIO.read(getClass().getResourceAsStream("/gui/HUD_buttonE.png"));

            InputStream fontIs = getClass().getResourceAsStream("/fonts/determination.ttf");
            Font base = Font.createFont(Font.TRUETYPE_FONT, fontIs);

            fontShards   = base.deriveFont(SHARDS_FONT_SIZE);
            fontHp       = base.deriveFont(HP_FONT_SIZE);
            fontLevel    = base.deriveFont(LEVEL_FONT_SIZE);
            fontArmament = base.deriveFont(ARMAMENT_FONT_SIZE);
            fontRelic    = base.deriveFont(RELIC_FONT_SIZE);
            fontPrompt   = base.deriveFont(PROMPT_FONT_SIZE);

        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        // g2.drawImage(playerBar, 0, 0, null);
        // drawHpBar(g2);
        // drawShards(g2);
        // drawHp(g2);
        // drawLevel(g2);
        // drawArmament(g2);
        // drawRelic(g2);
    }

    // =========================================================
    // HP BAR
    // =========================================================
    private void drawHpBar(Graphics2D g2) {
        int current = gp.player.currentHp;
        int max     = gp.player.getTotalMaxHp();
        int fillW   = (int)((double) current / max * HP_BAR_W);

        g2.setColor(Color.decode(HP_BAR_FILL_COLOR));
        g2.fillRect(HP_BAR_X, HP_BAR_Y, fillW, HP_BAR_H);
    }

    // =========================================================
    // SHARDS
    // =========================================================
    private void drawShards(Graphics2D g2) {
        if (fontShards == null) return;

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        g2.setFont(fontShards);
        g2.setColor(Color.decode(SHARDS_COLOR));
        g2.drawString(String.valueOf(gp.itemManager.witherShards), SHARDS_X, SHARDS_Y);
    }

    // =========================================================
    // HP
    // =========================================================
    private void drawHp(Graphics2D g2) {
        if (fontHp == null) return;

        int current = gp.player.currentHp;
        int max     = gp.player.getTotalMaxHp();

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        g2.setFont(fontHp);
        g2.setColor(Color.decode(HP_COLOR));
        String hpText = current + "/" + max;
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(hpText, HP_X - fm.stringWidth(hpText), HP_Y);
    }

    // =========================================================
    // LEVEL
    // =========================================================
    private void drawLevel(Graphics2D g2) {
        if (fontLevel == null) return;

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        g2.setFont(fontLevel);
        g2.setColor(Color.decode(LEVEL_COLOR));
        g2.drawString(String.valueOf(gp.player.level), LEVEL_X, LEVEL_Y);
    }

    // =========================================================
    // ARMAMENT
    // =========================================================
    private void drawArmament(Graphics2D g2) {
        if (fontArmament == null) return;

        String text = gp.player.mainHand != null ? gp.player.mainHand.name : "NONE";

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        g2.setFont(fontArmament);
        g2.setColor(Color.decode(ARMAMENT_COLOR));
        g2.drawString(text, ARMAMENT_X, ARMAMENT_Y);
    }

    // =========================================================
    // RELIC
    // =========================================================
    private void drawRelic(Graphics2D g2) {
        if (fontRelic == null) return;

        String text = gp.player.offHand != null ? gp.player.offHand.name : "NONE";

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        g2.setFont(fontRelic);
        g2.setColor(Color.decode(RELIC_COLOR));
        g2.drawString(text, RELIC_X, RELIC_Y);
    }

}