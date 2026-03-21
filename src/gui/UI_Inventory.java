package gui;

import item.Accessory;
import item.Armament;
import item.Armor;
import item.Consumable;
import item.Item;
import item.KeyItem;
import item.Relic;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import main.GamePanel;

public class UI_Inventory {

    // =========================================================
    // CONFIG
    // =========================================================
    static final int    PANEL_W      = 856;
    static final int    PANEL_H      = 784;
    static final int    BORDER       = 8;
    static final int    HEADER_H     = 80;
    static final int    FOOTER_H     = 150;
    static final int    PAD          = 30;
    static final int    CELL_W       = 240;
    static final int    CELL_H       = 72;
    static final int    CELL_GAP     = 30;
    static final int    COLS         = 3;
    static final float  FONT_SIZE    = 32f;
    static final float  FONT_SMALL   = 32f;

    static final String COLOR_BG         = "#000000";
    static final String COLOR_BORDER     = "#4f493b";
    static final String COLOR_TEXT       = "#696353";
    static final String COLOR_TEXT_FIXED = "#4f493b";
    static final String COLOR_TEXT_DIM   = "#4f493b";
    static final String COLOR_HIGHLIGHT  = "#e3ddd1";
    static final String COLOR_VALUE_TEAL = "#3b6d62";
    static final String COLOR_DIVIDER    = "#4f493b";

    static final String[] CONTEXT_ITEMS = { "Examine", "Equip", "Drop", "Cancel" };

    enum Tab { ALL, KEY_ITEM, CONSUMABLE, ARMOR, ARMAMENT, RELIC, ACCESSORY }
    static final Tab[]    TAB_ORDER = Tab.values();
    static final String[] TAB_LABEL = {
        "All", "Key Items", "Consumables", "Armor", "Armament", "Relic", "Accessory"
    };
    // =========================================================

    GamePanel gp;
    Font font;
    Font fontSmall;

    // Focus: 0 = tab bar, 1 = grid, 2 = context menu
    public int     focus        = 0;
    public int     tabIndex     = 0;
    public int     tabScrollX   = 0;
    public int     gridCol      = 0;
    public int     gridRow      = 0;
    public int     scrollOffset = 0;
    public boolean contextOpen  = false;
    public int     contextIndex = 0;

    // Computed layout — dihitung di constructor, konsisten di semua method
    int panelX, panelY;
    int innerX, innerW;
    int gridStartY;   // y awal grid (bawah header divider + PAD)
    int gridEndY;     // y akhir grid (atas footer divider)
    int visibleRows;

    // Footer column refs — mirror UI_Player
    int colLeftMax;
    int colRightLabelX;
    int colRightMax;

    public UI_Inventory(GamePanel gp) {
        this.gp = gp;

        panelX = (gp.screenWidth  - PANEL_W) / 2;
        panelY = (gp.screenHeight - PANEL_H) / 2;

        innerX = panelX + BORDER;
        innerW = PANEL_W - BORDER * 2;

        // Grid area: mulai setelah header (termasuk divider 8px), end sebelum footer divider
        gridStartY = panelY + BORDER + HEADER_H;           // tepat di bawah header divider
        gridEndY   = panelY + PANEL_H - BORDER - FOOTER_H; // tepat di atas footer divider

        // Berapa baris yang muat: (gridH - PAD_atas) / (CELL_H + CELL_GAP)
        // Baris pertama mulai di gridStartY + PAD, lalu tiap baris CELL_H + CELL_GAP
        int gridH   = gridEndY - gridStartY;
        visibleRows = (gridH - PAD) / (CELL_H + CELL_GAP);

        // Footer columns — sama persis dengan UI_Player
        int midX       = innerX + innerW / 2;
        colLeftMax      = midX;
        colRightLabelX  = gp.screenWidth / 2 + 10;
        colRightMax     = innerX + innerW;

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
        if (focus != 1 || contextOpen) return;
        if (gridRow > 0) {
            gridRow--;
            clampScroll();
            playSfxNav();
        }
    }

    public void onDown() {
        if (focus != 1 || contextOpen) return;
        List<Item> items = getFilteredItems();
        if (gridRow < rowCount(items) - 1) {
            gridRow++;
            clampScroll();
            playSfxNav();
        }
    }

