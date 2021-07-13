package github.pancras.remoting.transport;

import java.io.Closeable;

import github.pancras.remoting.dto.RpcRequest;

/**
 * @author pancras
 * @create 2021/6/17 10:00
 */
public interface RpcClient extends Closeable {
    /**
     * 向服务器发送RPC请求
     *
     * @param rpcRequest 请求对象
     * @return 响应对象
     * @throws Exception 异常
     */
    Object sendRpcRequest(RpcRequest rpcRequest) throws Exception;

    /**
     * 屏蔽掉异常
     */
    @Override
    void close();
}
