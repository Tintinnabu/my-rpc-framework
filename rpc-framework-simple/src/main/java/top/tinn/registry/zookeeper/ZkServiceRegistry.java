package top.tinn.registry.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import top.tinn.registry.zookeeper.util.CuratorUtils;
import top.tinn.registry.ServiceRegistry;

import java.net.InetSocketAddress;

/**
 * @Author: Tinn
 * @Date: 2020/8/12 10:53
 */
@Slf4j
public class ZkServiceRegistry implements ServiceRegistry {
    @Override
    public void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress) {
        String servicePath = CuratorUtils.ZK_REGISTER_ROOT_PATH + "/" + rpcServiceName
                + inetSocketAddress.toString();
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        CuratorUtils.createPersistentNode(zkClient, servicePath);
        log.info("service : {} registered.", servicePath);
    }
}
