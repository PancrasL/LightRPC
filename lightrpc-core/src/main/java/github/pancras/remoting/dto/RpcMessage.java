package github.pancras.remoting.dto;

import java.io.Serializable;

/**
 * @author PancrasL
 * <p>
 * RpcRequest和RpcResponse的包装类
 */
public class RpcMessage implements Serializable {
    /**
     * RPC消息类型，详见MessageType
     */
    private final MessageType messageType;
    /**
     * RPC消息体
     */
    private final Object data;

    private RpcMessage(MessageType messageType, Object data) {
        this.messageType = messageType;
        this.data = data;
    }

    public static RpcMessage newInstance(RpcRequest request) {
        return new RpcMessage(MessageType.RpcRequest, request);
    }

    public static RpcMessage newInstance(RpcResponse<Object> response) {
        return new RpcMessage(MessageType.RpcRequest, response);
    }

    public boolean isRequest() {
        return messageType == MessageType.RpcRequest;
    }

    public boolean isResponse() {
        return messageType == MessageType.RpcResponse;
    }

    public Object getData() {
        return data;
    }

    @Override
    public String toString() {
        return "RpcMessage{" +
                "messageType=" + messageType +
                ", data=" + data +
                '}';
    }
}
