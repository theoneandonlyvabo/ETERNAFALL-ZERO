package main;

import gui.UI_ActionBar.Mode;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    GamePanel gp;

    // -------------------------
    // KEYBINDS — edit di sini
    // -------------------------
    static final int KEY_UP        = KeyEvent.VK_W;
    static final int KEY_DOWN      = KeyEvent.VK_S;
    static final int KEY_LEFT      = KeyEvent.VK_A;
    static final int KEY_RIGHT     = KeyEvent.VK_D;
    static final int KEY_INTERACT  = KeyEvent.VK_E;
    static final int KEY_PAUSE     = KeyEvent.VK_ESCAPE;
    static final int KEY_NAV       = KeyEvent.VK_TAB;
    static final int KEY_EQUIPMENT = KeyEvent.VK_F;
    static final int KEY_INVENTORY = KeyEvent.VK_I;
    static final int KEY_MAP       = KeyEvent.VK_M;
    static final int KEY_DEBUG     = KeyEvent.VK_BACK_QUOTE;
    // -------------------------

    public boolean upPressed, downPressed, leftPressed, rightPressed;
    public boolean interactPressed;

    public FacingDirection lastDir = FacingDirection.DOWN;

    public boolean checkDrawTime = false;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    private boolean isMoving() {
        return upPressed || downPressed || leftPressed || rightPressed;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();

        // Dialog aktif — konsumsi E, block semua input lain
        if (gp.dialogManager.isActive) {
            if (code == KEY_INTERACT) {
                gp.dialogUI.onAdvance();
                interactPressed = false;
            }
            if (code == KEY_NAV || code == KEY_PAUSE) {
                gp.dialogManager.forceClose();
                interactPressed = false;
            }
            return;
        }

        if (code == KEY_UP)       { upPressed = true;    lastDir = FacingDirection.UP;    }
        if (code == KEY_DOWN)     { downPressed = true;  lastDir = FacingDirection.DOWN;  }
        if (code == KEY_LEFT)     { leftPressed = true;  lastDir = FacingDirection.LEFT;  }
        if (code == KEY_RIGHT)    { rightPressed = true; lastDir = FacingDirection.RIGHT; }
        if (code == KEY_INTERACT) interactPressed = true;

        if (code == KEY_PAUSE) {
            if (gp.gameState == GamePanel.worldState) {
                gp.gameState = GamePanel.pausedState;
                gp.music.pauseAll();
                gp.delta = 0;
            } else if (gp.gameState == GamePanel.pausedState) {
                gp.gameState = GamePanel.worldState;
                gp.music.resumeAll();
                gp.delta = 0;
            }
        }

        if (code == KEY_NAV) {
            if (!isMoving() && gp.gameState == GamePanel.worldState && !gp.dialogManager.isActive && gp.ui.actionBar.canToggleNav()) {
                Mode mode = gp.ui.actionBar.getMode();
                if (mode == Mode.NONE) {
                    gp.ui.actionBar.setMode(Mode.NAV);
                } else if (mode == Mode.NAV) {
                    gp.ui.actionBar.close();
                } else {
                    gp.ui.actionBar.setMode(Mode.NAV);
                }
            }
        }

        if (code == KEY_EQUIPMENT) {
            if (!isMoving() && gp.gameState == GamePanel.worldState && gp.ui.actionBar.canTogglePanel()) {
                Mode mode = gp.ui.actionBar.getMode();
                if (mode == Mode.EQUIPMENT) {
                    gp.ui.actionBar.close();
                } else {
                    gp.ui.actionBar.setMode(Mode.EQUIPMENT);
                }
            }
        }

        if (code == KEY_INVENTORY) {
            if (!isMoving() && gp.gameState == GamePanel.worldState && gp.ui.actionBar.canTogglePanel()) {
                Mode mode = gp.ui.actionBar.getMode();
                if (mode == Mode.INVENTORY) {
                    gp.ui.actionBar.close();
                } else {
                    gp.ui.actionBar.setMode(Mode.INVENTORY);
                }
            }
        }

        if (code == KEY_MAP) {
            if (!isMoving() && gp.gameState == GamePanel.worldState && gp.ui.actionBar.canTogglePanel()) {
                Mode mode = gp.ui.actionBar.getMode();
                if (mode == Mode.MAP) {
                    gp.ui.actionBar.close();
                } else {
                    gp.ui.actionBar.setMode(Mode.MAP);
                }
            }
        }

        if (code == KEY_DEBUG) checkDrawTime = !checkDrawTime;
    }

    @Override
    public void keyReleased(KeyEvent e) {

        int code = e.getKeyCode();

        if (code == KEY_UP)       upPressed = false;
        if (code == KEY_DOWN)     downPressed = false;
        if (code == KEY_LEFT)     leftPressed = false;
        if (code == KEY_RIGHT)    rightPressed = false;
        if (code == KEY_INTERACT) interactPressed = false;
    }
}