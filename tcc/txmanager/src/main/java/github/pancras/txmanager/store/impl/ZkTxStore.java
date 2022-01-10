package github.pancras.txmanager.store.impl;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryOneTime;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

import github.pancras.txmanager.dto.BranchTx;
import github.pancras.txmanager.store.TxStore;
import lombok.extern.slf4j.Slf4j;

/**
 * 事务存储管理器
 */
@Slf4j
public enum ZkTxStore implements TxStore {
    /**
     * 单例实现
     */
    INSTANCE;
    private static final String PREFIX = "/LightTCC/global/";
    private final CuratorFramework zkClient;

    ZkTxStore() {
        zkClient = CuratorFrameworkFactory.newClient("localhost:2181", new RetryOneTime(1));
        zkClient.start();
        try {
            zkClient.blockUntilConnected(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void writeXid(String xid) {
        String path = PREFIX + xid;
        createPath(path);
    }

    @Override
    public void writeBranchTx(BranchTx branchTx) {
        String xid = branchTx.getXid();
        if (!exist(PREFIX + xid)) {
            throw new IllegalStateException("Xid not exist:" + branchTx.getXid());
        }
        String branchPath = PREFIX + xid + "/" + branchTx.getBranchId();
        byte[] data = serialize(branchTx);
        createPathWithData(branchPath, data);
    }

    @Override
    public List<BranchTx> readBranches(String xid) {
        String path = PREFIX + xid;
        List<String> childs = getChild(path);
        List<BranchTx> branchTxes = new ArrayList<>();
        for (String child : childs) {
            byte[] data = getData(path + "/" + child);
            BranchTx branchTx = deserialize(data, BranchTx.class);
            branchTxes.add(branchTx);
        }
        return branchTxes;
    }

    @Override
    public void deleteXid(String xid) {
        String path = PREFIX + xid;
        try {
            zkClient.delete().deletingChildrenIfNeeded().forPath(path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void createPath(String path) {
        try {
            zkClient.create().creatingParentsIfNeeded().forPath(path);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        log.info("创建ZNode：" + path);
    }

    private void createPathWithData(String path, byte[] data) {
        try {
            zkClient.create().forPath(path, data);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private byte[] getData(String path) {
        try {
            return zkClient.getData().forPath(path);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private List<String> getChild(String path) {
        try {
            return zkClient.getChildren().forPath(path);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private boolean exist(String path) {
        try {
            return zkClient.checkExists().forPath(path) != null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private byte[] serialize(@Nonnull Object obj) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            return bos.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private <T> T deserialize(@Nonnull byte[] bytes, @Nonnull Class<T> clazz) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            Object obj = ois.readObject();
            return (T) obj;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
