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
    private MessageType messageType;
    /**
     * RPC消息体
     */
    private Object data;

    /**
     * 必须提供无参构造器，否则Kryo无法进行序列化操作
     */
    private RpcMessage() {

    }

    private RpcMessage(MessageType messageType, Object data) {
        this.messageType = messageType;
        this.data = data;
    }

    public static RpcMessage newRequest(RpcRequest request) {
        return new RpcMessage(MessageType.RpcRequest, request);
    }

    public static RpcMessage newResponse(RpcResponse<Object> response) {
        return new RpcMessage(MessageType.RpcResponse, response);
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
