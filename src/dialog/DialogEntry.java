package dialog;

import java.util.List;
import java.util.Map;

public class DialogEntry {

    public String id;
    public int priority;
    public boolean oneTime;
    public Map<String, String> condition; // key: "path" / "seen", value: expected value
    public List<String> lines;
    public Map<String, String> action;   // key: "setPath" / "markSeen", value: target

}