    public void onLeft() {
        if (focus == 2) {
            if (contextIndex > 0) { contextIndex--; playSfxNav(); }
            return;
        }
        if (focus == 0) {
            if (tabIndex > 0) { tabIndex--; resetGrid(); playSfxNav(); }
            return;
        }
        if (contextOpen) return;
        if (gridCol > 0) { gridCol--; playSfxNav(); }
    }

    public void onRight() {
        if (focus == 2) {
            if (contextIndex < CONTEXT_ITEMS.length - 1) { contextIndex++; playSfxNav(); }
            return;
        }
        if (focus == 0) {
            if (tabIndex < TAB_ORDER.length - 1) { tabIndex++; resetGrid(); playSfxNav(); }
            return;
        }
        if (contextOpen) return;
        List<Item> items = getFilteredItems();
        int maxCol = Math.min(COLS, items.size() - gridRow * COLS) - 1;
        if (gridCol < maxCol) { gridCol++; playSfxNav(); }
    }

    public void onConfirm() {
        if (focus == 2) { handleContextConfirm(); return; }
        if (focus == 0) { focus = 1; playSfxNav(); return; }
        List<Item> items = getFilteredItems();
        if (getSelectedItem(items) != null) {
            contextOpen  = true;
            contextIndex = 0;
            focus        = 2;
            playSfxNav();
        }
    }

    public void onTab() {
        if (focus == 2) return;
        if (focus == 1) { focus = 0; playSfxNav(); }
        // focus == 0: caller handle balik ke UI_Nav
    }

    public boolean isAtTabBar() { return focus == 0; }

    public void reset() {
        focus        = 0;
        tabIndex     = 0;
        tabScrollX   = 0;
        gridCol      = 0;
        gridRow      = 0;
        scrollOffset = 0;
        contextOpen  = false;
        contextIndex = 0;
    }

    // =========================================================
    // INTERNAL
    // =========================================================

    private void handleContextConfirm() {
        switch (CONTEXT_ITEMS[contextIndex]) {
            case "Cancel" -> { contextOpen = false; focus = 1; }
            // Examine, Equip, Drop — TODO
        }
    }

    private void resetGrid() {
        gridCol      = 0;
        gridRow      = 0;
        scrollOffset = 0;
    }

    private void clampScroll() {
        if (gridRow < scrollOffset) scrollOffset = gridRow;
        if (gridRow >= scrollOffset + visibleRows) scrollOffset = gridRow - visibleRows + 1;
    }

    private void updateTabScroll(FontMetrics fm) {
        int x = 0;
        for (int i = 0; i < TAB_ORDER.length; i++) {
            int tw = fm.stringWidth(TAB_LABEL[i]) + PAD * 2;
            if (i == tabIndex) {
                if (x < tabScrollX) tabScrollX = x;
                if (x + tw > tabScrollX + innerW) tabScrollX = x + tw - innerW;
                break;
            }
            x += tw;
        }
    }

