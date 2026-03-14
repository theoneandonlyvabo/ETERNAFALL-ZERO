package main;

import entity.Entity;
import entity.Player;
import item.ItemManager;
import java.awt.AlphaComposite;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import object.ObjectManager;
import object.ObjectSetter;
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
    Sound SFX = new Sound();
    public CollisionChecker cChecker = new CollisionChecker(this);
    public ObjectSetter oSetter = new ObjectSetter(this);
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

    // ENTITY AND OBJECT
    public Player player = new Player(this, keyH);
    public ObjectManager obj[] = new ObjectManager[10];

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
        this.setFocusTraversalKeysEnabled(false); // biar TAB sampai ke KeyListener
    }

    public void loadMap() {
        oSetter.setObject();
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
        long currentTime;
        long timer = 0;
        long gameTimer = 0;
        int drawCount = 0;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            long elapsed = currentTime - lastTime;
            lastTime = currentTime;

            // Delta dan timer hanya akumulasi kalau bukan pausedState
            if (gameState != pausedState) {
                delta += elapsed / drawInterval;
                timer += elapsed;
                if (!fading) gameTimer += elapsed;
            }

            if (delta >= 1) {
                update();
                render(bs);
                delta--;
                drawCount++;
            } else {
                // Kalau pausedState, tetap render tapi skip update
                if (gameState == pausedState) {
                    render(bs);
                }
                try {
                    Thread.sleep(1);
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
        // World logic skip kalau actionbar buka
        if (gameState == worldState && !fading && !ui.actionBar.isOpen()) {
            player.update();
            interactionM.update();
            itemManager.update(1f / FPS);
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

        // Clear
        g2.setColor(Color.black);
        g2.fillRect(0, 0, screenWidth, screenHeight);

        long drawStart = 0;
        if (keyH.checkDrawTime) {
            drawStart = System.nanoTime();
        }

        // 1. Tile
        tileM.draw(g2);

        // 2. Y-Sorting
        entityList.add(player);
        for (int i = 0; i < obj.length; i++) {
            if (obj[i] != null) entityList.add(obj[i]);
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
        entityList.clear();

        // 3. UI
        ui.draw(g2);

        // 4. Interaction prompt
        interactionM.draw(g2);

        // 5. Debug HUD
        if (keyH.checkDrawTime) {
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;

            Font debugFont = new Font("Monospaced", Font.PLAIN, 20);
            g2.setFont(debugFont);

            int x = 30;
            int lineH = 26;
            int y = screenHeight / 2 - lineH;
            int padX = 10;
            int padY = 8;
            int bgW = 270;
            int bgH = lineH * 4 + padY * 2;

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            g2.setColor(Color.black);
            g2.fillRect(x - padX, y - lineH - padY + 6, bgW, bgH);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

            g2.setColor(Color.white);
            g2.drawString("FPS        : " + currentFPS, x, y);

            y += lineH;
            g2.drawString("Frame Time : " + passed + " ns", x, y);

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

        // 6. Screen Effects
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