package github.pancras.transfer.service;

import org.springframework.stereotype.Service;

import github.pancras.transfer.action.FromTccAction;
import github.pancras.transfer.action.ToTccAction;
import github.pancras.txmanager.annotation.TccGlobal;

@Service
public class TransferService {
    FromTccAction tccFrom;
    ToTccAction tccTo;

    @TccGlobal
    public void doTransfer() {
        tccFrom.tccTry(null);
        tccTo.tccTry(null);
    }
}