package gui;

import item.Accessory;
import item.Armament;
import item.Armor;
import item.Item;
import item.Relic;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.IOException;
import java.io.InputStream;
import main.GamePanel;

public class UI_Player {

    // =========================================================
    // CONFIG
    // =========================================================
    static final int    PANEL_W      = 856;
    static final int    PANEL_H      = 784;
    static final int    BORDER       = 8;
    static final int    HEADER_H     = 150;
    static final int    FOOTER_H     = 150;
    static final int    SLOT_SIZE    = 112;
    static final int    SLOT_BORDER  = 8;
    static final int    PAD          = 30;
    static final float  FONT_SIZE    = 32f;
    static final float  FONT_SMALL   = 24f;

    // Context panel — sejajar nav
    static final int    CTX_X        = 1236;
    static final int    CTX_Y        = 130;
    static final int    CTX_W        = 260;
    static final int    CTX_H        = 304;

    static final String COLOR_BG         = "#000000";
    static final String COLOR_BORDER     = "#4f493b";
    static final String COLOR_TEXT       = "#696353";   // tipe 1 — nilai dinamis
    static final String COLOR_TEXT_FIXED = "#4f493b";   // tipe 2 — label fixed
    static final String COLOR_TEXT_DIM   = "#4f493b";
    static final String COLOR_HIGHLIGHT  = "#e3ddd1";   // hovered
    static final String COLOR_VALUE_TEAL = "#3b6d62";
    static final String COLOR_DIVIDER    = "#4f493b";

    static final int COLS = 2;
    static final int ROWS = 3;

    static final String[] CONTEXT_ITEMS = { "Examine", "Switch", "Drop", "Cancel" };
    // =========================================================

    GamePanel gp;
    Font font;
    Font fontSmall;

    // State
    public int     col          = 0;
    public int     row          = 0;
    public boolean contextOpen  = false;
    public int     contextIndex = 0;

    int panelX, panelY;

