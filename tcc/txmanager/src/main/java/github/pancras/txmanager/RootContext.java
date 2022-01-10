package github.pancras.txmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RootContext {
    private static final HashMap<String, Object> map = new HashMap<>();

    public static void setContext(String key, String value) {
        map.put(key, value);
    }

    public static String getXid() {
        return (String) map.get("xid");
    }

    public static void setBranchTransaction(BranchTransaction branch) {
        List o = (List) map.computeIfAbsent(branch.getXid(), v -> new ArrayList<BranchTransaction>());
        o.add(branch);
    }

    public static List<BranchTransaction> getBranchTransaction(String xid) {
        return (List<BranchTransaction>) map.get(xid);
    }
}
