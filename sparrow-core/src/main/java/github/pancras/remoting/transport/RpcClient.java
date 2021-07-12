package github.pancras.remoting.transport;

import java.io.Closeable;

import github.pancras.remoting.dto.RpcRequest;

/**
 * @author pancras
 * @create 2021/6/17 10:00
 */
public interface RpcClient extends Closeable {
    Object sendRpcRequest(RpcRequest rpcRequest) throws Exception;

    @Override
    void close();
}
