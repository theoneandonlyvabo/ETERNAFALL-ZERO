package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import main.GamePanel;

public class UI_Dialog {

    private final GamePanel gp;
    private Font nameFont;
    private Font dialogFont;

    // CONFIG
    private static final float FONT_SIZE        = 32f;
    private static final int   BOX_W            = 900;
    private static final int   BOX_H            = 220;
    private static final int   BOX_ARC          = 16;
    private static final int   PAD_X            = 36;
    private static final int   PAD_Y            = 28;
    private static final int   TYPEWRITER_SPEED = 2;

    // State
    private String displayedText = "";
    private int charIndex    = 0;
    private int frameCounter = 0;
    private boolean isDone   = false;

    // Wrapped lines cache
    private List<String> wrappedTarget = new ArrayList<>();
    private String lastTarget = null;

    public UI_Dialog(GamePanel gp) {

        this.gp = gp;

        try {
            InputStream is = getClass().getResourceAsStream("/fonts/determination.ttf");
            Font base  = Font.createFont(Font.TRUETYPE_FONT, is);
            nameFont   = base.deriveFont(Font.PLAIN, FONT_SIZE * 0.75f);
            dialogFont = base.deriveFont(Font.PLAIN, FONT_SIZE);
        } catch (Exception e) {
            nameFont   = new Font("Arial", Font.BOLD,  (int)(FONT_SIZE * 0.75f));
            dialogFont = new Font("Arial", Font.PLAIN, (int) FONT_SIZE);
        }

    }

    public void update() {

        String target = gp.dialogManager.getCurrentLine();
        if (target == null) return;

        if (!target.equals(lastTarget)) {
            reset();
            lastTarget = target;
        }

        if (!isDone) {
            frameCounter++;
            if (frameCounter >= TYPEWRITER_SPEED) {
                frameCounter = 0;
                if (charIndex < target.length()) {
                    charIndex++;
                    displayedText = target.substring(0, charIndex);
                } else {
                    isDone = true;
                }
            }
        }

    }

    public void onAdvance() {

        String target = gp.dialogManager.getCurrentLine();
        if (target == null) return;

        if (!isDone) {
            displayedText = target;
            charIndex     = target.length();
            isDone        = true;
        } else {
            gp.dialogManager.advance();
            String next = gp.dialogManager.getCurrentLine();
            if (next != null) reset();
        }

    }

    public void draw(Graphics2D g2) {

        if (!gp.dialogManager.isActive) return;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int boxX = (gp.screenWidth  - BOX_W) / 2;
        int boxY = (gp.screenHeight - BOX_H) / 2;

        // Background
        g2.setColor(new Color(0, 0, 0, 210));
        g2.fill(new RoundRectangle2D.Float(boxX, boxY, BOX_W, BOX_H, BOX_ARC, BOX_ARC));

        // Border
        g2.setColor(new Color(255, 255, 255, 60));
        g2.draw(new RoundRectangle2D.Float(boxX, boxY, BOX_W, BOX_H, BOX_ARC, BOX_ARC));

        // Nama NPC
        String npcName = gp.dialogManager.getCurrentNpcName();
        String npcRole = gp.dialogManager.getCurrentNpcRole();
        g2.setFont(nameFont);
        FontMetrics nm = g2.getFontMetrics();
        if (npcName != null) {
            g2.setColor(new Color(200, 200, 200, 255));
            g2.drawString(npcName, boxX + PAD_X, boxY + PAD_Y + nm.getAscent());
        }
        if (npcRole != null && shouldShowRole(npcRole)) {
            g2.setFont(nameFont.deriveFont(FONT_SIZE * 0.5f));
            FontMetrics rm = g2.getFontMetrics();
            g2.setColor(new Color(160, 160, 160, 200));
            g2.drawString(capitalize(npcRole), boxX + PAD_X, boxY + PAD_Y + nm.getAscent() + rm.getAscent() + 2);
        }

        // Wrap dan draw teks dialog
        g2.setFont(dialogFont);
        FontMetrics fm = g2.getFontMetrics();

        int textX    = boxX + PAD_X;
        int maxWidth = BOX_W - PAD_X * 2;
        int lineH    = fm.getHeight();
        int textY    = boxY + PAD_Y + (int)(FONT_SIZE * 0.75f) + 16 + fm.getAscent();

        List<String> lines = wrapText(displayedText, fm, maxWidth);

        g2.setColor(Color.WHITE);
        for (String line : lines) {
            g2.drawString(line, textX, textY);
            textY += lineH;
        }

        // Indikator E
        if (isDone) {
            g2.setFont(nameFont);
            String hint = "E";
            FontMetrics hm = g2.getFontMetrics();
            int hx = boxX + BOX_W - hm.stringWidth(hint) - PAD_X;
            int hy = boxY + BOX_H - PAD_Y;
            g2.setColor(new Color(255, 255, 255, 160));
            g2.drawString(hint, hx, hy);
        }

    }

    // Pecah teks jadi list of lines sesuai maxWidth
    private List<String> wrapText(String text, FontMetrics fm, int maxWidth) {

        List<String> lines = new ArrayList<>();
        if (text == null || text.isEmpty()) return lines;

        String[] words  = text.split(" ");
        StringBuilder sb = new StringBuilder();

        for (String word : words) {
            String test = sb.length() == 0 ? word : sb + " " + word;
            if (fm.stringWidth(test) <= maxWidth) {
                sb = new StringBuilder(test);
            } else {
                if (sb.length() > 0) lines.add(sb.toString());
                sb = new StringBuilder(word);
            }
        }

        if (sb.length() > 0) lines.add(sb.toString());
        return lines;

    }

    private boolean shouldShowRole(String role) {
        return !role.equals("default") && !role.equals("lore");
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    private void reset() {
        displayedText = "";
        charIndex     = 0;
        frameCounter  = 0;
        isDone        = false;
    }

}