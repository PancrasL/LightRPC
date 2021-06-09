package github.pancras.remoting.transport;

import github.pancras.remoting.dto.RpcRequest;

/**
 * @author pancras
 * @create 2021/6/9 14:06
 */
public interface RpcRequestTransport {
    Object sendRpcRequest(RpcRequest rpcRequest);
}
