package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.Interactable;

public class UI_Dialog {

    private final GamePanel gp;
    private Font dialogFont;

    // =========================================================
    // CONFIG
    // =========================================================
    private static final float  FONT_SIZE        = 32f;
    private static final int    BOX_W            = 856;
    private static final int    BOX_H            = 180;
    private static final int    BORDER_THICKNESS = 8;
    private static final int    PAD_X            = 25;
    private static final int    PAD_Y            = 25;
    private static final int    TEXT_INDENT      = 192;
    private static final int    BOTTOM_OFFSET    = 40;
    private static final int    MAX_LINES        = 3;
    private static final int    LINE_SPACING     = 0;
    private static final int    TYPEWRITER_SPEED = 2;
    private static final int    PAUSE_COMMA      = 20;
    private static final int    PAUSE_PERIOD     = 30;
    private static final int    PAUSE_SEPARATOR  = 60;
    private static final int    ICON_SIZE        = 128;
    private static final int    ICON_PAD_X       = 32;
    private static final String LINE_SEPARATOR   = "|";

    private static final Color COLOR_BG     = Color.decode("#000000");
    private static final Color COLOR_BORDER = Color.decode("#4f493b");
    private static final Color COLOR_TEXT   = Color.decode("#a29f7e");
    // =========================================================

    // Icon cache
    private final Map<String, BufferedImage> iconCache = new HashMap<>();

    // State
    private String  displayedText = "";
    private int     charIndex     = 0;
    private int     frameCounter  = 0;
    private int     pauseCounter  = 0;
    private boolean isDone        = false;
    private String  lastTarget    = null;

