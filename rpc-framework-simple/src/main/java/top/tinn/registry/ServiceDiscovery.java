package top.tinn.registry;

import java.net.InetSocketAddress;

/**
 * @Author: Tinn
 * @Date: 2020/8/12 10:52
 */
public interface ServiceDiscovery {
    /**
     * lookup service by rpcServiceName
     *
     * @param rpcServiceName rpc service name
     * @return service address
     */
    InetSocketAddress lookupService(String rpcServiceName);

}
