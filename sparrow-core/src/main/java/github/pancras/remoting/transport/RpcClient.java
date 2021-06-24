package github.pancras.remoting.transport;

import github.pancras.remoting.dto.RpcRequest;

/**
 * @author pancras
 * @create 2021/6/17 10:00
 */
public interface RpcClient {
    Object sendRpcRequest(RpcRequest rpcRequest) throws Exception;
}
