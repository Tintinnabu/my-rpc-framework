package top.tinn.registry;

import java.net.InetSocketAddress;

/**
 * @Author: Tinn
 * @Date: 2020/8/11 17:21
 */
public interface ServiceRegistry {
    /**
     * register service
     *
     * @param rpcServiceName    rpc service name
     * @param inetSocketAddress service address
     */
    void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress);

}