    private List<Item> getFilteredItems() {
        Tab    tab   = TAB_ORDER[tabIndex];
        int    count = gp.itemManager.worldCount;
        Item[] arr   = gp.itemManager.worldItems;
        List<Item> out = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Item item = arr[i];
            if (item == null) continue;
            if (tab == Tab.ALL)                                      { out.add(item); continue; }
            if (tab == Tab.ARMOR      && item instanceof Armor)      out.add(item);
            if (tab == Tab.ARMAMENT   && item instanceof Armament)   out.add(item);
            if (tab == Tab.RELIC      && item instanceof Relic)      out.add(item);
            if (tab == Tab.ACCESSORY  && item instanceof Accessory)  out.add(item);
            if (tab == Tab.CONSUMABLE && item instanceof Consumable) out.add(item);
            if (tab == Tab.KEY_ITEM   && item instanceof KeyItem)    out.add(item);
        }
        return out;
    }

    private Item getSelectedItem(List<Item> items) {
        int idx = gridRow * COLS + gridCol;
        if (idx < 0 || idx >= items.size()) return null;
        return items.get(idx);
    }

    private int rowCount(List<Item> items) {
        return (int) Math.ceil((double) items.size() / COLS);
    }

    private void playSfxNav() {
        gp.SFX.setFile(3);
        gp.SFX.play(3);
    }

    // =========================================================
    // DRAW
    // =========================================================

    public void draw(Graphics2D g2) {
        if (font == null) return;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

        drawTabBar(g2);
        drawGrid(g2);
        drawFooter(g2);
        if (contextOpen) drawContext(g2);
    }

    // ── Tab bar ─────────────────────────────────────────────
    private void drawTabBar(Graphics2D g2) {
        g2.setFont(font);
        FontMetrics fm = g2.getFontMetrics();
        updateTabScroll(fm);

        int tabAreaY = panelY + BORDER;
        int tabAreaH = HEADER_H - BORDER; // ruang teks, tidak termasuk divider bawah

        // Clip supaya tab yang keluar panel tidak ke-render
        Rectangle oldClip = g2.getClipBounds();
        g2.setClip(innerX, tabAreaY, innerW, tabAreaH);

        int x     = innerX - tabScrollX;
        int textY = tabAreaY + (tabAreaH + fm.getAscent() - fm.getDescent()) / 2 - fm.getDescent();

        for (int i = 0; i < TAB_ORDER.length; i++) {
            String  label  = TAB_LABEL[i];
            int     tw     = fm.stringWidth(label) + PAD * 2;
            boolean active = (i == tabIndex);

            if      (active && focus == 0) g2.setColor(Color.decode(COLOR_HIGHLIGHT));
            else if (active)               g2.setColor(Color.decode(COLOR_TEXT));
            else                           g2.setColor(Color.decode(COLOR_TEXT_DIM));

            g2.drawString(label, x + PAD, textY);
            x += tw;
        }

        g2.setClip(oldClip);

        // Divider bawah header
        g2.setColor(Color.decode(COLOR_DIVIDER));
        g2.fillRect(innerX, panelY + BORDER + HEADER_H - BORDER, innerW, BORDER);
    }

    // ── Grid ────────────────────────────────────────────────
    private void drawGrid(Graphics2D g2) {
        List<Item> items = getFilteredItems();

        g2.setFont(font);
        FontMetrics fm = g2.getFontMetrics();

        int clipH  = gridEndY - gridStartY;
        Rectangle oldClip = g2.getClipBounds();
        g2.setClip(innerX, gridStartY, innerW, clipH);

        int firstCellY = gridStartY + PAD;

        if (items.isEmpty()) {
            g2.setFont(fontSmall);
            g2.setColor(Color.decode(COLOR_TEXT_DIM));
            g2.drawString("Empty", innerX + PAD, firstCellY + g2.getFontMetrics().getAscent());
            g2.setFont(font);
            g2.setClip(oldClip);
            return;
        }

        int rows = rowCount(items);
        for (int r = scrollOffset; r < Math.min(rows, scrollOffset + visibleRows + 1); r++) {
            for (int c = 0; c < COLS; c++) {
                int idx = r * COLS + c;
                if (idx >= items.size()) break;

                Item    item  = items.get(idx);
                boolean hov   = (focus >= 1 && c == gridCol && r == gridRow);
                // Cell x: innerX + PAD, lalu tiap kolom CELL_W + CELL_GAP
                int     cellX = innerX + PAD + c * (CELL_W + CELL_GAP);
                int     cellY = firstCellY + (r - scrollOffset) * (CELL_H + CELL_GAP);

                drawCell(g2, fm, item, hov, cellX, cellY);
            }
        }

        g2.setClip(oldClip);
    }

    private void drawCell(Graphics2D g2, FontMetrics fm, Item item, boolean hovered, int x, int y) {
        String color = hovered ? COLOR_HIGHLIGHT : COLOR_TEXT;
        int    maxW  = CELL_W;
        String name  = item.name;

        if (fm.stringWidth(name) <= maxW) {
            g2.setColor(Color.decode(color));
            // Vertically center single-line text dalam CELL_H
            int textY = y + (CELL_H + fm.getAscent() - fm.getDescent()) / 2 - fm.getDescent();
            g2.drawString(name, x, textY);
        } else {
            // Word wrap ke dua baris
            String[] words = name.split(" ");
            String   line1 = name, line2 = "";
            int      best  = -1;
            for (int i = 1; i < words.length; i++) {
                String l1 = String.join(" ", java.util.Arrays.copyOfRange(words, 0, i));
                if (fm.stringWidth(l1) <= maxW) { best = i; line1 = l1; }
            }
            if (best != -1) {
                line2 = String.join(" ", java.util.Arrays.copyOfRange(words, best, words.length));
            }

            boolean twoLines = !line2.isEmpty();
            int lh    = fm.getHeight();
            int blockH = twoLines ? lh * 2 + 2 : lh;
            int startY = y + (CELL_H - blockH) / 2 + fm.getAscent();

            g2.setColor(Color.decode(color));
            g2.drawString(line1, x, startY);
            if (twoLines) {
                g2.setFont(fontSmall);
                g2.drawString(line2, x, startY + lh + 2);
                g2.setFont(font);
            }
        }
    }

    // ── Footer ──────────────────────────────────────────────
    private void drawFooter(Graphics2D g2) {
        int x = innerX;
        int y = gridEndY; // divider duduk tepat di sini

        // Divider atas footer
        g2.setColor(Color.decode(COLOR_DIVIDER));
        g2.fillRect(x, y, innerW, BORDER);

        List<Item> items = getFilteredItems();
        Item item = (focus >= 1) ? getSelectedItem(items) : null;

        g2.setFont(font);
        FontMetrics fm   = g2.getFontMetrics();
        int lineH        = fm.getHeight() + 8;
        int footerTextY  = y + BORDER + (FOOTER_H - BORDER - lineH * 2) / 2 + fm.getAscent();

        if (item == null) {
            drawLabelValueColored(g2, fm, "Level",    "N/A", x + PAD,        colLeftMax,    footerTextY,         COLOR_TEXT_FIXED, COLOR_TEXT_DIM);
            drawLabelValueColored(g2, fm, "Value",    "N/A", x + PAD,        colLeftMax,    footerTextY + lineH, COLOR_TEXT_FIXED, COLOR_TEXT_DIM);
            drawLabelValueColored(g2, fm, "Category", "N/A", colRightLabelX, colRightMax,   footerTextY,         COLOR_TEXT_FIXED, COLOR_TEXT_DIM);
            drawLabelValueColored(g2, fm, "Stat",     "N/A", colRightLabelX, colRightMax,   footerTextY + lineH, COLOR_TEXT_FIXED, COLOR_TEXT_DIM);
        } else if (item instanceof Armor a) {
            drawLabelValue(g2, fm, "Level",    String.valueOf(item.levelReq),           x + PAD,        colLeftMax,  footerTextY);
            drawLabelValueColored(g2, fm, "Value", formatNumber(item.value),            x + PAD,        colLeftMax,  footerTextY + lineH, COLOR_TEXT, COLOR_VALUE_TEAL);
            drawLabelValue(g2, fm, "Category", "Armor",                                 colRightLabelX, colRightMax, footerTextY);
            drawLabelValue(g2, fm, "Defense",  String.valueOf(a.hpBonus),               colRightLabelX, colRightMax, footerTextY + lineH);
        } else if (item instanceof Armament arm) {
            drawLabelValue(g2, fm, "Level",    String.valueOf(item.levelReq),           x + PAD,        colLeftMax,  footerTextY);
            drawLabelValueColored(g2, fm, "Value", formatNumber(item.value),            x + PAD,        colLeftMax,  footerTextY + lineH, COLOR_TEXT, COLOR_VALUE_TEAL);
            drawLabelValue(g2, fm, "Category", capitalize(arm.armamentType.name()),     colRightLabelX, colRightMax, footerTextY);
            drawLabelValue(g2, fm, "Power",    String.valueOf(arm.getDamage()),         colRightLabelX, colRightMax, footerTextY + lineH);
        } else if (item instanceof Relic rel) {
            drawLabelValue(g2, fm, "Level",    String.valueOf(item.levelReq),           x + PAD,        colLeftMax,  footerTextY);
            drawLabelValueColored(g2, fm, "Value", formatNumber(item.value),            x + PAD,        colLeftMax,  footerTextY + lineH, COLOR_TEXT, COLOR_VALUE_TEAL);
            drawLabelValue(g2, fm, "Category", "Relic",                                 colRightLabelX, colRightMax, footerTextY);
            drawLabelValue(g2, fm, "Effect",   rel.passiveEffect,                       colRightLabelX, colRightMax, footerTextY + lineH);
        } else if (item instanceof Accessory acc) {
            drawLabelValue(g2, fm, "Level",    String.valueOf(item.levelReq),           x + PAD,        colLeftMax,  footerTextY);
            drawLabelValueColored(g2, fm, "Value", formatNumber(item.value),            x + PAD,        colLeftMax,  footerTextY + lineH, COLOR_TEXT, COLOR_VALUE_TEAL);
            drawLabelValue(g2, fm, "Category", "Accessory",                             colRightLabelX, colRightMax, footerTextY);
            drawLabelValue(g2, fm, "Buff",     acc.description,                         colRightLabelX, colRightMax, footerTextY + lineH);
        } else if (item instanceof Consumable con) {
            drawLabelValue(g2, fm, "Level",   String.valueOf(item.levelReq),            x + PAD,        colLeftMax,  footerTextY);
            drawLabelValueColored(g2, fm, "Value", formatNumber(item.value),            x + PAD,        colLeftMax,  footerTextY + lineH, COLOR_TEXT, COLOR_VALUE_TEAL);
            drawLabelValue(g2, fm, "Effects", con.effect,                               colRightLabelX, colRightMax, footerTextY);
            drawLabelValue(g2, fm, "Amount",  String.valueOf(con.quantity),             colRightLabelX, colRightMax, footerTextY + lineH);
        } else if (item instanceof KeyItem) {
            drawLabelValue(g2, fm, "Level",   String.valueOf(item.levelReq),            x + PAD,        colLeftMax,  footerTextY);
            drawLabelValueColored(g2, fm, "Value", formatNumber(item.value),            x + PAD,        colLeftMax,  footerTextY + lineH, COLOR_TEXT, COLOR_VALUE_TEAL);
            drawLabelValue(g2, fm, "Effects", item.description,                         colRightLabelX, colRightMax, footerTextY);
            drawLabelValue(g2, fm, "Amount",  "1",                                      colRightLabelX, colRightMax, footerTextY + lineH);
        }
    }

    // ── Context menu ────────────────────────────────────────
    private void drawContext(Graphics2D g2) {
        List<Item> items = getFilteredItems();
        if (getSelectedItem(items) == null) return;

        int r     = gridRow - scrollOffset;
        int cellX = innerX + PAD + gridCol * (CELL_W + CELL_GAP);
        int cellY = gridStartY + PAD + r * (CELL_H + CELL_GAP);

        int ctxW = 200;
        int ctxH = BORDER * 2 + CONTEXT_ITEMS.length * (int)(FONT_SIZE + 12);
        int ctxX = cellX + CELL_W + 10;
        int ctxY = cellY;

        // Clamp ke dalam panel
        if (ctxX + ctxW > panelX + PANEL_W - BORDER) ctxX = cellX - ctxW - 10;

        g2.setColor(Color.decode(COLOR_BORDER));
        g2.fillRect(ctxX, ctxY, ctxW, ctxH);
        g2.setColor(Color.decode(COLOR_BG));
        g2.fillRect(ctxX + BORDER, ctxY + BORDER, ctxW - BORDER * 2, ctxH - BORDER * 2);

        g2.setFont(font);
        FontMetrics fm = g2.getFontMetrics();
        int itemH = fm.getHeight() + 12;
        int textX = ctxX + BORDER + 16;
        int textY = ctxY + BORDER + fm.getAscent() + 8;

        for (int i = 0; i < CONTEXT_ITEMS.length; i++) {
            g2.setColor(i == contextIndex
                ? Color.decode(COLOR_HIGHLIGHT)
                : Color.decode(COLOR_TEXT));
            g2.drawString(CONTEXT_ITEMS[i], textX, textY + i * itemH);
        }
    }

    // =========================================================
    // DRAW HELPERS
    // =========================================================

    private void drawLabelValue(Graphics2D g2, FontMetrics fm,
                                 String label, String value,
                                 int labelX, int maxX, int y) {
        g2.setColor(Color.decode(COLOR_TEXT_FIXED));
        g2.drawString(label, labelX, y);
        g2.setColor(Color.decode(COLOR_TEXT));
        g2.drawString(value, maxX - PAD - fm.stringWidth(value), y);
    }

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

    private String formatNumber(int n) {
        if (n < 1000) return String.valueOf(n);
        return String.format("%,d", n).replace(',', ' ');
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.charAt(0) + s.substring(1).toLowerCase();
    }
}