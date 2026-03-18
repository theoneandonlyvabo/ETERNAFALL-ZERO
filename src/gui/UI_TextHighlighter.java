package gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UI_TextHighlighter {

    // -------------------------------------------------------------------------
    // KEYWORD REGISTRY — tambah di sini
    // -------------------------------------------------------------------------
    private static final Map<String, Color> KEYWORDS = new LinkedHashMap<>();

    static {
        register("Claude",          "#825337");
        register("Messmer",         "#562923");
        register("Witherfeld",      "#57562a");
        register("Withershards",    "#3b6d62");
    }

    // -------------------------------------------------------------------------
    // API
    // -------------------------------------------------------------------------
    public static void register(String keyword, String hex) {
        KEYWORDS.put(keyword, Color.decode(hex));
    }

    public static List<Segment> split(String line, Color defaultColor) {

        List<Segment> result = new ArrayList<>();
        if (line == null || line.isEmpty()) return result;

        int pos = 0;

        while (pos < line.length()) {

            int    earliest   = -1;
            String foundKey   = null;
            Color  foundColor = null;

            for (Map.Entry<String, Color> entry : KEYWORDS.entrySet()) {
                int idx = line.indexOf(entry.getKey(), pos);
                if (idx != -1 && (earliest == -1 || idx < earliest)) {
                    earliest   = idx;
                    foundKey   = entry.getKey();
                    foundColor = entry.getValue();
                }
            }

            if (earliest == -1) {
                result.add(new Segment(line.substring(pos), defaultColor));
                break;
            }

            if (earliest > pos) {
                result.add(new Segment(line.substring(pos, earliest), defaultColor));
            }

            result.add(new Segment(foundKey, foundColor));
            pos = earliest + foundKey.length();
        }

        return result;

    }

    // -------------------------------------------------------------------------
    // SEGMENT
    // -------------------------------------------------------------------------
    public static class Segment {
        public final String text;
        public final Color  color;

        public Segment(String text, Color color) {
            this.text  = text;
            this.color = color;
        }
    }

}