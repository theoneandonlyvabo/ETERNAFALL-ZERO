package main;

import gui.HUD;
import gui.UI_Paused;
import java.awt.Graphics2D;

public class UserInterface {

    GamePanel gp;

    HUD hud;
    UI_Paused paused;

    public UserInterface(GamePanel gp) {

        this.gp = gp;
        hud = new HUD(gp);
        paused = new UI_Paused(gp);
        
    }

    public void draw(Graphics2D g2) {

        if (gp.gameState == gp.worldState) {
            hud.draw(g2);
        } else if (gp.gameState == gp.menuState) {
            paused.draw(g2);
        } else if (gp.gameState == gp.battleState) {
            // battle
        }

    }

}