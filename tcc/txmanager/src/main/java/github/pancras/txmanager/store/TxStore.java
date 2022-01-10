package github.pancras.txmanager.store;

import java.util.List;

import github.pancras.txmanager.aspect.TccGlobalAspect;
import github.pancras.txmanager.aspect.TccTryAspect;
import github.pancras.txmanager.dto.BranchTx;

public interface TxStore {
    /**
     * 被{@link TccGlobalAspect}调用，用来保存xid
     */
    void writeXid(String xid);

    /**
     * 被{@link TccTryAspect}调用，用来注册分支事务信息
     */
    void writeBranchTx(BranchTx branchTx);

    /**
     * 被{@link TccGlobalAspect}调用，用来通过xid获取全局事务的分支信息
     */
    List<BranchTx> readBranches(String xid);

    /**
     * 删除xid下的分支事务
     */
    void deleteXid(String xid);
}
