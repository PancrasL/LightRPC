package github.pancras.discover.loadbalance.loadbalancer;

import java.util.ArrayList;
import java.util.List;

class LoadBalancerUtil {
    public static List<String> processWeight(List<String> rawAddresses) {
        List<String> newAddressList = new ArrayList<>(rawAddresses.size());
        for (String weightAddress : rawAddresses) {
            String[] addressAndWeight = weightAddress.split("@");
            int cnt = Integer.parseInt(addressAndWeight[1]);
            for (int i = 0; i < cnt; i++) {
                newAddressList.add(addressAndWeight[0]);
            }
        }
        return newAddressList;
    }

    public static List<String> removeWeight(List<String> rawAddresses) {
        List<String> newAddressList = new ArrayList<>(rawAddresses.size());
        for (String weightAddress : rawAddresses) {
            String[] addressAndWeight = weightAddress.split("@");
            newAddressList.add(addressAndWeight[0]);
        }
        return newAddressList;
    }
}
