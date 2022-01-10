package github.pancras.transfer.action;

import github.pancras.txmanager.TccActionContext;
import github.pancras.txmanager.annotation.TccTry;

/**
 * 转出人：扣钱
 */
public interface FromTccAction {
    @TccTry
    boolean tccTry(TccActionContext context);

    boolean commit(TccActionContext context);

    boolean rollback(TccActionContext context);
}
