package dialog;

import entity.Messmer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import main.GamePanel;

public class DialogManager {

    // CONFIG
    private static final int SFX_COOLDOWN = 1; // frame minimum antar sfx

    private final GamePanel gp;
    private final DialogParser parser = new DialogParser();

    private final Map<String, NpcDialogData> cache     = new HashMap<>();
    private final Set<String>                seenFlags = new HashSet<>();

    public boolean       isActive     = false;
    private List<String> currentLines;
    private int          lineIndex    = 0;
    private String       pendingNpcId = null;
    private DialogEntry  pendingEntry = null;

    private int sfxCooldownCounter = 0;

    public DialogManager(GamePanel gp) {
        this.gp = gp;
    }

    public void startDialog(String npcId) {

        NpcDialogData data = loadNpc(npcId);
        if (data == null) return;

        DialogEntry entry = resolveEntry(data.dialog);
        if (entry == null || entry.lines == null || entry.lines.isEmpty()) return;

        currentLines       = entry.lines;
        lineIndex          = 0;
        pendingNpcId       = npcId;
        pendingEntry       = entry;
        isActive           = true;
        sfxCooldownCounter = 0;

    }

    public void advance() {

        if (!isActive) return;
        lineIndex++;
        if (lineIndex >= currentLines.size()) finishDialog();

    }

    public String getCurrentLine() {

        if (!isActive || currentLines == null) return null;
        if (lineIndex >= currentLines.size()) return null;
        return currentLines.get(lineIndex);

    }

    // =========================================================
    // SFX — dipanggil UI_Dialog tiap karakter muncul
    // =========================================================
    public void onCharRevealed() {
        if (sfxCooldownCounter > 0) {
            sfxCooldownCounter--;
            return;
        }
        if (pendingNpcId == null) return;
        String folder = pendingNpcId.replace("npc_", "");
        gp.sfx.playSfx("/npc/" + folder + "/sfx_" + folder + ".wav");
        sfxCooldownCounter = SFX_COOLDOWN;
    }

    // =========================================================

    private void finishDialog() {

        if (pendingEntry != null) {
            if (pendingEntry.oneTime) seenFlags.add(pendingEntry.id);
            if (pendingEntry.action  != null) executeAction(pendingEntry.action);
        }

        isActive           = false;
        currentLines       = null;
        lineIndex          = 0;
        pendingEntry       = null;
        pendingNpcId       = null;
        sfxCooldownCounter = 0;

    }

    private void executeAction(DialogEntry.DialogAction action) {

        switch (action.type) {
            case "setPath"      -> gp.player.currentPath = action.target;
            case "markSeen"     -> seenFlags.add(action.target);
            case "start_battle" -> startBattle(action.target);
        }

    }

    private void startBattle(String target) {

        switch (target) {
            case "messmer" -> gp.battleManager.startBattle(Messmer.DATA);
            // tambah enemy baru di sini
        }

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
                case "path" -> { if (!c.getValue().equals(gp.player.currentPath)) return false; }
                case "seen" -> { if (!seenFlags.contains(c.getValue())) return false; }
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

    public String getCurrentNpcId() { return pendingNpcId; }

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
        isActive           = false;
        currentLines       = null;
        lineIndex          = 0;
        pendingEntry       = null;
        pendingNpcId       = null;
        sfxCooldownCounter = 0;
    }

    public boolean hasSeen(String dialogId) {
        return seenFlags.contains(dialogId);
    }

}