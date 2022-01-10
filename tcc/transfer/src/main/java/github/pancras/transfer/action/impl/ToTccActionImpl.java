package github.pancras.transfer.action.impl;

import org.springframework.stereotype.Component;

import github.pancras.transfer.action.ToTccAction;
import github.pancras.txmanager.annotation.TccTry;
import github.pancras.txmanager.dto.TccActionContext;

/**
 * 加钱参与者实现
 */
@Component
public class ToTccActionImpl implements ToTccAction {
    @TccTry
    @Override
    public boolean tccTry(TccActionContext context) {
        System.out.println("准备收款，收款人：转入冻结资金成功");
        return true;
    }

    @Override
    public boolean commit(TccActionContext context) {
        System.out.println("收款成功，收款人：冻结资金已解冻");
        return true;
    }

    @Override
    public boolean rollback(TccActionContext context) {
        System.out.println("首款失败，收款人：冻结资金已退还");
        return true;
    }
}
