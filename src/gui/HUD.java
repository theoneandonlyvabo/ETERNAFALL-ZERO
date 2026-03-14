package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import javax.imageio.ImageIO;
import main.GamePanel;
import object.ObjectManager;

public class HUD {

    // =========================================================
    // EDITABLE CONSTANTS
    // =========================================================

    // -- Shards --
    static final float  SHARDS_FONT_SIZE    = 42f;
    static final String SHARDS_COLOR        = "#3b6d62";
    static final int    SHARDS_X            = 1358;
    static final int    SHARDS_Y            = 855;

    // -- HP --
    static final float  HP_FONT_SIZE        = 42f;
    static final String HP_COLOR            = "#e3ddd1";
    static final int    HP_X                = 498;
    static final int    HP_Y                = 830;

    // -- HP Bar --
    static final int    HP_BAR_X            = 253;
    static final int    HP_BAR_Y            = 835;
    static final int    HP_BAR_W            = 243;
    static final int    HP_BAR_H            = 20;
    static final String HP_BAR_FILL_COLOR   = "#562923";

    // -- Level --
    static final float  LEVEL_FONT_SIZE     = 42f;
    static final String LEVEL_COLOR         = "#e3ddd1";
    static final int    LEVEL_X             = 89;
    static final int    LEVEL_Y             = 855;

    // -- Armament --
    static final float  ARMAMENT_FONT_SIZE  = 42f;
    static final String ARMAMENT_COLOR      = "#e3ddd1";
    static final int    ARMAMENT_X          = 585;    // center, adjust as needed
    static final int    ARMAMENT_Y          = 830;

    // -- Relic --
    static final float  RELIC_FONT_SIZE     = 42f;
    static final String RELIC_COLOR         = "#e3ddd1";
    static final int    RELIC_X             = 585;    // center, adjust as needed
    static final int    RELIC_Y             = 855;

    // -- Interact Prompt --
    static final float  PROMPT_FONT_SIZE    = 32f;
    static final float  PROMPT_TRACKING     = 0f;
    static final int    PROMPT_ICON_W       = 48;
    static final int    PROMPT_ICON_H       = 48;
    static final int    PROMPT_ICON_TEXT_GAP = 2;
    static final int    PROMPT_PADDING_X    = 30;
    static final int    PROMPT_PADDING_Y    = 30;
    static final int    PROMPT_ICON_OFFSET_Y = -40;

    // =========================================================

    GamePanel gp;
    BufferedImage playerBar;
    BufferedImage buttonE;
    Font pixelFont;

