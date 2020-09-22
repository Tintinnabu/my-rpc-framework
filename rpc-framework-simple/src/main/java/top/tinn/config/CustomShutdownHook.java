package top.tinn.config;

import lombok.extern.slf4j.Slf4j;
import top.tinn.registry.zookeeper.util.CuratorUtils;
import top.tinn.utils.concurrent.threadpool.ThreadPoolFactoryUtils;

/**
 * @Author: Tinn
 * @Date: 2020/8/12 10:18
 */
@Slf4j
public class CustomShutdownHook {

    private static final CustomShutdownHook CUSTOM_SHUTDOWN_HOOK = new CustomShutdownHook();
    public static CustomShutdownHook getCustomShutdownHook(){
        return CUSTOM_SHUTDOWN_HOOK;
    }

    public void clearAll(){
        log.info("addShutdownHook for clearAll");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            CuratorUtils.clearRegistry(CuratorUtils.getZkClient());
            ThreadPoolFactoryUtils.shutDownAllThreadPools();
        }));
    }

}
