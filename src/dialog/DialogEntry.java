package dialog;

import java.util.List;
import java.util.Map;

public class DialogEntry {

    public String             id;
    public int                priority;
    public boolean            oneTime;
    public Map<String,String> condition;
    public List<String>       lines;
    public DialogAction       action;

    public static class DialogAction {
        public String type;   // "start_battle" | "setPath" | "markSeen"
        public String target; // enemy id | path name | dialog id
    }

}