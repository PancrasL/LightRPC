package github.pancras.txmanager;

import com.alibaba.fastjson.JSONObject;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import github.pancras.txmanager.dto.BranchTx;
import github.pancras.txmanager.store.TxStore;
import github.pancras.txmanager.store.ZkTxStore;

public enum ResourceManager {
    /**
     * 单例
     */
    INSTANCE;
    private final TxStore txStore = ZkTxStore.INSTANCE;
    private final String address = "127.0.0.1:8010";
    private HashMap<String, Object> resources;
    /**
     * socket server
     */
    private ServerSocket server;
    private ExecutorService threadPool;

    ResourceManager() {
        try {
            server = new ServerSocket();
            server.bind(new InetSocketAddress("127.0.0.1", 8010));
            this.threadPool = Executors.newCachedThreadPool();
            Socket socket;
            while ((socket = server.accept()) != null) {
                Socket finalSocket = socket;
                threadPool.execute(() -> {
                    try (ObjectInputStream in = new ObjectInputStream(finalSocket.getInputStream());
                         ObjectOutputStream out = new ObjectOutputStream(finalSocket.getOutputStream())) {
                        JSONObject jsonObj = (JSONObject) in.readObject();
                        out.writeObject(handle(jsonObj));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeBranchTx(BranchTx branchTx) {
        txStore.writeBranchTx(branchTx);
    }

    public void registResource(Object obj) {
        resources.put(obj.getClass().getCanonicalName(), obj);
    }

    public String getAddress() {
        return address;
    }

    private JSONObject handle(JSONObject jsonObject) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        String command = (String) jsonObject.get("command");
        switch (command) {
            case "commit":
                doCommit(jsonObject.getObject("branchTx", BranchTx.class));
                break;
            case "rollback":
                doRollback(jsonObject.getObject("branchTx", BranchTx.class));
                break;
            default:
                break;
        }
        return null;
    }

    private void doCommit(BranchTx branchTx) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object r = resources.get(branchTx.getResourceId());
        Method method = r.getClass().getMethod(branchTx.getCommitMethod());
        method.invoke(r);
    }

    private void doRollback(BranchTx branchTx) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object r = resources.get(branchTx.getResourceId());
        Method method = r.getClass().getMethod(branchTx.getRollbackMethod());
        method.invoke(r);
    }
}
