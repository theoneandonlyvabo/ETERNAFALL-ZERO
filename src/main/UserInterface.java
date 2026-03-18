package main;

import gui.HUD;
import gui.UI_ActionBar;
import gui.UI_Equipment;
import gui.UI_Inventory;
import gui.UI_Map;
import gui.UI_Paused;
import java.awt.Graphics2D;

public class UserInterface {

    GamePanel gp;

    // UI Components
    HUD hud;
    public UI_Paused paused;
    public UI_ActionBar actionBar;
    UI_Equipment equipment;
    UI_Inventory inventory;
    UI_Map map;

    public UserInterface(GamePanel gp) {
        this.gp = gp;
        hud       = new HUD(gp);
        paused    = new UI_Paused(gp);
        actionBar = new UI_ActionBar(gp);
        equipment = new UI_Equipment(gp);
        inventory = new UI_Inventory(gp);
        map       = new UI_Map(gp);
    }

    public void draw(Graphics2D g2) {

        // World UI — selalu draw kecuali battle
        if (gp.gameState != GamePanel.battleState) {
            hud.draw(g2);

            UI_ActionBar.Mode mode = actionBar.getMode();

            if (mode == UI_ActionBar.Mode.EQUIPMENT) equipment.draw(g2);
            if (mode == UI_ActionBar.Mode.INVENTORY)  inventory.draw(g2);
            if (mode == UI_ActionBar.Mode.MAP)         map.draw(g2);

            if (actionBar.isOpen()) actionBar.draw(g2);
        }

        // Paused overlay — draw di atas segalanya
        if (gp.gameState == GamePanel.pausedState) {
            paused.draw(g2);
        }

        // Battle UI
        if (gp.gameState == GamePanel.battleState) {
            // TODO: battle UI
        }
    }
}