package github.pancras.tcc;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.springframework.stereotype.Component;

import github.pancras.tcc.annotation.LocalTcc;

@Component
public class ZkTccHandler {
    private static final String ZK_TCC_ROOT_PATH = "/tcc/";
    private CuratorFramework zkClient;

    public void registMethod(String uuid, String className) {
        LocalTcc localTcc = null;
        try {
            localTcc = Class.forName(className).getAnnotation(LocalTcc.class);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String confirmMethodName = localTcc.confirm();
        String cancelMethodName = localTcc.cancel();
        createEphemeralNode(ZK_TCC_ROOT_PATH + uuid + "/confirm/" + confirmMethodName);
        createEphemeralNode(ZK_TCC_ROOT_PATH + uuid + "/cancel/" + cancelMethodName);
    }

    public void invokeForRpc(String uuid, String className, TccStatus code) {
        if (code == TccStatus.SUCCESS) {
            LocalTcc localTcc = null;
            try {
                localTcc = Class.forName(className).getAnnotation(LocalTcc.class);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            String confirmMethodName = localTcc.confirm();
            String cancelMethodName = localTcc.cancel();
            createEphemeralNode(ZK_TCC_ROOT_PATH + uuid + "/confirm/" + confirmMethodName);
            createEphemeralNode(ZK_TCC_ROOT_PATH + uuid + "/cancel/" + cancelMethodName);
        } else if (code == TccStatus.FAIL) {
            // rollback();
        }
    }

    private void createEphemeralNode(String path) {
        try {
            zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
