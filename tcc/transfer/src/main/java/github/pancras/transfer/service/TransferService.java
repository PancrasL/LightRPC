package github.pancras.transfer.service;

import org.springframework.stereotype.Service;

import github.pancras.spring.annotation.RpcReference;
import github.pancras.transfer.action.FromTccAction;
import github.pancras.transfer.action.ToTccAction;
import github.pancras.txmanager.annotation.TccGlobal;
import github.pancras.txmanager.dto.TccActionContext;

@Service
public class TransferService {
    @RpcReference
    FromTccAction tccFrom;
    @RpcReference
    ToTccAction tccTo;

    @TccGlobal
    public void doTransfer(TccActionContext context) {
        if (!tccFrom.tccTry(context)) {
            throw new RuntimeException("扣款失败");
        }
        tccTo.tccTry(context);
    }
}