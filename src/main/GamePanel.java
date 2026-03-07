package main;

import entity.Player;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import object.ObjectManager;
import object.ObjectSetter;
import world.TileManager;

public class GamePanel extends JPanel implements Runnable {
    
    // SCREEN SETTINGS

    final int originalTileSize = 16; // Single Tile Game Resolution (16x16 Pixels)

    public final int maxScreenCol = 16; // Screen Width (Px)
    public final int maxScreenRow = 9; // Screen Height (Px)
    public final int scale = 5; // Screen Size

    public final int tileSize = originalTileSize * scale;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

        // SCREEN EFFECTS

        float fadeAlpha = 1f; // 1 = gelap, 0 = terang
        boolean fading = true;

    // WORLD SETTINGS

    public final int maxWorldCol = 100; // World Width Limit
    public final int maxWorldRow = 100; // World Height Limit

    // FPS

    int FPS = 60;

    // SYSTEM

    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler();
    Sound music = new Sound();
    Sound SFX = new Sound();
    public CollisionChecker cChecker = new CollisionChecker(this);
    public ObjectSetter oSetter = new ObjectSetter(this);
    Thread gameThread;

    // GAME STATE

    public static final int worldState = 0;
    public static final int pausedState = 1;
    public static final int battleState = 2;
    public static final int dialogState = 3;
    public static final int inventoryState = 4;
    public static final int transitionState = 5;

    public int gameState = worldState;  

    // UI SETTINGS

    public UserInterface ui = new UserInterface(this);

    // ENTITY AND OBJECT

    public Player player = new Player(this, keyH);
    public ObjectManager obj[] = new ObjectManager[10];

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void loadMap() {

        oSetter.setObject();

        playSFX(1);

        playMusic(0);

    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        double drawInterval = 1000000000 / FPS; // 0.01666 seconds
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) {

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }

            if (timer >= 1000000000) {
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update() {

        player.update();

        if (fading) {

            // Fade Duration
            fadeAlpha -= 1f / 120f;

                if (fadeAlpha <= 0) {
                     fadeAlpha = 0;
                    fading = false;
                }
            }

    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Tile
        tileM.draw(g2);

        // Object
        for (int i = 0; i < obj.length; i++) {
            if (obj[i] != null) {
                obj[i].draw(g2, this);
            }
        }

        // Player
        player.draw(g2);

        // UI
        ui.draw(g2);

        // Screen Effects
        if (fading) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fadeAlpha));
            g2.setColor(Color.black);
            g2.fillRect(0, 0, screenWidth, screenHeight);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }

        g2.dispose();
    }

    public void playMusic(int i) {

        music.setFile(i);
        music.play();
        music.loop();

    }

    public void stopMusic() {

        music.stop();

    }

    public void playSFX(int i) {
        
        SFX.setFile(i);
        SFX.play();
    }
}