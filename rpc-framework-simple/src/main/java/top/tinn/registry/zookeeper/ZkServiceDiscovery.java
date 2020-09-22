package top.tinn.registry.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import top.tinn.enumeration.RpcErrorMessage;
import top.tinn.exception.RpcException;
import top.tinn.loadbalance.LoadBalance;
import top.tinn.loadbalance.RandomLoadBalance;
import top.tinn.registry.ServiceDiscovery;
import top.tinn.registry.zookeeper.util.CuratorUtils;

import java.net.InetSocketAddress;
import java.util.List;


/**
 * @Author: Tinn
 * @Date: 2020/8/12 11:31
 */
@Slf4j
public class ZkServiceDiscovery implements ServiceDiscovery {
    private final LoadBalance loadBalance;

    public ZkServiceDiscovery() {
        loadBalance = new RandomLoadBalance();
    }

    /**
     * 寻找服务地址、端口
     * @param rpcServiceName rpc service name
     * @return
     */
    @Override
    public InetSocketAddress lookupService(String rpcServiceName) {
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        List<String> serviceUrlList = CuratorUtils.getChildrenNodes(zkClient, rpcServiceName);
        if (serviceUrlList.size() == 0){
            throw new RpcException(RpcErrorMessage.SERVICE_CAN_NOT_BE_FOUND, rpcServiceName);
        }
        //load balancing
        String targetServiceUrl = loadBalance.selectServiceAddress(serviceUrlList);
        log.info("Successfully found the service address:[{}]", targetServiceUrl);
        String[] socketAddressArray = targetServiceUrl.split(":");
        String host = socketAddressArray[0];
        int port = Integer.parseInt(socketAddressArray[1]);
        return new InetSocketAddress(host, port);
    }
}
