package indi.pancras.zk;

import org.apache.curator.test.TestingServer;

public class MockZookeeper {
    public static void main(String[] args) throws Exception {
        TestingServer server = new TestingServer(2181, true);
        server.start();
    }
}
