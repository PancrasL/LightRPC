package github.pancras.remoting.transport.netty.client;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import github.pancras.remoting.dto.RpcResponse;

/**
 * @author PancrasL
 */
public class UnprocessedRequests {
    private static final Map<String, CompletableFuture<RpcResponse<?>>> UNPROCESSED_RESPONSE_FUTURES = new ConcurrentHashMap<>();

    /**
     * requestId请求的结果执行完毕后，存放在result中
     *
     * @param requestId the requestId
     * @param result    the result container
     */
    public void put(String requestId, CompletableFuture<RpcResponse<?>> result) {
        UNPROCESSED_RESPONSE_FUTURES.put(requestId, result);
    }

    public void complete(RpcResponse<?> rpcResponse) {
        CompletableFuture<RpcResponse<?>> future = UNPROCESSED_RESPONSE_FUTURES.remove(rpcResponse.getRequestId());
        future.complete(rpcResponse);
    }
}
