package github.pancras.remoting.transport;

import github.pancras.commons.Disposable;
import github.pancras.remoting.dto.RpcRequest;

/**
 * @author PancrasL
 */
public interface RpcClient extends Disposable {
    /**
     * 向服务器发送RPC请求
     *
     * @param rpcRequest 请求对象
     * @return 响应对象
     * @throws Exception 异常
     */
    Object sendRpcRequest(RpcRequest rpcRequest) throws Exception;
}
