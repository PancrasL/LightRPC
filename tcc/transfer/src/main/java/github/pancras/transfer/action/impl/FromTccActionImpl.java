package github.pancras.transfer.action.impl;

import github.pancras.transfer.action.FromTccAction;
import github.pancras.txmanager.TccActionContext;
import github.pancras.txmanager.annotation.TccTry;

/**
 * 扣钱参与者实现
 */
public class FromTccActionImpl implements FromTccAction {
    @TccTry
    @Override
    public boolean tccTry(TccActionContext businessActionContext) {
        System.out.println("准备付款，付款人：金额冻结成功");
        return true;
    }

    @Override
    public boolean commit(TccActionContext context) {
        System.out.println("付款成功，付款人：冻结资金已扣除");
        return true;
    }

    @Override
    public boolean rollback(TccActionContext context) {
        System.out.println("付款失败，付款人：冻结资金已解冻");
        return true;
    }
}
