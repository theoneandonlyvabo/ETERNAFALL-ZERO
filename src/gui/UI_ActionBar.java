package gui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class UI_ActionBar {

    public enum Mode {
        NONE, NAV, EQUIPMENT, INVENTORY, MAP, DIALOG, SHOP
    }

    GamePanel gp;
    BufferedImage actionBarImg;
    private Mode mode = Mode.NONE;

    public UI_ActionBar(GamePanel gp) {
        this.gp = gp;

        try {
            actionBarImg = ImageIO.read(getClass().getResourceAsStream("/gui/HUD_actionbar.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- State ---

    public Mode getMode() { return mode; }
    public boolean isOpen() { return mode != Mode.NONE; }

    public void setMode(Mode mode) { this.mode = mode; }

    public void close() {
        this.mode = Mode.NONE;
    }

    // TAB boleh kalau NONE, NAV, atau panel aktif
    public boolean canToggleNav() {
        return mode == Mode.NONE
            || mode == Mode.NAV
            || mode == Mode.EQUIPMENT
            || mode == Mode.INVENTORY
            || mode == Mode.MAP;
    }

    // F/I/M boleh kalau bukan mode locked (DIALOG/SHOP)
    public boolean canTogglePanel() {
        return mode == Mode.NONE
            || mode == Mode.NAV
            || mode == Mode.EQUIPMENT
            || mode == Mode.INVENTORY
            || mode == Mode.MAP;
    }

    // --- Draw ---

    public void draw(Graphics2D g2) {

        if (actionBarImg == null) return;

        g2.drawImage(actionBarImg, 0, 0, null);

        switch (mode) {
            case NAV       -> drawNav(g2);
            case EQUIPMENT -> drawEquipmentContext(g2);
            case INVENTORY -> drawInventoryContext(g2);
            case MAP       -> drawMapContext(g2);
            case DIALOG    -> drawDialogContext(g2);
            case SHOP      -> drawShopContext(g2);
            default        -> {}
        }

    }

    private void drawNav(Graphics2D g2) {
        // TODO: gambar nav items
    }

    private void drawEquipmentContext(Graphics2D g2) {
        // TODO: highlight equipment tab
    }

    private void drawInventoryContext(Graphics2D g2) {
        // TODO: highlight inventory tab
    }

    private void drawMapContext(Graphics2D g2) {
        // TODO: highlight map tab
    }

    private void drawDialogContext(Graphics2D g2) {
        // TODO: render dialog NPC
    }

    private void drawShopContext(Graphics2D g2) {
        // TODO: render shop
    }
    
}