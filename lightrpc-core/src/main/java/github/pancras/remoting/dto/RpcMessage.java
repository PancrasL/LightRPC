package github.pancras.remoting.dto;

import java.io.Serializable;

import github.pancras.commons.enums.MessageType;

/**
 * @author PancrasL
 * <p>
 * RpcRequest和RpcResponse的包装类
 */
public class RpcMessage implements Serializable {
    /**
     * RPC 消息类型，详见RpcConstants
     */
    private MessageType messageType;
    private Object data;

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RpcMessage{" +
                "messageType=" + messageType +
                ", data=" + data +
                '}';
    }
}
