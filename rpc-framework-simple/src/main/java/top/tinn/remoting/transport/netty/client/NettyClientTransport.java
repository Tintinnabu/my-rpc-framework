package top.tinn.remoting.transport.netty.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;
import top.tinn.factory.SingletonFactory;
import top.tinn.registry.ServiceDiscovery;
import top.tinn.registry.zookeeper.ZkServiceDiscovery;
import top.tinn.remoting.dto.RpcRequest;
import top.tinn.remoting.dto.RpcResponse;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

/**
 * @Author: Tinn
 * @Date: 2020/8/14 10:07
 */
@Slf4j
public class NettyClientTransport implements ClientTransport {
    private final ServiceDiscovery serviceDiscovery;
    private final UnprocessedRequests unprocessedRequests;
    private final ChannelProvider channelProvider;

    public NettyClientTransport() {
        serviceDiscovery = new ZkServiceDiscovery();
        unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
        channelProvider = SingletonFactory.getInstance(ChannelProvider.class);
    }

    @Override
    public CompletableFuture<RpcResponse<Object>> sendRpcRequest(RpcRequest rpcRequest) {
        CompletableFuture<RpcResponse<Object>> resultFuture = new CompletableFuture<>();
        String rpcServiceName = rpcRequest.toRpcProperties().toRpcServiceName();
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcServiceName);
        Channel channel = channelProvider.get(inetSocketAddress);
        if (channel != null && channel.isActive()){
            //提前放入Map容器中
            unprocessedRequests.put(rpcRequest.getRequestId(), resultFuture);
            //刷入channel，由channelHandler进行处理
            channel.writeAndFlush(rpcRequest).addListener((ChannelFutureListener) future -> {
                //发送成功
                if (future.isSuccess()){
                    log.info("client send message: [{}]", rpcRequest);
                }else {
                    future.channel().close();
                    resultFuture.completeExceptionally(future.cause());
                    log.error("Send failed:", future.cause());
                }
            });
        } else {
            throw new IllegalStateException();
        }
        return resultFuture;
    }
}
