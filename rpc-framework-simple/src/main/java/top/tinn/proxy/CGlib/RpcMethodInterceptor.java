package top.tinn.proxy.CGlib;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import top.tinn.entity.RpcServiceProperties;
import top.tinn.remoting.dto.RpcMessageChecker;
import top.tinn.remoting.dto.RpcRequest;
import top.tinn.remoting.dto.RpcResponse;
import top.tinn.remoting.transport.netty.client.ClientTransport;

import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @Author: Tinn
 * @Date: 2020/9/3 20:17
 */
@Slf4j
public class RpcMethodInterceptor implements MethodInterceptor {

    /**
     * Used to send requests to the server.And there are two implementations: socket and netty
     */
    private final ClientTransport clientTransport;
    private final RpcServiceProperties rpcServiceProperties;

    public RpcMethodInterceptor(ClientTransport clientTransport, RpcServiceProperties rpcServiceProperties) {
        this.clientTransport = clientTransport;
        if (rpcServiceProperties.getGroup() == null){
            rpcServiceProperties.setGroup("");
        }
        if (rpcServiceProperties.getVersion() == null){
            rpcServiceProperties.setVersion("");
        }
        this.rpcServiceProperties = rpcServiceProperties;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        //对目标方法进行增强
        log.info("invoked method: [{}]", method.getName());
        RpcRequest rpcRequest = RpcRequest.builder()
                .methodName(method.getName())
                .parameters(objects)
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
