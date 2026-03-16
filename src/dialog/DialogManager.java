package dialog;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import main.GamePanel;

public class DialogManager {

    private final GamePanel gp;
    private final DialogParser parser = new DialogParser();

    private final Map<String, NpcDialogData> cache = new HashMap<>();
    private final Set<String> seenFlags = new HashSet<>();

    public boolean isActive = false;
    private List<String> currentLines;
    private int lineIndex = 0;
    private String pendingNpcId = null;
    private DialogEntry pendingEntry = null;

    public DialogManager(GamePanel gp) {
        this.gp = gp;
    }

    public void startDialog(String npcId) {

        System.out.println("startDialog called: " + npcId);

        NpcDialogData data = loadNpc(npcId);
        System.out.println("data: " + data);
        if (data == null) return;

        DialogEntry entry = resolveEntry(data.dialog);
        System.out.println("entry: " + entry);
        if (entry == null || entry.lines == null || entry.lines.isEmpty()) return;

        System.out.println("lines: " + entry.lines);

        currentLines = entry.lines;
        lineIndex    = 0;
        pendingNpcId = npcId;
        pendingEntry = entry;
        isActive     = true;

    }

    public void advance() {

        if (!isActive) return;

        lineIndex++;

        if (lineIndex >= currentLines.size()) {
            finishDialog();
        }

    }

    public String getCurrentLine() {

        if (!isActive || currentLines == null) return null;
        if (lineIndex >= currentLines.size()) return null;
        return currentLines.get(lineIndex);

    }

    private void finishDialog() {

        if (pendingEntry != null) {
            if (pendingEntry.oneTime) seenFlags.add(pendingEntry.id);
            if (pendingEntry.action != null) executeAction(pendingEntry.action);
        }

        isActive     = false;
        currentLines = null;
        lineIndex    = 0;
        pendingEntry = null;
        pendingNpcId = null;

    }

    private void executeAction(Map<String, String> action) {

        if (action.containsKey("setPath"))  gp.player.currentPath = action.get("setPath");
        if (action.containsKey("markSeen")) seenFlags.add(action.get("markSeen"));

    }

    private DialogEntry resolveEntry(List<DialogEntry> entries) {

        DialogEntry best = null;

        for (DialogEntry entry : entries) {
            if (entry.oneTime && seenFlags.contains(entry.id)) continue;
            if (!checkCondition(entry.condition)) continue;
            if (best == null || entry.priority > best.priority) best = entry;
        }

        return best;

    }

    private boolean checkCondition(Map<String, String> condition) {

        if (condition == null) return true;

        for (Map.Entry<String, String> c : condition.entrySet()) {
            switch (c.getKey()) {
                case "path": if (!c.getValue().equals(gp.player.currentPath)) return false; break;
                case "seen": if (!seenFlags.contains(c.getValue())) return false; break;
            }
        }

        return true;

    }

    private NpcDialogData loadNpc(String npcId) {

        if (cache.containsKey(npcId)) return cache.get(npcId);

        String folder = npcId.replace("npc_", "");
NpcDialogData data = parser.parse("/npc/" + folder + "/" + npcId + ".json");
        if (data != null) cache.put(npcId, data);
        return data;

    }

    public String getCurrentNpcRole() {
        if (pendingNpcId == null) return null;
        NpcDialogData data = cache.get(pendingNpcId);
        return data != null ? data.role : null;
    }

    public String getCurrentNpcName() {
        if (pendingNpcId == null) return null;
        NpcDialogData data = cache.get(pendingNpcId);
        return data != null ? data.name : null;
    }

    public void forceClose() {
        isActive     = false;
        currentLines = null;
        lineIndex    = 0;
        pendingEntry = null;
        pendingNpcId = null;
    }

    public boolean hasSeen(String dialogId) {
        return seenFlags.contains(dialogId);
    }

}