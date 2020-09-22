package top.tinn.loadbalance;

import java.util.List;

/**
 * @Author: Tinn
 * @Date: 2020/8/12 11:34
 */
public interface LoadBalance {
    String selectServiceAddress(List<String> serviceAddresses);
}
