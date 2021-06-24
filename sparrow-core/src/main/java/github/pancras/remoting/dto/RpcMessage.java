package github.pancras.remoting.dto;

import java.io.Serializable;

/**
 * @author pancras
 * @create 2021/6/16 21:00
 * <p>
 * RpcRequest和RpcResponse的包装类
 */
public class RpcMessage implements Serializable {
    // RPC 消息类型，详见RpcConstants
    private byte messageType;
    // RPC 消息体（RpcRequest 或 RpcResponse）
    private Object data;

    public byte getMessageType() {
        return messageType;
    }

    public void setMessageType(byte messageType) {
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
