package github.pancras.commons.exception;

public class RpcException extends RuntimeException {
    public RpcException(String detail) {
        super(detail);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

}