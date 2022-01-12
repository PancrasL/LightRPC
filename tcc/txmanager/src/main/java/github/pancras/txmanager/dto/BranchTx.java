package github.pancras.txmanager.dto;

import java.io.Serializable;

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
public class BranchTx implements Serializable {
    private String xid;
    private String branchId;
    private String commitMethod;
    private String rollbackMethod;
    private String resourceId;
    private String resourceAddress;
}