    public UI_Dialog(GamePanel gp) {

        this.gp = gp;

        try {
            InputStream is = getClass().getResourceAsStream("/fonts/determination.ttf");
            Font base  = Font.createFont(Font.TRUETYPE_FONT, is);
            dialogFont = base.deriveFont(Font.PLAIN, FONT_SIZE);
        } catch (Exception e) {
            dialogFont = new Font("Monospaced", Font.PLAIN, (int) FONT_SIZE);
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
            if (pauseCounter > 0) {
                pauseCounter--;
                return;
            }
            frameCounter++;
            if (frameCounter >= TYPEWRITER_SPEED) {
                frameCounter = 0;
                if (charIndex < target.length()) {
                    charIndex++;
                    displayedText = target.substring(0, charIndex);
                    char last = target.charAt(charIndex - 1);
                    // Notify DialogManager tiap karakter muncul — skip separator dan spasi
                    if (last != '|' && last != ' ' && last != '.' && last != ',' && last != '!' 
                        && last != '?' && last != ':' && last != ';') gp.dialogManager.onCharRevealed();
                    if      (last == '.') pauseCounter = PAUSE_PERIOD;
                    else if (last == ',') pauseCounter = PAUSE_COMMA;
                    else if (last == '|') pauseCounter = PAUSE_SEPARATOR;
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
            int nextSep = target.indexOf('|', charIndex);
            if (nextSep != -1) {
                charIndex     = nextSep + 1;
                displayedText = target.substring(0, charIndex);
                pauseCounter  = 0;
                frameCounter  = 0;
            } else {
                displayedText = target;
                charIndex     = target.length();
                isDone        = true;
            }
        } else {
            gp.dialogManager.advance();
            String next = gp.dialogManager.getCurrentLine();
            if (next != null) reset();
        }

    }

    public void draw(Graphics2D g2) {

        if (!gp.dialogManager.isActive) return;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        int boxX = (gp.screenWidth - BOX_W) / 2;
        int boxY = gp.screenHeight - BOX_H - BOTTOM_OFFSET;

        // Background
        g2.setColor(COLOR_BG);
        g2.fillRect(boxX, boxY, BOX_W, BOX_H);

        // Border
        g2.setColor(COLOR_BORDER);
        g2.setStroke(new java.awt.BasicStroke(BORDER_THICKNESS));
        g2.drawRect(
            boxX + BORDER_THICKNESS / 2,
            boxY + BORDER_THICKNESS / 2,
            BOX_W - BORDER_THICKNESS,
            BOX_H - BORDER_THICKNESS
        );
        g2.setStroke(new java.awt.BasicStroke(1));

        // NPC Icon
        drawIcon(g2, boxX, boxY);

        // Text
        g2.setFont(dialogFont);
        FontMetrics fm = g2.getFontMetrics();

        int textX    = boxX + TEXT_INDENT;
        int maxWidth = BOX_W - TEXT_INDENT - PAD_X;
        int lineH    = fm.getHeight() + LINE_SPACING;
        int textY    = boxY + PAD_Y + fm.getAscent();

        String target = gp.dialogManager.getCurrentLine();
        if (target == null) return;

        List<RenderLine> displayLayout = buildLayout(displayedText, target, fm, maxWidth);

        g2.setColor(COLOR_TEXT);
        int drawn = 0;
        for (RenderLine rl : displayLayout) {
            if (drawn >= MAX_LINES) break;
            drawSegments(g2, rl.text, textX, textY + (drawn * lineH), fm);
            drawn++;
        }

    }

    // =========================================================
    // ICON
    // =========================================================
    private void drawIcon(Graphics2D g2, int boxX, int boxY) {

        Interactable target = gp.interactionM.currentTarget;
        if (target == null) return;

        String path = target.getIconPath();
        if (path == null) return;

        BufferedImage icon = iconCache.computeIfAbsent(path, p -> {
            try {
                InputStream is = getClass().getResourceAsStream(p);
                if (is == null) return null;
                return ImageIO.read(is);
            } catch (Exception e) {
                return null;
            }
        });

        if (icon == null) return;

        int iconX = boxX + ICON_PAD_X;
        int iconY = boxY + (BOX_H - ICON_SIZE) / 2;
        g2.drawImage(icon, iconX, iconY, ICON_SIZE, ICON_SIZE, null);

    }

    // =========================================================
    // RENDER HELPERS
    // =========================================================
    private void drawSegments(Graphics2D g2, String text, int x, int y, FontMetrics fm) {
        List<UI_TextHighlighter.Segment> segments = UI_TextHighlighter.split(text, COLOR_TEXT);
        int curX = x;
        for (UI_TextHighlighter.Segment seg : segments) {
            g2.setColor(seg.color != null ? seg.color : COLOR_TEXT);
            g2.drawString(seg.text, curX, y);
            curX += fm.stringWidth(seg.text);
        }
    }

    private List<RenderLine> buildLayout(String displayText, String fullTarget, FontMetrics fm, int maxWidth) {

        List<RenderLine> result = new ArrayList<>();
        if (fullTarget == null || fullTarget.isEmpty()) return result;

        String cleanDisplay = displayText.replaceAll("\\|$", "");
        String[] fullSegs    = fullTarget.split("\\" + LINE_SEPARATOR, -1);
        String[] displaySegs = cleanDisplay.split("\\" + LINE_SEPARATOR, -1);

        for (int s = 0; s < displaySegs.length; s++) {
            String fullSeg    = s < fullSegs.length ? fullSegs[s].trim() : "";
            String displaySeg = displaySegs[s].trim();

            List<String> wrappedFull = wrapSegment(fullSeg, fm, maxWidth);

            int cursor = 0;
            for (int i = 0; i < wrappedFull.size(); i++) {
                if (cursor >= displaySeg.length()) break;
                int lineLen = wrappedFull.get(i).length();
                int end     = Math.min(cursor + lineLen, displaySeg.length());
                result.add(new RenderLine(displaySeg.substring(cursor, end), i == 0));
                cursor += lineLen;
                if (cursor < displaySeg.length() && displaySeg.charAt(cursor) == ' ') cursor++;
            }
        }

        return result;

    }

    private List<String> wrapSegment(String text, FontMetrics fm, int maxWidth) {

        List<String> lines = new ArrayList<>();
        if (text.isEmpty()) {
            lines.add("");
            return lines;
        }

        String[] words = text.split(" ");
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

    private static class RenderLine {
        String  text;
        boolean isStar;
        RenderLine(String text, boolean isStar) {
            this.text   = text;
            this.isStar = isStar;
        }
    }

    public void reset() {
        displayedText = "";
        charIndex     = 0;
        frameCounter  = 0;
        pauseCounter  = 0;
        isDone        = false;
        lastTarget    = null;
    }

}