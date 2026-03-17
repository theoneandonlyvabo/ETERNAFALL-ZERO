package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import main.GamePanel;

public class UI_Dialog {

    private final GamePanel gp;
    private Font dialogFont;

    // CONFIG
    private static final float FONT_SIZE        = 32f;
    private static final int   BOX_W            = 856;
    private static final int   BOX_H            = 180;
    private static final int   BORDER_THICKNESS = 8;
    private static final int   PAD_X            = 25;
    private static final int   PAD_Y            = 25;
    private static final int   TEXT_INDENT      = 200;
    private static final int   STAR_MARGIN      = 10;
    private static final int   BOTTOM_OFFSET    = 80;
    private static final int   MAX_LINES        = 3;
    private static final int   LINE_SPACING     = 0;
    private static final int   TYPEWRITER_SPEED = 3;
    private static final int   PAUSE_COMMA      = 20;
    private static final int   PAUSE_PERIOD     = 30;
    private static final int   PAUSE_SEPARATOR  = 60;
    private static final String LINE_SEPARATOR  = "|";

    private static final Color COLOR_BG     = new Color(0x0a, 0x0a, 0x0a, 255);
    private static final Color COLOR_BORDER = new Color(0x4f, 0x49, 0x3b, 255);
    private static final Color COLOR_TEXT   = new Color(0xa2, 0x9f, 0x7e, 255);

    // State
    private String displayedText = "";
    private int charIndex        = 0;
    private int frameCounter     = 0;
    private int pauseCounter     = 0;
    private boolean isDone       = false;

    private String lastTarget    = null;

    // Cached layout from full target — fixed so position never shifts
    private int     cachedTotalLines = 1;
    private int     cachedTextY      = 0;

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
                    if (last == '.')      pauseCounter = PAUSE_PERIOD;
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
            // Cari | berikutnya setelah charIndex
            int nextSep = target.indexOf('|', charIndex);
            if (nextSep != -1) {
                // Skip ke tepat setelah |
                charIndex     = nextSep + 1;
                displayedText = target.substring(0, charIndex);
                pauseCounter  = 0;
                frameCounter  = 0;
            } else {
                // Nggak ada | lagi, skip ke akhir
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

        int boxX = (gp.screenWidth  - BOX_W) / 2;
        int boxY = (gp.screenHeight - BOX_H) / 2;

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

        g2.setFont(dialogFont);
        FontMetrics fm = g2.getFontMetrics();

        int textX    = boxX + TEXT_INDENT;
        int starX    = textX - STAR_MARGIN - fm.stringWidth("*");
        int maxWidth = BOX_W - TEXT_INDENT - PAD_X;
        int lineH    = fm.getHeight() + LINE_SPACING;

        String target = gp.dialogManager.getCurrentLine();
        if (target == null) return;

        int textY = boxY + PAD_Y + fm.getAscent();

        List<RenderLine> displayLayout = buildLayout(displayedText, target, fm, maxWidth);

        g2.setColor(COLOR_TEXT);
        int drawn = 0;
        for (RenderLine rl : displayLayout) {
            if (drawn >= MAX_LINES) break;
            if (rl.isStar) g2.drawString("", starX, textY + (drawn * lineH)); // Pointers
            g2.drawString(rl.text, textX, textY + (drawn * lineH));
            drawn++;
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

            // Wrap boundaries fixed from full segment
            List<String> wrappedFull = wrapSegment(fullSeg, fm, maxWidth);

            // Walk through wrapped lines, fill each with as many chars as fit
            int cursor = 0;
            for (int i = 0; i < wrappedFull.size(); i++) {
                if (cursor >= displaySeg.length()) break;
                int lineLen = wrappedFull.get(i).length();
                int end     = Math.min(cursor + lineLen, displaySeg.length());
                result.add(new RenderLine(displaySeg.substring(cursor, end), i == 0));
                cursor += lineLen;
                // Account for the space between words that wrapSegment consumed
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

    private void reset() {
        displayedText = "";
        charIndex     = 0;
        frameCounter  = 0;
        pauseCounter  = 0;
        isDone        = false;
    }

}