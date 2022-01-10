package github.pancras.transfer.action;

import github.pancras.txmanager.annotation.TccTry;
import github.pancras.txmanager.dto.TccActionContext;

/**
 * 转入人：收钱
 */
public interface ToTccAction {
    @TccTry
    boolean tccTry(TccActionContext context);

    boolean commit(TccActionContext context);

    boolean rollback(TccActionContext context);
}
