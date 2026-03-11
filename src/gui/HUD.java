package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import javax.imageio.ImageIO;
import main.GamePanel;
import object.ObjectManager;

public class HUD {

    // Interact Prompt Settings (Sudah kembali pakai camelCase)
    static final int promptIconW = 48;
    static final int promptIconH = 48;
    static final float promptFontSize = 32f;
    static final int promptIconTextGap = 2;
    static final float promptTracking = -0.15f;
    static final int promptPaddingX = 30;
    static final int promptPaddingY = 30;
    
    // Tambahan variabel untuk mengakali ruang transparan di gambar PNG
    static final int iconOffsetY = 10; // Tambah angkanya kalau icon masih kurang turun

    GamePanel gp;
    BufferedImage statsBar;
    BufferedImage itemSlot;
    BufferedImage buttonE;
    Font pixelFont;

    public HUD(GamePanel gp) {
        this.gp = gp;

        try {
            statsBar = ImageIO.read(getClass().getResourceAsStream("/gui/HUD_stats_bar.png"));
            itemSlot = ImageIO.read(getClass().getResourceAsStream("/gui/HUD_item_slot.png"));
            buttonE  = ImageIO.read(getClass().getResourceAsStream("/gui/HUD_buttonE.png"));

            InputStream fontIs = getClass().getResourceAsStream("/fonts/Eternafall8bit.ttf");
            pixelFont = Font.createFont(Font.TRUETYPE_FONT, fontIs).deriveFont(promptFontSize);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        g2.drawImage(statsBar, promptPaddingX, promptPaddingY, null);
        
        g2.drawImage(itemSlot, promptPaddingX, gp.getHeight() - itemSlot.getHeight() - promptPaddingY, null);
    }

    public void drawInteractPrompt(Graphics2D g2) {

        if (gp.interactionM.currentTarget == null) return;
        if (!(gp.interactionM.currentTarget instanceof ObjectManager obj)) return;
        if (obj.interactPrompt == null || obj.interactPrompt.isEmpty()) return;
        if (pixelFont == null || buttonE == null) return;

        String label = obj.interactPrompt;

        Font trackedFont = pixelFont.deriveFont(Map.of(TextAttribute.TRACKING, promptTracking));

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        g2.setFont(trackedFont);

        FontRenderContext frc = g2.getFontRenderContext();
        TextLayout layout = new TextLayout(label, trackedFont, frc);
        int textWidth = (int) layout.getAdvance();

        FontMetrics fm = g2.getFontMetrics(trackedFont);
        int textHeight = fm.getAscent() - fm.getDescent();

        int x = gp.getWidth() - promptPaddingX - promptIconW - promptIconTextGap - textWidth;
        x = Math.max(promptPaddingX, x);
        
        int y = gp.getHeight() - promptPaddingY;
        
        // Di sini kita tambahkan iconOffsetY agar posisinya bisa turun menutupi area transparan
        int iconY = y - promptIconH + iconOffsetY; 

        g2.drawImage(buttonE, x, iconY, promptIconW, promptIconH, null);

        // Posisi teks juga disesuaikan supaya tetap di tengah-tengah icon
        int textY = iconY + (promptIconH / 2) + (textHeight / 2);

        g2.setColor(Color.white);
        g2.drawString(label, x + promptIconW + promptIconTextGap, textY);
        
    }
}