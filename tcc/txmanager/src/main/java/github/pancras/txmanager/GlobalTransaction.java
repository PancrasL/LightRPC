package github.pancras.txmanager;

import java.util.ArrayList;
import java.util.List;

public class GlobalTransaction {
    private final List<BranchTransaction> branchList = new ArrayList<>();

    public void addBranch(BranchTransaction branch) {
        branchList.add(branch);
    }

    public List<BranchTransaction> getBranchList() {
        return branchList;
    }
}
