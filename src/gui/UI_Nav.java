package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.IOException;
import java.io.InputStream;
import main.GamePanel;

public class UI_Nav {

    // =========================================================
    // CONFIG
    // =========================================================
    static final int    NAV_X        = 40;
    static final int    NAV_Y        = 140;
    static final int    NAV_W        = 260;
    static final int    NAV_H        = 360;
    static final int    BORDER       = 8;

    static final int    SHARDS_X     = 40;
    static final int    SHARDS_Y     = 40;
    static final int    SHARDS_W     = 260;
    static final int    SHARDS_H     = 80;

    static final int    PANEL_W      = 856;
    static final int    PANEL_H      = 784;

    static final float  FONT_SIZE    = 32f;

    static final String COLOR_HOVER  = "#e3ddd1";
    static final String COLOR_IDLE   = "#4f493b";
    static final String COLOR_BG     = "#000000";
    static final String COLOR_BORDER = "#4f493b";
    static final String COLOR_SHARDS = "#5aaa7a";

    public static final String[] NAV_ITEMS = { "Player", "Inventory", "Map", "Quest Log", "Settings", "Cancel" };

    // =========================================================

    GamePanel gp;
    Font font;

    public boolean navOpen    = false;
    public boolean panelOpen  = false;
    public int     hoverIndex  = 0;
    public int     activeIndex = -1;

    int panelX, panelY;

    public UI_Nav(GamePanel gp) {
        this.gp = gp;

        panelX = (gp.screenWidth  - PANEL_W) / 2;
        panelY = (gp.screenHeight - PANEL_H) / 2;

        try {
            InputStream is = getClass().getResourceAsStream("/fonts/determination.ttf");
            Font base = Font.createFont(Font.TRUETYPE_FONT, is);
            font = base.deriveFont(FONT_SIZE);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
    }

    // =========================================================
    // PUBLIC API
    // =========================================================

    public void onTab() {
        if (!navOpen) {
            navOpen     = true;
            panelOpen   = false;
            hoverIndex  = 0;
            activeIndex = -1;
        } else if (panelOpen) {
            panelOpen   = false;
            activeIndex = -1;
        } else {
            closeAll();
        }
    }

    public void onNavUp() {
        if (!navOpen || panelOpen) return;
        if (hoverIndex > 0) {
            hoverIndex--;
            playSfxNav();
        }
    }

    public void onNavDown() {
        if (!navOpen || panelOpen) return;
        if (hoverIndex < NAV_ITEMS.length - 1) {
            hoverIndex++;
            playSfxNav();
        }
    }

    public void onConfirm() {
        if (!navOpen || panelOpen) return;
        // "Cancel" adalah item terakhir — close
        if (hoverIndex == NAV_ITEMS.length - 1) {
            closeAll();
            return;
        }
        openPanel(hoverIndex);
    }

    public void onShortcut(int index) {
        if (panelOpen && activeIndex == index) {
            closeAll();
            return;
        }
        navOpen    = true;
        hoverIndex = index;
        openPanel(index);
    }

    public void closeAll() {
        navOpen     = false;
        panelOpen   = false;
        hoverIndex  = 0;
        activeIndex = -1;
    }

    public boolean isOpen() {
        return navOpen;
    }

    // =========================================================
    // INTERNAL
    // =========================================================

    private void openPanel(int index) {
        activeIndex = index;
        panelOpen   = true;
    }

    private void playSfxNav() {
        gp.sfx.setFile(3);
        gp.sfx.play(3);
    }

    // =========================================================
    // DRAW
    // =========================================================

    public void draw(Graphics2D g2) {
        if (!navOpen) return;

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

        if (panelOpen) drawPanel(g2);
        drawShards(g2);
        drawNav(g2);
    }

    private void drawShards(Graphics2D g2) {
        g2.setColor(Color.decode(COLOR_BORDER));
        g2.fillRect(SHARDS_X, SHARDS_Y, SHARDS_W, SHARDS_H);

        g2.setColor(Color.decode(COLOR_BG));
        g2.fillRect(SHARDS_X + BORDER, SHARDS_Y + BORDER, SHARDS_W - BORDER * 2, SHARDS_H - BORDER * 2);

        if (font == null) return;
        g2.setFont(font);
        FontMetrics fm = g2.getFontMetrics();

        int innerY    = SHARDS_Y + (SHARDS_H + fm.getAscent() - fm.getDescent()) / 2;
        int rightEdge = SHARDS_X + SHARDS_W - BORDER - 16;

        String shardsText = String.valueOf(gp.player.witherShards);
        int textW = fm.stringWidth(shardsText);
        g2.setColor(Color.decode(COLOR_SHARDS));
        g2.drawString(shardsText, rightEdge - textW, innerY);
    }

    private void drawNav(Graphics2D g2) {
        g2.setColor(Color.decode(COLOR_BORDER));
        g2.fillRect(NAV_X, NAV_Y, NAV_W, NAV_H);

        g2.setColor(Color.decode(COLOR_BG));
        g2.fillRect(NAV_X + BORDER, NAV_Y + BORDER, NAV_W - BORDER * 2, NAV_H - BORDER * 2);

        if (font == null) return;
        g2.setFont(font);

        FontMetrics fm  = g2.getFontMetrics();
        int itemH       = fm.getHeight() + 12;
        int totalH      = itemH * NAV_ITEMS.length - 12;
        int innerX      = NAV_X + BORDER + 16;
        int innerY      = NAV_Y + (NAV_H - totalH) / 2 + fm.getAscent();

        for (int i = 0; i < NAV_ITEMS.length; i++) {
            g2.setColor(i == hoverIndex ? Color.decode(COLOR_HOVER) : Color.decode(COLOR_IDLE));
            g2.drawString(NAV_ITEMS[i], innerX, innerY + i * itemH);
        }
    }

    private void drawPanel(Graphics2D g2) {
        g2.setColor(Color.decode(COLOR_BORDER));
        g2.fillRect(panelX, panelY, PANEL_W, PANEL_H);
        g2.setColor(Color.decode(COLOR_BG));
        g2.fillRect(panelX + BORDER, panelY + BORDER, PANEL_W - BORDER * 2, PANEL_H - BORDER * 2);
    }

}