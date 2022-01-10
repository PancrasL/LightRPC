package github.pancras.txmanager;

import java.io.Serializable;
import java.util.HashMap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TccActionContext implements Serializable {
    private String xid;
    private String branchId;
    private HashMap<String, String> map;

    public String getContext(String key) {
        return map.get(key);
    }
}
