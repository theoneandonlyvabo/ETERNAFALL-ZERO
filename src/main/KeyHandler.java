package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    GamePanel gp;

    public boolean upPressed, downPressed, leftPressed, rightPressed;
    public boolean interactPressed;
    public FacingDirection lastDir = FacingDirection.DOWN;

    // Debug
    public boolean checkDrawTime = false;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) {
            upPressed = true;
            lastDir = FacingDirection.UP;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = true;
            lastDir = FacingDirection.LEFT;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = true;
            lastDir = FacingDirection.DOWN;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = true;
            lastDir = FacingDirection.RIGHT;
        }
        if (code == KeyEvent.VK_E) {
            interactPressed = true;
        }
        
        if (code == KeyEvent.VK_ESCAPE) {
            if (gp.gameState == gp.worldState) {
                gp.gameState = gp.menuState;
                gp.delta = 0;
            } else if (gp.gameState == gp.menuState) {
                gp.gameState = gp.worldState;
                gp.delta = 0;
            }
}

        // Debug
        if (code == KeyEvent.VK_BACK_QUOTE) {
            checkDrawTime = !checkDrawTime;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) upPressed = false;
        if (code == KeyEvent.VK_A) leftPressed = false;
        if (code == KeyEvent.VK_S) downPressed = false;
        if (code == KeyEvent.VK_D) rightPressed = false;
        if (code == KeyEvent.VK_E) interactPressed = false;
    }
}