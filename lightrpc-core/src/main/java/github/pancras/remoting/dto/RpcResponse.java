package github.pancras.remoting.dto;

import java.io.Serializable;

/**
 * @author PancrasL
 * <p>
 * RPC 响应对象
 */
public class RpcResponse<T> implements Serializable {
    private static final Integer SUCCESS_FLAG = 1;
    private static final Integer FAIL_FLAG = -1;

    private String requestId;
    private Integer status;
    private String message;
    private T data;

    public static <T> RpcResponse<T> success(T data, String requestId) {
        RpcResponse<T> response = new RpcResponse<>();
        response.setRequestId(requestId);
        response.setStatus(SUCCESS_FLAG);
        response.setMessage("sucess");
        response.setData(data);
        return response;
    }

    public static <T> RpcResponse<T> fail() {
        RpcResponse<T> response = new RpcResponse<>();
        response.setStatus(FAIL_FLAG);
        response.setMessage("fail");
        return response;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer code) {
        this.status = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RpcResponse{" +
                "requestId='" + requestId + '\'' +
                ", code=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
