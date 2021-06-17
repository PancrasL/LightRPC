package github.pancras.registry.zk.util;

import org.apache.curator.framework.CuratorFramework;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author pancras
 * @create 2021/6/10 15:33
 */
public class CuratorUtilsTest {
    CuratorFramework zkClient;
    boolean zkServerStarted = false;

    @Before
    public void setUp() {
        if (!zkServerStarted)
            return;
        zkClient = CuratorUtils.getZkClient();
    }

    @Test
    public void testCreateAndGet() {
        if (!zkServerStarted)
            return;
        // 测试创建
        CuratorUtils.createPersistentNode(zkClient, "/test/test1");
        CuratorUtils.createPersistentNode(zkClient, "/test/test2");

        // 测试添加
        List<String> nodes = CuratorUtils.getChildrenNodes(zkClient, "/test");
        assertNotNull(nodes);
        assertEquals("test1", nodes.get(1));
        assertEquals("test2", nodes.get(0));

        // 测试删除
        CuratorUtils.deleteNode(zkClient, "/test");
    }
}