package github.pancras.commons.enums;

/**
 * @author PancrasL
 */

public enum MessageType {
    /**
     * Zookeeper registry type.
     */
    RpcRequest,

    /**
     * Redis registry type.
     */
    RpcResponse;

    public static MessageType getType(String name) {
        for (MessageType messageType : MessageType.values()) {
            if (messageType.name().equalsIgnoreCase(name)) {
                return messageType;
            }
        }
        throw new IllegalArgumentException("Not support message type: " + name);
    }
}
