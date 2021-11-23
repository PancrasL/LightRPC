package github.pancras.remoting.dto;

/**
 * 用于标识RpcMessage中的消息类型
 *
 * @author PancrasL
 */
//仅被RpcMessage使用，故声明为包级私有的
enum MessageType {
    /**
     * Rpc request type.
     */
    RpcRequest,

    /**
     * Rpc response type.
     */
    RpcResponse
}