    public UI_Player(GamePanel gp) {
        this.gp = gp;

        // Panel di-offset ke kanan supaya tidak tertutup nav
        panelX = (gp.screenWidth - PANEL_W) / 2;
        panelY = (gp.screenHeight - PANEL_H) / 2;

        try {
            InputStream is = getClass().getResourceAsStream("/fonts/determination.ttf");
            Font base = Font.createFont(Font.TRUETYPE_FONT, is);
            font      = base.deriveFont(FONT_SIZE);
            fontSmall = base.deriveFont(FONT_SMALL);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
    }

    // =========================================================
    // PUBLIC API
    // =========================================================

    public void onUp() {
        if (contextOpen) {
            if (contextIndex > 0) {
                contextIndex--;
                playSfxNav();
            }
            return;
        }
        if (row > 0) {
            row--;
            playSfxNav();
        }
    }

    public void onDown() {
        if (contextOpen) {
            if (contextIndex < CONTEXT_ITEMS.length - 1) {
                contextIndex++;
                playSfxNav();
            }
            return;
        }
        if (row < ROWS - 1) {
            row++;
            playSfxNav();
        }
    }

    public void onLeft() {
        if (contextOpen) return;
        if (col > 0) {
            col--;
            playSfxNav();
        }
    }

    public void onRight() {
        if (contextOpen) return;
        if (col < COLS - 1) {
            col++;
            playSfxNav();
        }
    }

    public void onConfirm() {
        if (contextOpen) {
            handleContextConfirm();
            return;
        }
        contextOpen  = true;
        contextIndex = 0;
    }

    public void onCancel() {
        if (contextOpen) {
            contextOpen = false;
            return;
        }
    }

    public void reset() {
        col          = 0;
        row          = 0;
        contextOpen  = false;
        contextIndex = 0;
    }

    // =========================================================
    // INTERNAL
    // =========================================================

    private void handleContextConfirm() {
        switch (CONTEXT_ITEMS[contextIndex]) {
            case "Cancel" -> contextOpen = false;
            // Examine, Switch, Drop — TODO
        }
    }

    private void playSfxNav() {
        gp.SFX.setFile(3);
        gp.SFX.play(3);
    }

    private Item getSlotItem(int c, int r) {
        if (c == 0) {
            return switch (r) {
                case 0 -> gp.player.headpiece;
                case 1 -> gp.player.chestpiece;
                case 2 -> gp.player.legpiece;
                default -> null;
            };
        } else {
            return switch (r) {
                case 0 -> gp.player.mainHand;
                case 1 -> gp.player.offHand;
                case 2 -> gp.player.accessory;
                default -> null;
            };
        }
    }

    private String getSlotLabel(int c, int r) {
        if (c == 0) {
            return switch (r) {
                case 0 -> "Headpiece";
                case 1 -> "Chestpiece";
                case 2 -> "Legpiece";
                default -> "";
            };
        } else {
            return switch (r) {
                case 0 -> "Armament";
                case 1 -> "Relic";
                case 2 -> "Accessory";
                default -> "";
            };
        }
    }

    private String getFooterSlotLabel(int c, int r, Item item) {
        if (item instanceof Armament arm) return capitalize(arm.armamentType.name());
        if (c == 0) return getSlotLabel(c, r);
        return switch (r) {
            case 0 -> "Armament";
            case 1 -> "Relic";
            case 2 -> "Accessory";
            default -> "";
        };
    }

    // =========================================================
    // DRAW
    // =========================================================

    public void draw(Graphics2D g2) {
        if (font == null) return;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

        int contentY = panelY + BORDER + HEADER_H;
        int gridH    = PANEL_H - BORDER * 2 - HEADER_H - FOOTER_H;

        drawHeader(g2);
        drawGrid(g2, contentY, gridH);
        drawFooter(g2);
        if (contextOpen) drawContext(g2);
    }

    // ── Header ──────────────────────────────────────────────
    private void drawHeader(Graphics2D g2) {
        int x = panelX + BORDER;
        int y = panelY + BORDER;
        int w = PANEL_W - BORDER * 2;

        // Divider bawah header
        g2.setColor(Color.decode(COLOR_DIVIDER));
        g2.fillRect(x, y + HEADER_H - BORDER, w, BORDER);

        g2.setFont(font);
        FontMetrics fm = g2.getFontMetrics();
        int lineH = fm.getHeight() + 8;
        int textY = y + (HEADER_H - BORDER - lineH * 2) / 2 + fm.getAscent();

        int midX  = x + w / 2;

        // Kiri
        int level  = gp.player.level;
        int shards = gp.itemManager.witherShards;
        int needed = levelUpCost(level);
        drawLabelValue(g2, fm, "Level",  level + " (" + shards + "/" + needed + ")", x + PAD, midX, textY);
        drawLabelValueColored(g2, fm, "Shards", formatNumber(shards), x + PAD, midX, textY + lineH, COLOR_TEXT_FIXED, COLOR_VALUE_TEAL);

        // Kanan
        int hp    = gp.player.getTotalMaxHp();
        int power = getDamageMax();
        drawLabelValue(g2, fm, "Health", formatNumber(hp),      midX + PAD, midX + w / 2, textY);
        drawLabelValue(g2, fm, "Power",  String.valueOf(power), midX + PAD, midX + w / 2, textY + lineH);
    }

    // ── Grid ────────────────────────────────────────────────
    private void drawGrid(Graphics2D g2, int startY, int gridH) {
        int innerX  = panelX + BORDER;
        int innerW  = PANEL_W - BORDER * 2;
        int midX    = innerX + innerW / 2;

        // Zona grid: 20px dari atas dan bawah area grid
        int gridTop    = startY + 20;
        int gridBottom = startY + gridH - 20;
        int usableH    = gridBottom - gridTop;
        int rowH       = usableH / ROWS;

        for (int c = 0; c < COLS; c++) {
            for (int r = 0; r < ROWS; r++) {
                // Kolom kiri: 20px dari border kiri
                // Kolom kanan: midX + 20
                int slotX = (c == 0) ? innerX + PAD : midX + 10;
                int slotY = gridTop + r * rowH + (rowH - SLOT_SIZE) / 2;
                boolean hov = (c == col && r == row);
                drawSlot(g2, slotX, slotY, c, r, hov);
            }
        }
    }

    private void drawSlot(Graphics2D g2, int x, int y, int c, int r, boolean hovered) {
        Item item = getSlotItem(c, r);

        // Slot box
        g2.setColor(hovered ? Color.decode(COLOR_HIGHLIGHT) : Color.decode(COLOR_BORDER));
        g2.fillRect(x, y, SLOT_SIZE, SLOT_SIZE);
        g2.setColor(Color.decode(COLOR_BG));
        g2.fillRect(x + SLOT_BORDER, y + SLOT_BORDER,
                    SLOT_SIZE - SLOT_BORDER * 2, SLOT_SIZE - SLOT_BORDER * 2);

        // TODO: draw item sprite

        // Zona teks kanan slot
        int innerW   = PANEL_W - BORDER * 2;
        int midX     = panelX + BORDER + innerW / 2;
        int textX    = x + SLOT_SIZE + PAD / 2;
        int maxRight = (c == 0) ? midX - PAD : panelX + BORDER + innerW - PAD;
        int maxW     = maxRight - textX;
        int centerY  = y + SLOT_SIZE / 2;

        g2.setFont(font);
        FontMetrics fm = g2.getFontMetrics();
        String color = hovered ? COLOR_HIGHLIGHT : COLOR_TEXT;

        if (item == null) {
            g2.setColor(hovered ? Color.decode(COLOR_HIGHLIGHT) : Color.decode(COLOR_TEXT_DIM));
            drawStringFit(g2, fm, "None", textX, centerY + fm.getAscent() / 2, maxW);
        } else {
            String name  = item.name;
            String lvReq = levelReqLabel(item);

            if (fm.stringWidth(name) <= maxW) {
                // 1 baris cukup
                int baseY = centerY - fm.getHeight() / 4;
                g2.setColor(Color.decode(color));
                drawStringFit(g2, fm, name, textX, baseY, maxW);
                g2.setFont(fontSmall);
                FontMetrics fms = g2.getFontMetrics();
                g2.setColor(Color.decode(COLOR_TEXT_DIM));
                drawStringFit(g2, fms, lvReq, textX, baseY + fm.getHeight() + 4, maxW);
                g2.setFont(font);
            } else {
                // Cari split terbaik untuk 2 baris
                String[] words = name.split(" ");
                String line1 = "", line2 = "";
                int bestSplit = -1;

                for (int i = 1; i < words.length; i++) {
                    String l1 = String.join(" ", java.util.Arrays.copyOfRange(words, 0, i));
                    String l2 = String.join(" ", java.util.Arrays.copyOfRange(words, i, words.length));
                    if (fm.stringWidth(l1) <= maxW) {
                        bestSplit = i;
                        line1 = l1;
                        line2 = l2;
                    }
                }

                if (bestSplit == -1) {
                    line1 = name;
                    line2 = "";
                }

                boolean twoLines  = !line2.isEmpty();
                int lh            = fm.getHeight();
                int nameBlockH    = twoLines ? lh * 2 + 2 : lh;
                int totalH        = nameBlockH + 4 + (int) FONT_SMALL;
                int startY        = centerY - totalH / 2 + fm.getAscent();

                g2.setColor(Color.decode(color));
                drawStringFit(g2, fm, line1, textX, startY, maxW);
                if (twoLines) {
                    drawStringFit(g2, fm, line2, textX, startY + lh + 2, maxW);
                }
                g2.setFont(fontSmall);
                FontMetrics fms = g2.getFontMetrics();
                g2.setColor(Color.decode(COLOR_TEXT_DIM));
                int lvY = twoLines ? startY + lh * 2 + 6 : startY + lh + 6;
                drawStringFit(g2, fms, lvReq, textX, lvY, maxW);
                g2.setFont(font);
            }
        }
    }

    /**
     * Draw string, squeeze horizontally via AffineTransform kalau melebihi maxW.
     */
    private void drawStringFit(Graphics2D g2, FontMetrics fm, String text, int x, int y, int maxW) {
        int tw = fm.stringWidth(text);
        if (tw <= maxW || tw == 0) {
            g2.drawString(text, x, y);
        } else {
            double scaleX = (double) maxW / tw;
            java.awt.geom.AffineTransform orig = g2.getTransform();
            g2.translate(x, y);
            g2.scale(scaleX, 1.0);
            g2.drawString(text, 0, 0);
            g2.setTransform(orig);
        }
    }

    // ── Footer ──────────────────────────────────────────────
    private void drawFooter(Graphics2D g2) {
        int x = panelX + BORDER;
        int y = panelY + PANEL_H - BORDER - FOOTER_H;
        int w = PANEL_W - BORDER * 2;

        // Divider atas footer
        g2.setColor(Color.decode(COLOR_DIVIDER));
        g2.fillRect(x, y, w, BORDER);

        Item   item = getSlotItem(col, row);
        String slot = getSlotLabel(col, row);

        g2.setFont(font);
        FontMetrics fm = g2.getFontMetrics();
        int lineH = fm.getHeight() + 8;
        int textY = y + BORDER + (FOOTER_H - lineH * 2) / 2 + fm.getAscent();
        int midX  = x + w / 2;

        if (item == null) {
            drawLabelValueColored(g2, fm, "Level",    "N/A", x + PAD, midX,            textY,         COLOR_TEXT_FIXED, COLOR_TEXT_DIM);
            drawLabelValueColored(g2, fm, "Value",    "N/A", x + PAD, midX,            textY + lineH,  COLOR_TEXT_FIXED, COLOR_TEXT_DIM);
            drawLabelValueColored(g2, fm, "Category", getSlotLabel(col, row), midX + PAD, midX + w / 2, textY,  COLOR_TEXT_FIXED, COLOR_TEXT);
            drawLabelValueColored(g2, fm, "Defense",  "N/A", midX + PAD, midX + w / 2, textY + lineH,  COLOR_TEXT_FIXED, COLOR_TEXT_DIM);
        } else if (item instanceof Armor a) {
            drawLabelValue(g2, fm, "Level",    levelReqLabel(item),       x + PAD, midX,            textY);
            drawLabelValueColored(g2, fm, "Value", formatNumber(item.value), x + PAD, midX,          textY + lineH, COLOR_TEXT, COLOR_VALUE_TEAL);
            drawLabelValue(g2, fm, "Category", slot,                      midX + PAD, midX + w / 2, textY);
            drawLabelValue(g2, fm, "Defense",  String.valueOf(a.hpBonus), midX + PAD, midX + w / 2, textY + lineH);
        } else if (item instanceof Armament arm) {
            String power   = String.valueOf(getDamageMaxFor(arm));
            String weaponT = capitalize(arm.armamentType.name());
            drawLabelValue(g2, fm, "Level",    levelReqLabel(item),       x + PAD, midX,            textY);
            drawLabelValueColored(g2, fm, "Value", formatNumber(item.value), x + PAD, midX,          textY + lineH, COLOR_TEXT, COLOR_VALUE_TEAL);
            drawLabelValue(g2, fm, "Category", weaponT,                   midX + PAD, midX + w / 2, textY);
            drawLabelValue(g2, fm, "Power",    power,                     midX + PAD, midX + w / 2, textY + lineH);
        } else if (item instanceof Relic rel) {
            drawLabelValue(g2, fm, "Level",    levelReqLabel(item),        x + PAD, midX,            textY);
            drawLabelValueColored(g2, fm, "Value", formatNumber(item.value), x + PAD, midX,           textY + lineH, COLOR_TEXT, COLOR_VALUE_TEAL);
            drawLabelValue(g2, fm, "Category", "Relic",                    midX + PAD, midX + w / 2, textY);
            drawLabelValue(g2, fm, "Effect",   rel.passiveEffect,          midX + PAD, midX + w / 2, textY + lineH);
        } else if (item instanceof Accessory acc) {
            drawLabelValue(g2, fm, "Level",     levelReqLabel(item),           x + PAD, midX,            textY);
            drawLabelValueColored(g2, fm, "Value", formatNumber(item.value),   x + PAD, midX,            textY + lineH, COLOR_TEXT, COLOR_VALUE_TEAL);
            drawLabelValue(g2, fm, "HP Bonus",  "x" + acc.hpMultiplier,       midX + PAD, midX + w / 2, textY);
            drawLabelValue(g2, fm, "Dmg Bonus", "x" + acc.damageMultiplier,   midX + PAD, midX + w / 2, textY + lineH);
        }
    }

    private void drawContext(Graphics2D g2) {
        g2.setColor(Color.decode(COLOR_BORDER));
        g2.fillRect(CTX_X, CTX_Y, CTX_W, CTX_H);
        g2.setColor(Color.decode(COLOR_BG));
        g2.fillRect(CTX_X + BORDER, CTX_Y + BORDER, CTX_W - BORDER * 2, CTX_H - BORDER * 2);

        g2.setFont(font);
        FontMetrics fm = g2.getFontMetrics();
        // itemH harus identik sama nav
        int itemH  = fm.getHeight() + 12;
        // Total height nav (5 items) — bukan context items
        int navTotalH = itemH * 5 - 12;
        int innerX    = CTX_X + BORDER + 16;
        // Starting Y sama persis dengan nav items
        int innerY    = CTX_Y + (CTX_H - navTotalH) / 2 + fm.getAscent();

        for (int i = 0; i < CONTEXT_ITEMS.length; i++) {
            g2.setColor(i == contextIndex
                ? Color.decode(COLOR_HIGHLIGHT)
                : Color.decode(COLOR_TEXT));
            g2.drawString(CONTEXT_ITEMS[i], innerX, innerY + i * itemH);
        }
    }

    // =========================================================
    // DRAW HELPERS
    // =========================================================

    /**
     * Draw label (kiri) dan value (right-aligned ke maxX) dengan warna default.
     * Label = tipe 2 (fixed), value = tipe 1 (dinamis).
     */
    private void drawLabelValue(Graphics2D g2, FontMetrics fm,
                                 String label, String value,
                                 int labelX, int maxX, int y) {
        g2.setColor(Color.decode(COLOR_TEXT_FIXED));
        g2.drawString(label, labelX, y);
        g2.setColor(Color.decode(COLOR_TEXT));
        g2.drawString(value, maxX - PAD - fm.stringWidth(value), y);
    }

    /**
     * Sama tapi warna value bisa dikustom — untuk shards (teal) dll.
     */
    private void drawLabelValueColored(Graphics2D g2, FontMetrics fm,
                                        String label, String value,
                                        int labelX, int maxX, int y,
                                        String labelColor, String valueColor) {
        g2.setColor(Color.decode(labelColor));
        g2.drawString(label, labelX, y);
        g2.setColor(Color.decode(valueColor));
        g2.drawString(value, maxX - PAD - fm.stringWidth(value), y);
    }

    // =========================================================
    // UTIL
    // =========================================================

    private String levelReqLabel(Item item) {
        return item.levelReq == 0 ? "[Lv. -]" : "[Lv. " + item.levelReq + "]";
    }

    private String formatNumber(int n) {
        if (n < 1000) return String.valueOf(n);
        return String.format("%,d", n).replace(',', ' ');
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.charAt(0) + s.substring(1).toLowerCase();
    }

    private int levelUpCost(int level) {
        if (level <= 10) return level * 100;
        if (level <= 20) return level * 500;
        return level * 1000;
    }

    private int getDamageMax() {
        int base = gp.player.mainHand != null ? gp.player.mainHand.getDamage() : gp.player.baseDamage;
        float multi = gp.player.accessory != null ? gp.player.accessory.damageMultiplier : 1.0f;
        return (int)(base * multi);
    }

    private int getDamageMaxFor(Armament arm) {
        float multi = gp.player.accessory != null ? gp.player.accessory.damageMultiplier : 1.0f;
        return (int)(arm.getDamage() * multi);
    }

}