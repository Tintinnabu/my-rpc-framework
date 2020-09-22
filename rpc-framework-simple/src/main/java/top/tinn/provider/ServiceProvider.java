package top.tinn.provider;

import top.tinn.entity.RpcServiceProperties;

/**
 * store and provide service object.
 * @Author: Tinn
 * @Date: 2020/8/11 17:15
 */
public interface ServiceProvider {
    /**
     * @param service              service object
     * @param serviceClass         the interface class implemented by the service instance object
     * @param rpcServiceProperties service related attributes
     */
    void addService(Object service, Class<?> serviceClass, RpcServiceProperties rpcServiceProperties);

    /**
     * @param rpcServiceProperties service related attributes
     * @return service object
     */
    Object getService(RpcServiceProperties rpcServiceProperties);

    /**
     * @param service              service object
     * @param rpcServiceProperties service related attributes
     */
    void publishService(Object service, RpcServiceProperties rpcServiceProperties);

    /**
     * @param service service object
     */
    void publishService(Object service);

    void publishService(Object service, RpcServiceProperties rpcServiceProperties, int port);
}
