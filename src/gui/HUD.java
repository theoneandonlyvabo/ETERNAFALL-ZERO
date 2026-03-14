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

    // Interact Prompt Settings
    static final int promptIconW = 48;
    static final int promptIconH = 48;
    static final float promptFontSize = 32f;
    static final int promptIconTextGap = 2;
    static final float promptTracking = 0f;
    static final int promptPaddingX = 30;
    static final int promptPaddingY = 30;
    static final int iconOffsetY = -40;

    GamePanel gp;
    BufferedImage playerBar;
    BufferedImage buttonE;
    Font pixelFont;

    public HUD(GamePanel gp) {
        this.gp = gp;

        try {
            playerBar = ImageIO.read(getClass().getResourceAsStream("/gui/HUD_playerbar.png"));
            buttonE   = ImageIO.read(getClass().getResourceAsStream("/gui/HUD_buttonE.png"));

            InputStream fontIs = getClass().getResourceAsStream("/fonts/upheaval.ttf");
            pixelFont = Font.createFont(Font.TRUETYPE_FONT, fontIs).deriveFont(promptFontSize);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        g2.drawImage(playerBar, 0, 0, null);
        drawShards(g2);
    }

    private void drawShards(Graphics2D g2) {

        if (pixelFont == null) return;

        g2.setFont(pixelFont.deriveFont(42f));
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        g2.setColor(Color.decode("#3b6d62"));
        g2.drawString(String.valueOf(gp.itemManager.witherShards), 1358, 855);

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

        int x = gp.screenWidth - promptPaddingX - promptIconW - promptIconTextGap - textWidth;
        x = Math.max(promptPaddingX, x);

        int y = gp.screenHeight - promptPaddingY;
        int iconY = y - promptIconH + iconOffsetY;

        g2.drawImage(buttonE, x, iconY, promptIconW, promptIconH, null);

        int textY = iconY + (promptIconH / 2) + (textHeight / 2);

        g2.setColor(Color.white);
        g2.drawString(label, x + promptIconW + promptIconTextGap, textY);
    }
}