    public HUD(GamePanel gp) {
        this.gp = gp;

        try {
            playerBar = ImageIO.read(getClass().getResourceAsStream("/gui/HUD_playerbar.png"));
            buttonE   = ImageIO.read(getClass().getResourceAsStream("/gui/HUD_buttonE.png"));

            InputStream fontIs = getClass().getResourceAsStream("/fonts/upheaval.ttf");
            pixelFont = Font.createFont(Font.TRUETYPE_FONT, fontIs).deriveFont(PROMPT_FONT_SIZE);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        g2.drawImage(playerBar, 0, 0, null);
        drawHpBar(g2);
        drawShards(g2);
        drawHp(g2);
        drawLevel(g2);
        drawArmament(g2);
        drawRelic(g2);
    }

    // =========================================================
    // HP BAR
    // =========================================================
    private void drawHpBar(Graphics2D g2) {
        int current = gp.player.currentHp;
        int max     = gp.player.getTotalMaxHp();
        int fillW   = (int)((double) current / max * HP_BAR_W);

        // fill
        g2.setColor(Color.decode(HP_BAR_FILL_COLOR));
        g2.fillRect(HP_BAR_X, HP_BAR_Y, fillW, HP_BAR_H);
    }

    // =========================================================
    // SHARDS
    // =========================================================
    private void drawShards(Graphics2D g2) {
        if (pixelFont == null) return;

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        g2.setFont(pixelFont.deriveFont(SHARDS_FONT_SIZE));
        g2.setColor(Color.decode(SHARDS_COLOR));
        g2.drawString(String.valueOf(gp.itemManager.witherShards), SHARDS_X, SHARDS_Y);
    }

    // =========================================================
    // HP
    // =========================================================
    private void drawHp(Graphics2D g2) {
        if (pixelFont == null) return;

        int current = gp.player.currentHp;
        int max     = gp.player.getTotalMaxHp();

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        g2.setFont(pixelFont.deriveFont(HP_FONT_SIZE));
        g2.setColor(Color.decode(HP_COLOR));
        String hpText = current + "/" + max;
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(hpText, HP_X - fm.stringWidth(hpText), HP_Y);
    }

    // =========================================================
    // LEVEL
    // =========================================================
    private void drawLevel(Graphics2D g2) {
        if (pixelFont == null) return;

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        g2.setFont(pixelFont.deriveFont(LEVEL_FONT_SIZE));
        g2.setColor(Color.decode(LEVEL_COLOR));
        g2.drawString(String.valueOf(gp.player.level), LEVEL_X, LEVEL_Y);
    }

    // =========================================================
    // ARMAMENT
    // =========================================================
    private void drawArmament(Graphics2D g2) {
        if (pixelFont == null) return;

        String text = gp.player.mainHand != null ? gp.player.mainHand.name : "NONE";

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        g2.setFont(pixelFont.deriveFont(ARMAMENT_FONT_SIZE));
        g2.setColor(Color.decode(ARMAMENT_COLOR));
        g2.drawString(text, ARMAMENT_X, ARMAMENT_Y);
    }

    // =========================================================
    // RELIC
    // =========================================================
    private void drawRelic(Graphics2D g2) {
        if (pixelFont == null) return;

        String text = gp.player.offHand != null ? gp.player.offHand.name : "NONE";

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        g2.setFont(pixelFont.deriveFont(RELIC_FONT_SIZE));
        g2.setColor(Color.decode(RELIC_COLOR));
        g2.drawString(text, RELIC_X, RELIC_Y);
    }

    // =========================================================
    // INTERACT PROMPT
    // =========================================================
    public void drawInteractPrompt(Graphics2D g2) {
        if (gp.interactionM.currentTarget == null) return;
        if (!(gp.interactionM.currentTarget instanceof ObjectManager obj)) return;
        if (obj.interactPrompt == null || obj.interactPrompt.isEmpty()) return;
        if (pixelFont == null || buttonE == null) return;

        String label = obj.interactPrompt;
        Font trackedFont = pixelFont.deriveFont(Map.of(TextAttribute.TRACKING, PROMPT_TRACKING));

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        g2.setFont(trackedFont);

        FontRenderContext frc   = g2.getFontRenderContext();
        TextLayout layout       = new TextLayout(label, trackedFont, frc);
        int textWidth           = (int) layout.getAdvance();

        FontMetrics fm  = g2.getFontMetrics(trackedFont);
        int textHeight  = fm.getAscent() - fm.getDescent();

        int x     = gp.screenWidth - PROMPT_PADDING_X - PROMPT_ICON_W - PROMPT_ICON_TEXT_GAP - textWidth;
        x         = Math.max(PROMPT_PADDING_X, x);
        int y     = gp.screenHeight - PROMPT_PADDING_Y;
        int iconY = y - PROMPT_ICON_H + PROMPT_ICON_OFFSET_Y;
        int textY = iconY + (PROMPT_ICON_H / 2) + (textHeight / 2);

        g2.drawImage(buttonE, x, iconY, PROMPT_ICON_W, PROMPT_ICON_H, null);
        g2.setColor(Color.white);
        g2.drawString(label, x + PROMPT_ICON_W + PROMPT_ICON_TEXT_GAP, textY);
    }
}