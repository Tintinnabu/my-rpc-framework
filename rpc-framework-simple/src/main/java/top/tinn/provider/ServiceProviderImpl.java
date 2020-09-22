package top.tinn.provider;

import lombok.extern.slf4j.Slf4j;
import top.tinn.entity.RpcServiceProperties;
import top.tinn.enumeration.RpcErrorMessage;
import top.tinn.exception.RpcException;
import top.tinn.registry.zookeeper.ZkServiceRegistry;
import top.tinn.registry.ServiceRegistry;
import top.tinn.remoting.transport.netty.server.NettyServer;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Tinn
 * @Date: 2020/8/11 17:19
 */
@Slf4j
public class ServiceProviderImpl implements ServiceProvider{
    /**
     * key: rpc service name(interface name + version + group)
     * value: service object
     */
    private final Map<String, Object> serviceMap;
    private final Set<String> registeredService;
    private final ServiceRegistry serviceRegistry;

    public ServiceProviderImpl() {
        this.serviceMap = new ConcurrentHashMap<>();
        this.registeredService = ConcurrentHashMap.newKeySet();
        this.serviceRegistry = new ZkServiceRegistry();
    }

    @Override
    public void addService(Object service, Class<?> serviceClass, RpcServiceProperties rpcServiceProperties) {
        String rpcServiceName = rpcServiceProperties.toRpcServiceName();
        if (registeredService.contains(rpcServiceName)){
            return;
        }
        registeredService.add(rpcServiceName);
        serviceMap.put(rpcServiceName, service);
        log.info("Add service: {} and interfaces:{}", rpcServiceName, service.getClass().getInterfaces());
    }

    @Override
    public Object getService(RpcServiceProperties rpcServiceProperties) {
        Object service = serviceMap.get(rpcServiceProperties.toRpcServiceName());
        if (service == null){
            throw new RpcException(RpcErrorMessage.SERVICE_CAN_NOT_BE_FOUND);
        }
        return service;
    }

    @Override
    public void publishService(Object service, RpcServiceProperties rpcServiceProperties) {
        try {
            String host = InetAddress.getLocalHost().getHostAddress();
            Class<?> serviceRelatedInterface = service.getClass().getInterfaces()[0];
            String serviceName = serviceRelatedInterface.getCanonicalName();
            rpcServiceProperties.setServiceName(serviceName);
            //默认都注册在9998port上
            this.addService(service, serviceRelatedInterface, rpcServiceProperties);
            serviceRegistry.registerService(rpcServiceProperties.toRpcServiceName(),
                    new InetSocketAddress(host, NettyServer.PORT));
        }catch (UnknownHostException e){
            log.error("occur exception when getHostAddress", e);
        }
    }

    @Override
    public void publishService(Object service) {
        this.publishService(service, RpcServiceProperties.builder().group("").version("").build());
    }

    @Override
    public void publishService(Object service, RpcServiceProperties rpcServiceProperties, int port) {
        try {
            String host = InetAddress.getLocalHost().getHostAddress();
            Class<?> serviceRelatedInterface = service.getClass().getInterfaces()[0];
            String serviceName = serviceRelatedInterface.getCanonicalName();
            rpcServiceProperties.setServiceName(serviceName);
            //默认都注册在9998port上
            this.addService(service, serviceRelatedInterface, rpcServiceProperties);
            serviceRegistry.registerService(rpcServiceProperties.toRpcServiceName(),
                    new InetSocketAddress(host, port));
        }catch (UnknownHostException e){
            log.error("occur exception when getHostAddress", e);
        }
    }
}
