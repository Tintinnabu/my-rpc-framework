package top.tinn.remoting.transport.netty.client;

import top.tinn.remoting.dto.RpcResponse;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务器未处理请求
 *
 * @Author: Tinn
 * @Date: 2020/8/14 10:08
 */
public class UnprocessedRequests {
    private static final Map<String, CompletableFuture<RpcResponse<Object>>> UNPROCESSED_RESPONSE_FUTURES
            = new ConcurrentHashMap<>();

    public void put(String requestId, CompletableFuture<RpcResponse<Object>> future){
        UNPROCESSED_RESPONSE_FUTURES.put(requestId, future);
    }

    public void complete(RpcResponse<Object> rpcResponse){
        CompletableFuture<RpcResponse<Object>> future = UNPROCESSED_RESPONSE_FUTURES.remove(rpcResponse.getRequestId());
        if (future != null){
            //开始处理RpcResponse，同步阻塞
            future.complete(rpcResponse);
        } else {
            throw new IllegalStateException();
        }
    }
}
