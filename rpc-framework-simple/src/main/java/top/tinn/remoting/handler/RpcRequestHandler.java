package top.tinn.remoting.handler;

import lombok.extern.slf4j.Slf4j;
import top.tinn.exception.RpcException;
import top.tinn.factory.SingletonFactory;
import top.tinn.provider.ServiceProvider;
import top.tinn.provider.ServiceProviderImpl;
import top.tinn.remoting.dto.RpcRequest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * RpcRequest processor
 *
 * @Author: Tinn
 * @Date: 2020/8/12 14:22
 */
@Slf4j
public class RpcRequestHandler {
    private final ServiceProvider serviceProvider;

    public RpcRequestHandler() {
        serviceProvider = SingletonFactory.getInstance(ServiceProviderImpl.class);
    }

    /**
     * Processing rpcRequest: call the corresponding method, and then return the method
     */
    public Object handle(RpcRequest rpcRequest){
        Object service = serviceProvider.getService(rpcRequest.toRpcProperties());
        return invokeTargetMethod(rpcRequest, service);
    }

    /**
     *
     * get method execution results
     *
     * @param rpcRequest
     * @param service
     * @return
     */
    private Object invokeTargetMethod(RpcRequest rpcRequest, Object service) {
        Object result;
        try {
            Method method = service.getClass().getMethod(
                    rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            result = method.invoke(service, rpcRequest.getParameters());
            log.info("service:[{}] successful invoke method:[{}]", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RpcException(e.getMessage(), e);
        }
        return result;
    }
}
