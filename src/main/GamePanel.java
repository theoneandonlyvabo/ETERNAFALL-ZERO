package main;

import battle.BattleManager;
import dialog.DialogManager;
import entity.Entity;
import entity.Player;
import gui.UI_Dialog;
import gui.UI_Inventory;
import gui.UI_Nav;
import gui.UI_Player;
import item.ItemManager;
import java.awt.AlphaComposite;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import object.ObjectManager;
import world.TileManager;

public class GamePanel extends Canvas implements Runnable {

    // SCREEN SETTINGS
    final int originalTileSize = 16;
    public final int maxScreenCol = 19;
    public final int maxScreenRow = 10;
    public final int scale = 5;

    public final int tileSize = originalTileSize * scale;
    public final int screenWidth = 1536;
    public final int screenHeight = 864;

    // SCREEN EFFECTS
    float fadeAlpha = 1f;
    boolean fading = true;

    // WORLD SETTINGS
    public final int maxWorldCol = 100;
    public final int maxWorldRow = 100;

    // FPS
    int FPS = 60;

    // SYSTEM
    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler(this);
    public Sound music = new Sound();
    public Sound SFX = new Sound();
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public InteractionManager interactionM = new InteractionManager(this);
    public ItemManager itemManager = new ItemManager();
    Thread gameThread;

    // GAME STATE
    public int gameState;
    public static final int pausedState = 0;
    public static final int worldState  = 1;
    public static final int battleState = 2;

    // UI
    public UserInterface ui = new UserInterface(this);
    public DialogManager dialogManager = new DialogManager(this);
    public UI_Dialog dialogUI = new UI_Dialog(this);
    public UI_Nav nav = new UI_Nav(this);
    public UI_Player playerPanel = new UI_Player(this);
    public UI_Inventory inventoryPanel = new UI_Inventory(this);

    // BATTLE
    public BattleManager battleManager = new BattleManager(this);

    // ENTITY AND OBJECT
    public Player player = new Player(this, keyH);
    public ObjectManager obj[] = new ObjectManager[10];
    public Entity npc[] = new Entity[30];

    // Y-SORTING LIST
    ArrayList<Object> entityList = new ArrayList<>();

    // DEBUG
    public double delta = 0;
    int currentFPS = 0;
    long gameTimerSeconds = 0;
    long gameTimerMs = 0;

    public GamePanel() {

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(false);

    }

    public void setupGame() {

        aSetter.setObject();
        aSetter.setNPC();
        playSFX(2);
        playMusic(0);
        playMusic(1);
        gameState = worldState;

    }

    public void startGameThread() {

        gameThread = new Thread(this);
        gameThread.start();

    }

