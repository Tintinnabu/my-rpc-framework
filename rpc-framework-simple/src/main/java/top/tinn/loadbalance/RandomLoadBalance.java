package top.tinn.loadbalance;

import java.util.List;
import java.util.Random;

/**
 * @Author: Tinn
 * @Date: 2020/8/12 11:37
 */
public class RandomLoadBalance extends AbstractLoadBalance{
    @Override
    protected String doSelect(List<String> serviceAddresses) {
        Random random = new Random();
        return serviceAddresses.get(random.nextInt(serviceAddresses.size()));
    }
}
