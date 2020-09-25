package top.tinn.proxy.JDKDynamic;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import top.tinn.entity.RpcServiceProperties;
import top.tinn.remoting.dto.RpcMessageChecker;
import top.tinn.remoting.dto.RpcRequest;
import top.tinn.remoting.dto.RpcResponse;
import top.tinn.remoting.transport.netty.client.ClientTransport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @Author: Tinn
 * @Date: 2020/8/12 11:41
 */
@Slf4j
public class RpcClientProxy implements InvocationHandler {
    /**
     * Used to send requests to the server.And there are two implementations: socket and netty
     */
    private final ClientTransport clientTransport;
    private final RpcServiceProperties rpcServiceProperties;

    public RpcClientProxy(ClientTransport clientTransport, RpcServiceProperties rpcServiceProperties) {
        this.clientTransport = clientTransport;
        if (rpcServiceProperties.getGroup() == null){
            rpcServiceProperties.setGroup("");
        }
        if (rpcServiceProperties.getVersion() == null){
            rpcServiceProperties.setVersion("");
        }
        this.rpcServiceProperties = rpcServiceProperties;
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz){
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

     @SneakyThrows
    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(Object proxy, Method method, Object[] args){
        log.info("invoked method: [{}]", method.getName());
        RpcRequest rpcRequest = RpcRequest.builder()
                .methodName(method.getName())
                .parameters(args)
                .interfaceName(method.getDeclaringClass().getName())
                .paramTypes(method.getParameterTypes())
                .requestId(UUID.randomUUID().toString())
                .group(rpcServiceProperties.getGroup())
                .version(rpcServiceProperties.getVersion())
                .build();
        RpcResponse<Object> rpcResponse = null;
        //发送请求
        CompletableFuture<RpcResponse<Object>> completableFuture =
                (CompletableFuture<RpcResponse<Object>>) clientTransport.sendRpcRequest(rpcRequest);
        //阻塞等待响应
        rpcResponse = completableFuture.get();
        RpcMessageChecker.check(rpcResponse, rpcRequest);
        return rpcResponse.getData();
    }
}
