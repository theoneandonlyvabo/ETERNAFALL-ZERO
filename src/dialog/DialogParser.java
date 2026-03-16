package dialog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DialogParser {

    public NpcDialogData parse(String resourcePath) {

        String raw = readFile(resourcePath);
        if (raw == null) return null;

        NpcDialogData data = new NpcDialogData();
        data.id     = extractString(raw, "id");
        data.name   = extractString(raw, "name");
        data.role   = extractString(raw, "role");
        data.dialog = parseDialogArray(raw);

        return data;

    }

    private String readFile(String path) {

        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) return null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line).append("\n");
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    // Ambil value string dari key JSON flat (tidak nested)
    private String extractString(String json, String key) {

        String search = "\"" + key + "\"";
        int idx = json.indexOf(search);
        if (idx == -1) return null;

        int colon = json.indexOf(":", idx);
        if (colon == -1) return null;

        int start = json.indexOf("\"", colon + 1);
        if (start == -1) return null;

        int end = json.indexOf("\"", start + 1);
        if (end == -1) return null;

        return json.substring(start + 1, end);

    }

    private List<DialogEntry> parseDialogArray(String json) {

        List<DialogEntry> entries = new ArrayList<>();

        // Isolasi array "dialog": [ ... ]
        int arrayStart = json.indexOf("\"dialog\"");
        if (arrayStart == -1) return entries;

        int bracketOpen  = json.indexOf("[", arrayStart);
        int bracketClose = findMatchingBracket(json, bracketOpen, '[', ']');
        if (bracketOpen == -1 || bracketClose == -1) return entries;

        String arrayContent = json.substring(bracketOpen + 1, bracketClose);

        // Pecah tiap object { ... } dalam array
        List<String> blocks = splitObjects(arrayContent);
        for (String block : blocks) {
            DialogEntry entry = parseEntry(block);
            if (entry != null) entries.add(entry);
        }

        return entries;

    }

    private DialogEntry parseEntry(String block) {

        DialogEntry entry = new DialogEntry();

        entry.id       = extractString(block, "id");
        entry.priority = extractInt(block, "priority");
        entry.oneTime  = extractBoolean(block, "one_time");
        entry.lines    = extractStringArray(block, "lines");
        entry.condition = extractStringMap(block, "condition");
        entry.action    = extractStringMap(block, "action");

        if (entry.id == null) return null;
        return entry;

    }

    private int extractInt(String json, String key) {

        String search = "\"" + key + "\"";
        int idx = json.indexOf(search);
        if (idx == -1) return 0;

        int colon = json.indexOf(":", idx);
        if (colon == -1) return 0;

        int start = colon + 1;
        while (start < json.length() && Character.isWhitespace(json.charAt(start))) start++;

        int end = start;
        while (end < json.length() && (Character.isDigit(json.charAt(end)) || json.charAt(end) == '-')) end++;

        try {
            return Integer.parseInt(json.substring(start, end).trim());
        } catch (NumberFormatException e) {
            return 0;
        }

    }

    private boolean extractBoolean(String json, String key) {

        String search = "\"" + key + "\"";
        int idx = json.indexOf(search);
        if (idx == -1) return false;

        int colon = json.indexOf(":", idx);
        if (colon == -1) return false;

        String after = json.substring(colon + 1).trim();
        return after.startsWith("true");

    }

    private List<String> extractStringArray(String json, String key) {

        List<String> result = new ArrayList<>();

        String search = "\"" + key + "\"";
        int idx = json.indexOf(search);
        if (idx == -1) return result;

        int bracketOpen  = json.indexOf("[", idx);
        int bracketClose = findMatchingBracket(json, bracketOpen, '[', ']');
        if (bracketOpen == -1 || bracketClose == -1) return result;

        String content = json.substring(bracketOpen + 1, bracketClose);

        // Parse tiap string dalam array
        int pos = 0;
        while (pos < content.length()) {
            int q1 = content.indexOf("\"", pos);
            if (q1 == -1) break;
            int q2 = content.indexOf("\"", q1 + 1);
            if (q2 == -1) break;
            result.add(content.substring(q1 + 1, q2));
            pos = q2 + 1;
        }

        return result;

    }

    // Parse object { "key": "value", ... } atau return null kalau "null"
    private Map<String, String> extractStringMap(String json, String key) {

        String search = "\"" + key + "\"";
        int idx = json.indexOf(search);
        if (idx == -1) return null;

        int colon = json.indexOf(":", idx);
        if (colon == -1) return null;

        String after = json.substring(colon + 1).trim();
        if (after.startsWith("null")) return null;

        int braceOpen  = json.indexOf("{", colon);
        int braceClose = findMatchingBracket(json, braceOpen, '{', '}');
        if (braceOpen == -1 || braceClose == -1) return null;

        String content = json.substring(braceOpen + 1, braceClose);
        Map<String, String> map = new HashMap<>();

        int pos = 0;
        
        while (pos < content.length()) {
            int k1 = content.indexOf("\"", pos);
            if (k1 == -1) break;
            int k2 = content.indexOf("\"", k1 + 1);
            if (k2 == -1) break;
            String mapKey = content.substring(k1 + 1, k2);

            int c = content.indexOf(":", k2);
            if (c == -1) break;

            int v1 = content.indexOf("\"", c + 1);
            if (v1 == -1) break;
            int v2 = content.indexOf("\"", v1 + 1);
            if (v2 == -1) break;
            String mapVal = content.substring(v1 + 1, v2);

            map.put(mapKey, mapVal);
            pos = v2 + 1;
        }

        return map;

    }

    // Cari posisi closing bracket yang matching
    private int findMatchingBracket(String json, int openPos, char open, char close) {

        if (openPos == -1 || openPos >= json.length()) return -1;

        int depth = 0;
        for (int i = openPos; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == open)  depth++;
            if (c == close) depth--;
            if (depth == 0) return i;
        }

        return -1;

    }

    // Pecah string JSON array jadi list of object strings { ... }
    private List<String> splitObjects(String content) {

        List<String> result = new ArrayList<>();
        int i = 0;

        while (i < content.length()) {
            int start = content.indexOf("{", i);
            if (start == -1) break;
            int end = findMatchingBracket(content, start, '{', '}');
            if (end == -1) break;
            result.add(content.substring(start, end + 1));
            i = end + 1;
        }

        return result;

    }

}