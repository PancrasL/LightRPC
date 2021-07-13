package github.pancras.remoting.dto;

import java.io.Serializable;

import github.pancras.remoting.constants.RpcConstants;

/**
 * @author pancras
 * @create 2021/6/5 19:40
 * <p>
 * RPC 响应对象
 */
public class RpcResponse<T> implements Serializable {

    private String requestId;
    private Integer code;
    private String message;
    private T data;

    public static <T> RpcResponse<T> success(T data, String requestId) {
        RpcResponse<T> response = new RpcResponse<>();
        response.setRequestId(requestId);
        response.setCode(RpcConstants.SUCCESS_FLAG);
        response.setMessage(RpcConstants.SUCESS);
        response.setData(data);
        return response;
    }

    public static <T> RpcResponse<T> fail() {
        RpcResponse<T> response = new RpcResponse<>();
        response.setCode(RpcConstants.FAIL_FLAG);
        response.setMessage(RpcConstants.FAIL);
        return response;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
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
                ", code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
