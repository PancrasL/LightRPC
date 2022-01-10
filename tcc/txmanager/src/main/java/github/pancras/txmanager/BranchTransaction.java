package github.pancras.txmanager;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BranchTransaction {
    private String xid;
    private String branchId;
    private String commitMethod;
    private String rollbackMethod;
}