    @Override
    public void run() {

        createBufferStrategy(3);
        BufferStrategy bs = getBufferStrategy();

        double drawInterval = 1000000000.0 / FPS;
        long lastTime = System.nanoTime();
        long timer = 0;
        long gameTimer = 0;
        int drawCount = 0;

        while (gameThread != null) {

            long currentTime = System.nanoTime();
            long elapsed = currentTime - lastTime;
            lastTime = currentTime;

            if (gameState != pausedState) {
                delta += elapsed / drawInterval;
                timer += elapsed;
                if (!fading) gameTimer += elapsed;
            }

            if (gameState != pausedState) {
                while (delta >= 1) {
                    update();
                    delta--;
                }
                render(bs);
                drawCount++;
            } else {
                render(bs);
                try {
                    Thread.sleep(8);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (timer >= 1000000000) {
                currentFPS = drawCount;
                drawCount = 0;
                timer = 0;
            }

            if (gameTimer >= 1000000000) {
                gameTimerSeconds++;
                gameTimer -= 1000000000;
            }
            gameTimerMs = gameTimer / 1000000;

        }
    }

    public void update() {

        if (gameState == worldState && !fading && !nav.isOpen() && !dialogManager.isActive) {
            player.update();
            itemManager.update(1f / FPS);
        }

        if (gameState == worldState && !fading && !nav.isOpen()) {
            interactionM.update();
        }

        if (dialogManager.isActive) {
            dialogUI.update();
        }

        if (gameState == battleState) {
            // TODO: battleUI.update() dipanggil di sini setelah UI_Battle dibuat
        }

        if (fading) {
            fadeAlpha -= 1f / 60f;
            if (fadeAlpha <= 0) {
                fadeAlpha = 0;
                fading = false;
                delta = 0;
            }
        }

    }

    public void render(BufferStrategy bs) {

        Graphics2D g2 = (Graphics2D) bs.getDrawGraphics();

        g2.setColor(Color.black);
        g2.fillRect(0, 0, screenWidth, screenHeight);

        long drawStart = 0;
        if (keyH.checkDrawTime) {
            drawStart = System.nanoTime();
        }

        if (gameState == worldState || gameState == pausedState) {

            // Tile
            tileM.draw(g2);

            // Y-Sorting
            entityList.clear();
            entityList.add(player);
            for (int i = 0; i < obj.length; i++) {
                if (obj[i] != null) entityList.add(obj[i]);
            }
            for (int i = 0; i < npc.length; i++) {
                if (npc[i] != null) entityList.add(npc[i]);
            }

            Collections.sort(entityList, new Comparator<Object>() {
                @Override
                public int compare(Object o1, Object o2) {
                    int y1 = 0, y2 = 0;
                    if (o1 instanceof Entity) y1 = ((Entity) o1).worldY;
                    else if (o1 instanceof ObjectManager) y1 = ((ObjectManager) o1).worldY;
                    if (o2 instanceof Entity) y2 = ((Entity) o2).worldY;
                    else if (o2 instanceof ObjectManager) y2 = ((ObjectManager) o2).worldY;
                    return Integer.compare(y1, y2);
                }
            });

            for (Object renderObj : entityList) {
                if (renderObj instanceof Entity) ((Entity) renderObj).draw(g2);
                else if (renderObj instanceof ObjectManager) ((ObjectManager) renderObj).draw(g2, this);
            }

            // UI
            ui.draw(g2);

            // Interaction prompt
            interactionM.draw(g2);

            // Navbar
            nav.draw(g2);
            if (nav.panelOpen && nav.activeIndex == 0) playerPanel.draw(g2);
            if (nav.panelOpen && nav.activeIndex == 1) inventoryPanel.draw(g2);
            dialogUI.draw(g2);

        }

        if (gameState == battleState) {
            // TODO: battleUI.draw(g2) dipanggil di sini setelah UI_Battle dibuat
        }

        // Debug HUD
        if (keyH.checkDrawTime) {
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;

            Font debugFont = new Font("Monospaced", Font.PLAIN, 20);
            g2.setFont(debugFont);

            int x = 30;
            int lineH = 26;
            int y = 45;
            int padX = 10;
            int padY = 8;
            int bgW = 285;
            int bgH = lineH * 4 + padY * 2;

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            g2.setColor(Color.black);
            g2.fillRect(x - padX, y - lineH - padY + 6, bgW, bgH);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

            g2.setColor(Color.white);
            g2.drawString("FPS        : " + currentFPS, x, y);

            y += lineH;
            g2.drawString(String.format("Frame Time : %.2f ms", passed / 1_000_000.0), x, y);

            y += lineH;
            boolean spike = delta >= 1.4;
            g2.setColor(spike ? Color.red : Color.green);
            g2.drawString(String.format("Delta      : %.4f%s", delta, spike ? "  SPIKE" : ""), x, y);

            y += lineH;
            long mins = gameTimerSeconds / 60;
            long secs = gameTimerSeconds % 60;
            g2.setColor(Color.white);
            g2.drawString(String.format("Time       : %02d:%02d:%03d", mins, secs, gameTimerMs), x, y);
        }

        // Screen Effects
        if (fading) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fadeAlpha));
            g2.setColor(Color.black);
            g2.fillRect(0, 0, screenWidth, screenHeight);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }

        g2.dispose();
        bs.show();

        Toolkit.getDefaultToolkit().sync();

    }

    public void playMusic(int i) {

        music.setFile(i);
        music.play(i);
        music.loop(i);

    }

    public void stopMusic() {

        music.stopAll();

    }

    public void playSFX(int i) {

        SFX.setFile(i);
        SFX.play(i);

    }

}