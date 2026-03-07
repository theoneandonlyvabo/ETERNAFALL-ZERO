package main;

import gui.HUD;
import java.awt.Graphics2D;

public class UserInterface {

    GamePanel gp;

    HUD hud;

    public UserInterface(GamePanel gp) {
        this.gp = gp;
        hud = new HUD(gp);
    }

    public void draw(Graphics2D g2) {

        switch (gp.gameState) {
            
            case GamePanel.worldState -> hud.draw(g2);
            
            case GamePanel.pausedState -> {}
            
            case GamePanel.battleState -> {}
            
            case GamePanel.dialogState -> {}
            
            case GamePanel.inventoryState -> {}
            
            case GamePanel.transitionState -> {}
    }
}
}