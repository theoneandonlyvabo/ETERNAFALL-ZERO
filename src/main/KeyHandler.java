package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    GamePanel gp;

    // -------------------------
    // KEYBINDS
    // -------------------------
    static final int KEY_UP        = KeyEvent.VK_W;
    static final int KEY_DOWN      = KeyEvent.VK_S;
    static final int KEY_LEFT      = KeyEvent.VK_A;
    static final int KEY_RIGHT     = KeyEvent.VK_D;
    static final int KEY_INTERACT  = KeyEvent.VK_E;
    static final int KEY_PAUSE     = KeyEvent.VK_ESCAPE;
    static final int KEY_NAV       = KeyEvent.VK_TAB;
    static final int KEY_PLAYER    = KeyEvent.VK_P;
    static final int KEY_INVENTORY = KeyEvent.VK_I;
    static final int KEY_MAP       = KeyEvent.VK_M;
    static final int KEY_QUEST     = KeyEvent.VK_K;
    static final int KEY_DEBUG     = KeyEvent.VK_BACK_QUOTE;
    // -------------------------

    // Index sinkron sama UI_Nav.NAV_ITEMS
    static final int NAV_PLAYER    = 0;
    static final int NAV_INVENTORY = 1;
    static final int NAV_MAP       = 2;
    static final int NAV_QUEST     = 3;
    static final int NAV_SETTINGS  = 4;

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

        // ── Dialog aktif ──
        if (gp.dialogManager.isActive) {
            if (code == KEY_INTERACT) {
                gp.dialogUI.onAdvance();
                interactPressed = false;
            }
            if (code == KEY_NAV || code == KEY_PAUSE) {
                gp.dialogManager.forceClose();
                gp.dialogUI.reset();
                interactPressed = false;
            }
            return;
        }

        // ── Player panel aktif ──
        if (gp.nav.isOpen() && gp.nav.panelOpen && gp.nav.activeIndex == NAV_PLAYER) {
            switch (code) {
                case KeyEvent.VK_W -> gp.playerPanel.onUp();
                case KeyEvent.VK_S -> gp.playerPanel.onDown();
                case KeyEvent.VK_A -> gp.playerPanel.onLeft();
                case KeyEvent.VK_D -> gp.playerPanel.onRight();
                case KeyEvent.VK_E -> gp.playerPanel.onConfirm();
                default -> {
                    // TAB atau ESC: kalau context buka tutup context, kalau tidak balik ke nav
                    if (code == KEY_NAV || code == KEY_PAUSE) {
                        if (gp.playerPanel.contextOpen) {
                            gp.playerPanel.onCancel();
                        } else {
                            gp.playerPanel.reset();
                            gp.nav.onTab();
                        }
                    }
                }
            }
            return;
        }

        // ── Nav open ──
        if (gp.nav.isOpen()) {

            if (code == KEY_NAV) { gp.nav.onTab(); return; }

            if (gp.nav.panelOpen) {
                if (code == KEY_PLAYER)    { gp.nav.onShortcut(NAV_PLAYER);    return; }
                if (code == KEY_INVENTORY) { gp.nav.onShortcut(NAV_INVENTORY); return; }
                if (code == KEY_MAP)       { gp.nav.onShortcut(NAV_MAP);       return; }
                if (code == KEY_QUEST)     { gp.nav.onShortcut(NAV_QUEST);     return; }
                if (code == KEY_PAUSE)     { gp.nav.onShortcut(NAV_SETTINGS);  return; }
                return;
            }

            if (code == KEY_UP)       { gp.nav.onNavUp();   return; }
            if (code == KEY_DOWN)     { gp.nav.onNavDown(); return; }
            if (code == KEY_INTERACT) { gp.nav.onConfirm(); return; }

            if (code == KEY_PLAYER)    { gp.nav.onShortcut(NAV_PLAYER);    return; }
            if (code == KEY_INVENTORY) { gp.nav.onShortcut(NAV_INVENTORY); return; }
            if (code == KEY_MAP)       { gp.nav.onShortcut(NAV_MAP);       return; }
            if (code == KEY_QUEST)     { gp.nav.onShortcut(NAV_QUEST);     return; }
            if (code == KEY_PAUSE)     { gp.nav.onShortcut(NAV_SETTINGS);  return; }

            return;
        }

        // ── Exploration ──
        if (code == KEY_UP)    { upPressed = true;    lastDir = FacingDirection.UP;    }
        if (code == KEY_DOWN)  { downPressed = true;  lastDir = FacingDirection.DOWN;  }
        if (code == KEY_LEFT)  { leftPressed = true;  lastDir = FacingDirection.LEFT;  }
        if (code == KEY_RIGHT) { rightPressed = true; lastDir = FacingDirection.RIGHT; }
        if (code == KEY_INTERACT) interactPressed = true;

        if (code == KEY_NAV) {
            if (!isMoving() && gp.gameState == GamePanel.worldState && !gp.dialogManager.isActive) {
                gp.nav.onTab();
            }
            return;
        }

        if (!isMoving() && gp.gameState == GamePanel.worldState) {
            if (code == KEY_PLAYER)    { gp.nav.onShortcut(NAV_PLAYER);    return; }
            if (code == KEY_INVENTORY) { gp.nav.onShortcut(NAV_INVENTORY); return; }
            if (code == KEY_MAP)       { gp.nav.onShortcut(NAV_MAP);       return; }
            if (code == KEY_QUEST)     { gp.nav.onShortcut(NAV_QUEST);     return; }
        }

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